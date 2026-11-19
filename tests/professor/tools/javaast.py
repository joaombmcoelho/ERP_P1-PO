"""Shared infrastructure for the Java code-quality tools (tree-sitter based).

Provides:
  * parsing (tree-sitter-java), with syntax-error locations;
  * helpers on tree-sitter nodes (text, modifiers, positions);
  * a tree walk that tracks the chain of enclosing types (TypeContext);
  * file selection (skipping canonical `sane/` files and po-uilib);
  * a common Finding/Report model, text/JSON output, and a CLI driver.

A tool is a `Tool` object with a name, a title, per-severity explanations,
advice, and an `analyze(source_file)` function returning Findings.
"""

import argparse
import json
import os
import sys
from dataclasses import asdict, dataclass, field
from typing import Callable, Dict, List, Optional

import tree_sitter_java
from tree_sitter import Language, Node, Parser

SCRIPT_DIR = os.path.dirname(os.path.abspath(__file__))
DEFAULT_SANE_DIR = os.path.normpath(os.path.join(SCRIPT_DIR, "..", "sane"))

SEVERITIES = ("HIGH", "MEDIUM", "LOW")
SEVERITY_RANK = {s: i for i, s in enumerate(SEVERITIES)}

JAVA = Language(tree_sitter_java.language())
_PARSER = Parser(JAVA)

# node types that declare a named type
TYPE_DECLARATIONS = {
    "class_declaration": "class",
    "enum_declaration": "enum",
    "record_declaration": "record",
    "interface_declaration": "interface",
    "annotation_type_declaration": "annotation",
}

# node types whose contents are executable code (types declared inside are
# local classes)
CODE_NODES = {
    "method_declaration", "constructor_declaration", "compact_constructor_declaration",
    "block", "lambda_expression", "static_initializer", "variable_declarator",
}

# exception types too general to be caught or declared
BROAD_EXCEPTIONS = {"Exception", "Throwable", "RuntimeException", "Error"}


def is_broad_exception(type_name: str) -> bool:
    """True for Exception, Throwable, RuntimeException, Error, with or without
    the java.lang. prefix."""
    name = type_name.replace(" ", "")
    if name.startswith("java.lang."):
        name = name[len("java.lang."):]
    return name in BROAD_EXCEPTIONS


MODIFIER_ORDER = ["public", "protected", "private", "abstract", "static",
                  "final", "sealed", "non-sealed", "transient", "volatile",
                  "synchronized", "native", "strictfp", "default"]


# ---------------------------------------------------------------------------
# Parsing


@dataclass
class SourceFile:
    path: str            # path as given/discovered
    display: str         # path used in reports
    source: bytes
    tree: object
    syntax_errors: list  # [(line, column)] 1-based

    @property
    def root(self) -> Node:
        return self.tree.root_node


def parse_source(source: bytes):
    return _PARSER.parse(source)


def find_syntax_errors(root: Node, limit=20):
    """Locations (1-based line, column) of ERROR and MISSING nodes."""
    errors = []
    if not root.has_error:
        return errors
    stack = [root]
    while stack and len(errors) < limit:
        n = stack.pop()
        if n.type == "ERROR" or n.is_missing:
            errors.append((n.start_point[0] + 1, n.start_point[1] + 1))
            continue
        if n.has_error:
            stack.extend(reversed(n.children))
    return sorted(errors)


# Optional parse cache (path -> (source, tree, errors)), used by drivers that
# run several tools over the same files. Disabled (None) by default.
_LOAD_CACHE = None


def set_load_cache(enabled: bool):
    """Enable (and clear) or disable the parse cache."""
    global _LOAD_CACHE
    _LOAD_CACHE = {} if enabled else None


def load(path, display=None) -> SourceFile:
    if _LOAD_CACHE is not None and path in _LOAD_CACHE:
        source, tree, errors = _LOAD_CACHE[path]
    else:
        with open(path, "rb") as f:
            source = f.read()
        tree = parse_source(source)
        errors = find_syntax_errors(tree.root_node)
        if _LOAD_CACHE is not None:
            _LOAD_CACHE[path] = (source, tree, errors)
    return SourceFile(path, display or path, source, tree, errors)


# ---------------------------------------------------------------------------
# Node helpers


def text(node: Optional[Node]) -> str:
    if node is None:
        return ""
    return " ".join(node.text.decode("utf-8", "replace").split())


def position(node: Node):
    """1-based (line, column)."""
    return node.start_point[0] + 1, node.start_point[1] + 1


def modifiers_node(node: Node) -> Optional[Node]:
    for c in node.children:
        if c.type == "modifiers":
            return c
    return None


def modifiers(node: Node) -> set:
    """Keyword modifiers of a declaration (annotations excluded)."""
    m = modifiers_node(node)
    if m is None:
        return set()
    return {c.type for c in m.children
            if c.type not in ("marker_annotation", "annotation")}


def annotations(node: Node) -> List[str]:
    m = modifiers_node(node)
    if m is None:
        return []
    return [text(c) for c in m.children
            if c.type in ("marker_annotation", "annotation")]


def sort_modifiers(mods) -> List[str]:
    return sorted(mods, key=lambda m: (
        MODIFIER_ORDER.index(m) if m in MODIFIER_ORDER else len(MODIFIER_ORDER), m))


def access(mods) -> Optional[str]:
    for a in ("public", "protected", "private"):
        if a in mods:
            return a
    return None


def package_of(root: Node) -> str:
    for c in root.named_children:
        if c.type == "package_declaration":
            for n in c.named_children:
                if n.type in ("scoped_identifier", "identifier"):
                    return text(n)
    return ""


def imports_of(root: Node):
    """(single-type imports, on-demand packages), excluding static imports."""
    single, wildcard = [], []
    for c in root.named_children:
        if c.type != "import_declaration":
            continue
        if any(k.type == "static" for k in c.children):
            continue
        name = next((text(n) for n in c.named_children
                     if n.type in ("scoped_identifier", "identifier")), "")
        if any(k.type == "asterisk" for k in c.children):
            wildcard.append(name)
        elif name:
            single.append(name)
    return single, wildcard


def enclosing_method(node: Node) -> Optional[Node]:
    """Nearest enclosing method/constructor declaration (crossing lambdas,
    but not type declarations)."""
    n = node.parent
    while n is not None:
        if n.type in ("method_declaration", "constructor_declaration",
                      "compact_constructor_declaration"):
            return n
        if n.type in TYPE_DECLARATIONS or n.type == "class_body":
            return None
        n = n.parent
    return None


# ---------------------------------------------------------------------------
# Walking with type context


@dataclass(frozen=True)
class TypeContext:
    name: str
    kind: str            # class, enum, record, interface, annotation, anonymous
    is_private: bool
    is_local: bool       # declared inside code (local or anonymous class)
    node: Node = field(compare=False, repr=False)


def type_context_of(node: Node, in_code: bool) -> Optional[TypeContext]:
    kind = TYPE_DECLARATIONS.get(node.type)
    if kind:
        name = text(node.child_by_field_name("name")) or "?"
        return TypeContext(name, kind, "private" in modifiers(node), in_code, node)
    if node.type == "object_creation_expression":
        for c in node.children:
            if c.type == "class_body":
                t = text(node.child_by_field_name("type")) or "?"
                return TypeContext("<anonymous %s>" % t, "anonymous", False, True, node)
    return None


def walk(root: Node):
    """Yield (node, stack, in_code) for every named node in pre-order.

    `stack` is the tuple of enclosing TypeContexts (innermost last), including
    the node's own context when the node itself declares a type. `in_code` is
    True when the node lies inside executable code of the innermost type.
    Iterative, so deeply nested expressions do not hit the recursion limit.
    """
    todo = [(root, (), False)]
    while todo:
        node, stack, in_code = todo.pop()
        ctx = type_context_of(node, in_code)
        if ctx is not None:
            stack = stack + (ctx,)
        if node.is_named:
            yield node, stack, in_code
        if ctx is not None:
            child_in_code = False if ctx.kind != "anonymous" else in_code
        else:
            child_in_code = in_code or node.type in CODE_NODES
        for c in reversed(node.children):
            if ctx is not None and ctx.kind == "anonymous":
                # the anonymous class body is a fresh (non-code) context;
                # its constructor arguments stay in the enclosing code
                todo.append((c, stack if c.type == "class_body" else stack[:-1],
                             False if c.type == "class_body" else in_code))
            else:
                todo.append((c, stack, child_in_code))


def type_path(stack) -> str:
    return ".".join(c.name for c in stack)


def signature(node: Node) -> str:
    """Parameter types of a method/constructor, e.g. "(int, String...)"."""
    params = node.child_by_field_name("parameters")
    types = []
    if params is not None:
        for p in params.named_children:
            if p.type == "formal_parameter":
                types.append(text(p.child_by_field_name("type")) +
                             text(p.child_by_field_name("dimensions")))
            elif p.type == "spread_parameter":
                t = [c for c in p.named_children
                     if c.type not in ("modifiers", "variable_declarator")]
                types.append((text(t[0]) if t else "?") + "...")
    return "(%s)" % ", ".join(types)


MEMBER_NODES = {
    "method_declaration", "constructor_declaration",
    "compact_constructor_declaration", "static_initializer", "block",
    "field_declaration",
}


def member_path(node: Node, stack) -> str:
    """Name of the member (method, constructor, initializer, field) of the
    innermost enclosing type that contains `node`, prefixed by the type path,
    e.g. "Erp.Inner.load(String)". Falls back to the type path."""
    innermost = stack[-1].node if stack else None
    n = node.parent
    member = None
    while n is not None and n != innermost:
        if n.type in MEMBER_NODES and n.parent is not None and \
           n.parent.type in ("class_body", "enum_body_declarations",
                             "interface_body", "annotation_type_body"):
            member = n
        n = n.parent
    base = type_path(stack)
    if member is None:
        return base
    t = member.type
    if t == "method_declaration":
        name = text(member.child_by_field_name("name")) + signature(member)
    elif t in ("constructor_declaration", "compact_constructor_declaration"):
        name = "<init>" + (signature(member) if t == "constructor_declaration" else "")
    elif t == "static_initializer":
        name = "<static initializer>"
    elif t == "block":
        name = "<instance initializer>"
    else:  # field initializer
        names = [text(d.child_by_field_name("name"))
                 for d in member.children_by_field_name("declarator")]
        name = ",".join(names)
    return "%s.%s" % (base, name) if base else name


def visibility_note(stack) -> str:
    """Note explaining that enclosing types narrow the effective visibility."""
    if any(c.is_local for c in stack):
        return ("declared in an anonymous or local class, so its effective "
                "visibility is limited")
    for c in stack:
        if c.is_private:
            return ("enclosing type '%s' is private, which limits the effective "
                    "visibility to the outer class" % c.name)
    return ""


# ---------------------------------------------------------------------------
# Project class hierarchy


@dataclass
class TypeInfo:
    fqcn: str
    simple: str
    kind: str
    package: str
    imports: tuple
    supers_raw: list = field(default_factory=list)
    methods: set = field(default_factory=set)
    supers: list = field(default_factory=list)   # resolved fqcns
    type_params: list = field(default_factory=list)  # e.g. ["K", "V"]
    superclass_raw: str = ""                          # `extends` of a class
    superclass: Optional[str] = None                  # resolved fqcn
    fields: list = field(default_factory=list)        # [FieldInfo]
    file: str = ""                                    # SourceFile.path
    display: str = ""                                 # SourceFile.display


@dataclass
class FieldInfo:
    name: str
    type: str          # declared type text (with declarator dimensions)
    modifiers: set
    line: int
    column: int


class Hierarchy:
    def __init__(self):
        self.types = {}       # fqcn -> TypeInfo
        self.by_simple = {}   # simple name -> [fqcn]
        self.files = {}       # SourceFile.path -> (package, imports)

    # -- building

    def add_file(self, sf):
        package = package_of(sf.root)
        imports = imports_of(sf.root)
        self.files[sf.path] = (package, imports)
        for node, stack, _ in walk(sf.root):
            if node.type not in TYPE_DECLARATIONS:
                continue
            if any(c.is_local for c in stack):
                continue
            fqcn = ".".join(([package] if package else []) +
                            [c.name for c in stack])
            info = TypeInfo(fqcn, stack[-1].name, stack[-1].kind, package,
                            imports)
            info.supers_raw = _super_names(node)
            info.methods = _method_names(node)
            info.file, info.display = sf.path, sf.display
            sc = node.child_by_field_name("superclass")
            if sc is not None:
                info.superclass_raw = next(
                    (type_base_name(k) for k in sc.named_children), "")
            info.fields = _fields(node)
            tp = node.child_by_field_name("type_parameters")
            if tp is not None:
                info.type_params = [
                    text(next((k for k in p.named_children
                               if k.type in ("type_identifier", "identifier")), p))
                    for p in tp.named_children if p.type == "type_parameter"]
            self.types[fqcn] = info
            self.by_simple.setdefault(info.simple, []).append(fqcn)

    def link(self):
        for info in self.types.values():
            for raw in info.supers_raw:
                r = self.resolve(raw, info.package, info.imports, info.fqcn)
                if r and r != info.fqcn:
                    info.supers.append(r)
            if info.superclass_raw:
                r = self.resolve(info.superclass_raw, info.package,
                                 info.imports, info.fqcn)
                if r and r != info.fqcn:
                    info.superclass = r

    # -- queries

    def resolve(self, name, package, imports, enclosing=None):
        """Resolve a (possibly qualified) type name to a project fqcn."""
        if not name:
            return None
        if "." in name:
            if name in self.types:
                return name
            cand = (package + "." + name) if package else name
            if cand in self.types:
                return cand
            head, rest = name.split(".", 1)
            r = self.resolve(head, package, imports, enclosing)
            if r and (r + "." + rest) in self.types:
                return r + "." + rest
            return None
        cands = self.by_simple.get(name)
        if not cands:
            return None
        # nested in an enclosing type (or its supertypes' nested types)
        e = enclosing
        while e:
            if (e + "." + name) in self.types:
                return e + "." + name
            e = e.rsplit(".", 1)[0] if "." in e else None
        single, wildcard = imports
        for imp in single:
            if imp.endswith("." + name) and imp in self.types:
                return imp
        same = (package + "." + name) if package else name
        if same in self.types:
            return same
        for w in wildcard:
            if (w + "." + name) in self.types:
                return w + "." + name
        if len(cands) == 1:
            return cands[0]
        return None

    def superclass_chain(self, fqcn):
        """Project superclasses (extends only), nearest first."""
        chain, seen = [], {fqcn}
        s = self.types[fqcn].superclass
        while s and s not in seen:
            chain.append(s)
            seen.add(s)
            s = self.types[s].superclass
        return chain

    def direct_subclasses(self, fqcn):
        return [t.fqcn for t in self.types.values() if t.superclass == fqcn]

    def all_supers(self, fqcn):
        seen, order, todo = set(), [], list(self.types[fqcn].supers)
        while todo:
            s = todo.pop(0)
            if s in seen or s == fqcn:
                continue
            seen.add(s)
            order.append(s)
            todo.extend(self.types[s].supers)
        return order

    def available_methods(self, fqcn):
        m = set(self.types[fqcn].methods)
        for s in self.all_supers(fqcn):
            m |= self.types[s].methods
        return m


def type_base_name(t):
    """Name of a type node without type arguments/dimensions."""
    if t is None:
        return ""
    if t.type == "generic_type":
        for c in t.named_children:
            if c.type in ("type_identifier", "scoped_type_identifier"):
                return text(c)
        return ""
    if t.type in ("type_identifier", "scoped_type_identifier"):
        return text(t)
    return ""


def _super_names(decl):
    names = []
    for c in decl.children:
        if c.type in ("superclass", "super_interfaces", "extends_interfaces"):
            stack = [c]
            while stack:
                n = stack.pop()
                for k in n.named_children:
                    if k.type in ("type_identifier", "scoped_type_identifier",
                                  "generic_type"):
                        names.append(type_base_name(k))
                    elif k.type == "type_list":
                        stack.append(k)
    return [n for n in names if n]


def _method_names(decl):
    body = decl.child_by_field_name("body")
    names = set()
    if body is None:
        return names
    members = list(body.named_children)
    for m in list(members):
        if m.type == "enum_body_declarations":
            members.extend(m.named_children)
    for m in members:
        if m.type == "method_declaration":
            names.add(text(m.child_by_field_name("name")))
    return names




def _fields(decl):
    body = decl.child_by_field_name("body")
    out = []
    if body is None:
        return out
    members = list(body.named_children)
    for m in list(members):
        if m.type == "enum_body_declarations":
            members.extend(m.named_children)
    for m in members:
        if m.type != "field_declaration":
            continue
        mods = modifiers(m)
        ftype = text(m.child_by_field_name("type"))
        for d in m.children_by_field_name("declarator"):
            n = d.child_by_field_name("name")
            line, col = position(n or d)
            out.append(FieldInfo(text(n), ftype + text(d.child_by_field_name(
                "dimensions")), mods, line, col))
    return out


def build_hierarchy(analysed, others=()):
    """Build and link the class hierarchy of the given SourceFiles."""
    h = Hierarchy()
    for sf in list(analysed) + list(others):
        h.add_file(sf)
    h.link()
    return h


# ---------------------------------------------------------------------------
# File selection


def load_sane_suffixes(sane_dir):
    suffixes = set()
    if not sane_dir or not os.path.isdir(sane_dir):
        return suffixes
    for root, _dirs, files in os.walk(sane_dir):
        for name in files:
            if name.endswith(".java"):
                rel = os.path.relpath(os.path.join(root, name), sane_dir)
                suffixes.add(rel.replace(os.sep, "/"))
    return suffixes


FRAMEWORK_PACKAGE = "pt.tecnico.uilib"


def is_framework(sf) -> bool:
    """True for po-uilib sources (by package), wherever they are stored."""
    pkg = package_of(sf.root)
    return pkg == FRAMEWORK_PACKAGE or pkg.startswith(FRAMEWORK_PACKAGE + ".")


def is_skipped(path, sane_suffixes):
    norm = os.path.abspath(path).replace(os.sep, "/")
    if any(part.startswith("po-uilib") for part in norm.split("/")):
        return True
    if "/pt/tecnico/uilib/" in norm:
        return True
    return any(norm.endswith("/" + s) for s in sane_suffixes)


def collect_java_files(paths):
    for p in paths:
        if os.path.isfile(p):
            if p.endswith(".java"):
                yield p
            continue
        for root, dirs, files in os.walk(p):
            dirs[:] = sorted(d for d in dirs if not d.startswith("."))
            for name in sorted(files):
                if name.endswith(".java"):
                    yield os.path.join(root, name)


# ---------------------------------------------------------------------------
# Findings and reports


@dataclass
class Finding:
    file: str
    line: int
    column: int
    severity: str
    symbol: str          # e.g. "Company.Inner.name"
    detail: str          # e.g. the offending declaration
    note: str = ""
    extra: dict = field(default_factory=dict)


@dataclass
class Report:
    findings: list = field(default_factory=list)
    unreadable: list = field(default_factory=list)      # [(file, error)]
    syntax_errors: list = field(default_factory=list)   # [(file, [(l, c)])]
    files_scanned: int = 0
    files_skipped: int = 0

    def counts(self):
        c = {s: 0 for s in SEVERITIES}
        for f in self.findings:
            c[f.severity] += 1
        return c


@dataclass
class Tool:
    name: str                       # e.g. "nonprivate-fields"
    title: str                      # e.g. "Non-private instance fields"
    description: str                # one paragraph for --help
    severity_labels: Dict[str, str]  # severity -> short label
    explanations: Dict[str, str]    # severity -> why it is a problem
    advice: str                     # how to fix
    analyze: Callable[[SourceFile], List[Finding]]
    none_found: str = "No issues found."
    # optional finer-grained explanations, keyed by Finding.extra["kind"]:
    #   kind -> (label, explanation). When given, the report explains each
    #   kind that occurs instead of each severity.
    kind_explanations: Optional[Dict[str, tuple]] = None
    # optional whole-project pre-pass, called before any analyze() with
    # (analysed files, other files). The other files are the skipped ones
    # (canonical/sane files, po-uilib), still useful as context (e.g. the
    # class hierarchy). Tools without it are analysed file by file.
    prepare: Optional[Callable[[List[SourceFile], List[SourceFile]], None]] = None


def _kinds_present(tool, report):
    """Kinds that occur, ordered by their most severe finding, then by name
    order in tool.kind_explanations."""
    order = list(tool.kind_explanations)
    best = {}
    for f in report.findings:
        k = f.extra.get("kind")
        if k in tool.kind_explanations:
            best[k] = min(best.get(k, len(SEVERITIES)), SEVERITY_RANK[f.severity])
    return sorted(best, key=lambda k: (best[k], order.index(k)))


def run_analysis(tool: Tool, paths, sane_suffixes, base=None) -> Report:
    report = Report()
    analysed, others = [], []
    for path in collect_java_files(paths):
        display = os.path.relpath(path, base) if base else path
        skipped = is_skipped(path, sane_suffixes)
        if skipped:
            report.files_skipped += 1
            if tool.prepare is None:
                continue
        else:
            report.files_scanned += 1
        try:
            sf = load(path, display)
        except OSError as e:
            if not skipped:
                report.unreadable.append((display, str(e)))
            continue
        if not skipped and is_framework(sf):
            # po-uilib sources stored elsewhere in the project
            skipped = True
            report.files_scanned -= 1
            report.files_skipped += 1
        if skipped:
            others.append(sf)
            continue
        if sf.syntax_errors:
            report.syntax_errors.append((display, sf.syntax_errors))
        if tool.prepare is None:
            report.findings.extend(tool.analyze(sf))
        else:
            analysed.append(sf)
    if tool.prepare is not None:
        tool.prepare(analysed, others)
        for sf in analysed:
            report.findings.extend(tool.analyze(sf))
    report.findings.sort(key=lambda f: (f.file, f.line, f.column))
    return report


def render_text(tool: Tool, report: Report, explain=True, stats=True) -> str:
    """Text report. With stats=False, the file statistics and the lists of
    files with syntax errors/unreadable files are left out (for drivers that
    show them once for several tools)."""
    out = []
    c = report.counts()
    out.append("%s: %d (%s)" % (tool.title, len(report.findings),
               ", ".join("%s: %d" % (s, c[s]) for s in SEVERITIES)))
    if stats:
        out.append("Files scanned: %d (skipped: %d, with syntax errors: %d)"
                   % (report.files_scanned, report.files_skipped,
                      len(report.syntax_errors)))

    current = None
    for f in report.findings:
        if f.file != current:
            current = f.file
            out.append("")
            out.append(f.file)
        out.append("  %4d:%-3d [%-6s] %s  (%s)"
                   % (f.line, f.column, f.severity, f.symbol, f.detail))
        if f.note:
            out.append("             note: " + f.note)

    if stats and report.syntax_errors:
        out.append("")
        out.append("Files with syntax errors (analysed as far as possible; "
                   "results near the errors may be incomplete):")
        for name, errs in report.syntax_errors:
            out.append("  %s: line %s" % (
                name, ", ".join(str(l) for l, _c in errs[:5]) +
                (", ..." if len(errs) > 5 else "")))
    if stats and report.unreadable:
        out.append("")
        out.append("Files that could not be read:")
        for name, err in report.unreadable:
            out.append("  %s: %s" % (name, err))

    out.append("")
    if not report.findings:
        out.append(tool.none_found)
    elif explain:
        out.append("Why this is a problem:")
        if tool.kind_explanations:
            for k in _kinds_present(tool, report):
                label, expl = tool.kind_explanations[k]
                sevs = sorted({f.severity for f in report.findings
                               if f.extra.get("kind") == k},
                              key=SEVERITY_RANK.get)
                out.append("  [%s] %s: %s" % ("/".join(sevs), label, expl))
        else:
            for sev in SEVERITIES:
                if c[sev]:
                    out.append("  [%s] %s: %s" % (sev, tool.severity_labels[sev],
                                                  tool.explanations[sev]))
        out.append("  Fix: " + tool.advice)
    return "\n".join(out)


def render_json(tool: Tool, report: Report) -> str:
    return json.dumps({
        "tool": tool.name,
        "summary": {
            "total": len(report.findings),
            **report.counts(),
            "files_scanned": report.files_scanned,
            "files_skipped": report.files_skipped,
            "files_with_syntax_errors": len(report.syntax_errors),
            "files_unreadable": len(report.unreadable),
        },
        "explanations": {
            sev: {"label": tool.severity_labels[sev],
                  "explanation": tool.explanations[sev]} for sev in SEVERITIES},
        "kind_explanations": (
            {k: {"label": l, "explanation": e}
             for k, (l, e) in tool.kind_explanations.items()}
            if tool.kind_explanations else None),
        "advice": tool.advice,
        "findings": [asdict(f) for f in report.findings],
        "syntax_errors": [{"file": n, "locations": [list(e) for e in errs]}
                          for n, errs in report.syntax_errors],
        "unreadable": [{"file": n, "error": e} for n, e in report.unreadable],
    }, indent=2, ensure_ascii=False)


def main(tool: Tool, argv=None) -> int:
    ap = argparse.ArgumentParser(prog=tool.name, description=tool.description)
    ap.add_argument("paths", nargs="+", metavar="PATH",
                    help="project directory, directory, or .java file")
    ap.add_argument("--format", choices=("text", "json"), default="text")
    ap.add_argument("--no-explain", action="store_true",
                    help="omit the explanation section (text format)")
    ap.add_argument("--sane-dir", default=DEFAULT_SANE_DIR,
                    help="directory with canonical files to skip "
                         "(default: %(default)s)")
    ap.add_argument("--no-skip-sane", action="store_true",
                    help="also analyse the canonical (sane) files")
    ap.add_argument("--min-severity", choices=SEVERITIES, default="LOW",
                    help="report only this severity or higher")
    ap.add_argument("--fail-on", choices=SEVERITIES,
                    help="exit with status 1 if any finding at this "
                         "severity or higher exists")
    args = ap.parse_args(argv)

    for p in args.paths:
        if not os.path.exists(p):
            ap.error("no such file or directory: %s" % p)

    sane = set() if args.no_skip_sane else load_sane_suffixes(args.sane_dir)
    base = (args.paths[0] if len(args.paths) == 1 and os.path.isdir(args.paths[0])
            else None)
    report = run_analysis(tool, args.paths, sane, base)

    limit = SEVERITY_RANK[args.min_severity]
    report.findings = [f for f in report.findings
                       if SEVERITY_RANK[f.severity] <= limit]

    if args.format == "json":
        print(render_json(tool, report))
    else:
        print(render_text(tool, report, explain=not args.no_explain))

    if args.fail_on:
        lim = SEVERITY_RANK[args.fail_on]
        if any(SEVERITY_RANK[f.severity] <= lim for f in report.findings):
            return 1
    return 0
