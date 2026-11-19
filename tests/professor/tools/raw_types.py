#!/usr/bin/env python3
"""Detect raw types: generic types used without type arguments (Java).

A generic type (List<E>, Map<K, V>, Comparable<T>, Command<Receiver>, or a
generic type of the project) used without type arguments is a raw type: the
compiler no longer checks what goes in and out, and casts become necessary.

  HIGH    raw types in declarations: fields, parameters, return types, local
          variables, type arguments, type bounds, and supertypes
          (extends/implements)
  MEDIUM  raw types in object creation (`new ArrayList()` instead of
          `new ArrayList<>()`) and in casts

The diamond (`new ArrayList<>()`) is fine: the type arguments are inferred
from the declaration. Not reported (Java requires or allows the raw form
there): class literals (List.class), instanceof, method references
(ArrayList::new), generic array creation (new List[n]).

Generic types are recognized from a list of common JDK and po-uilib types and
from the project's own generic type declarations; project types shadow the
list (a project class `Stack` that is not generic is not reported).

Usage:
  raw_types.py [options] PATH...
"""

import sys

import javaast
from javaast import Finding, Tool

# generic types (simple name -> type parameters) from the JDK and po-uilib
KNOWN_GENERICS = {
    # java.util
    "Collection": "E", "List": "E", "ArrayList": "E", "LinkedList": "E",
    "Vector": "E", "Stack": "E", "Set": "E", "HashSet": "E",
    "LinkedHashSet": "E", "TreeSet": "E", "SortedSet": "E",
    "NavigableSet": "E", "EnumSet": "E", "Queue": "E", "Deque": "E",
    "ArrayDeque": "E", "PriorityQueue": "E", "Iterator": "E",
    "ListIterator": "E", "Spliterator": "T", "Optional": "T",
    "Map": "K, V", "HashMap": "K, V", "LinkedHashMap": "K, V",
    "TreeMap": "K, V", "SortedMap": "K, V", "NavigableMap": "K, V",
    "EnumMap": "K, V", "Hashtable": "K, V", "IdentityHashMap": "K, V",
    "WeakHashMap": "K, V", "Entry": "K, V", "Comparator": "T",
    "CopyOnWriteArrayList": "E", "ConcurrentHashMap": "K, V",
    "ConcurrentMap": "K, V",
    # java.lang
    "Comparable": "T", "Iterable": "T", "Class": "T", "Enum": "E",
    "ThreadLocal": "T",
    # java.util.function
    "Function": "T, R", "BiFunction": "T, U, R", "Supplier": "T",
    "Consumer": "T", "BiConsumer": "T, U", "Predicate": "T",
    "BiPredicate": "T, U", "UnaryOperator": "T", "BinaryOperator": "T",
    "ToIntFunction": "T", "ToDoubleFunction": "T", "ToLongFunction": "T",
    "IntFunction": "R",
    # java.util.stream
    "Stream": "T", "Collector": "T, A, R",
    # java.util.concurrent, java.lang.ref
    "Callable": "V", "Future": "V", "Reference": "T", "WeakReference": "T",
    "SoftReference": "T",
    # po-uilib
    "Command": "Receiver", "Field": "Type", "FieldParsed": "Type",
}

TOOL = Tool(
    name="raw-types",
    title="Raw types (generic types without type arguments)",
    description="Detect generic types used without type arguments.",
    severity_labels={
        "HIGH": "raw type in a declaration or supertype",
        "MEDIUM": "raw type in object creation or cast",
        "LOW": "-",
    },
    explanations={
        "HIGH": (
            "A generic type is used without type arguments (a raw type) in a "
            "declaration: the compiler no longer knows what the collection "
            "(or other generic object) holds, so it cannot check what is put "
            "into it, everything taken out of it is just Object and must be "
            "cast, and mistakes only appear at run time as "
            "ClassCastException. A raw supertype (e.g. `implements "
            "Comparable`) forces the inherited methods to take Object "
            "parameters (e.g. compareTo(Object)), with casts inside. Raw types "
            "exist only for compatibility with pre-Java 5 code."
        ),
        "MEDIUM": (
            "A generic type is created or cast to without type arguments: "
            "`new ArrayList()` produces a raw object that is converted "
            "unchecked to the declared type (the compiler warns about "
            "unchecked operations), and a raw cast discards the type "
            "information. Use the diamond (`new ArrayList<>()`), which infers "
            "the type arguments from the declaration."
        ),
        "LOW": "-",
    },
    advice=(
        "Always give generic types their type arguments (List<Product>, "
        "Map<String, Client>, Comparable<Product>, Command<ErpManager>); in "
        "`new`, use the diamond (new ArrayList<>()). Compile with "
        "-Xlint:unchecked to see the warnings raw types cause."
    ),
    analyze=None,   # set below
    prepare=None,   # set below
    none_found="No issues found: no raw types.",
)

HIERARCHY = javaast.Hierarchy()


def prepare(analysed, others):
    global HIERARCHY
    HIERARCHY = javaast.build_hierarchy(analysed, others)


def _type_params(name, package, imports, enclosing):
    """Type parameters of the named type if it is generic, else None."""
    fqcn = HIERARCHY.resolve(name, package, imports, enclosing)
    if fqcn is not None:
        tp = HIERARCHY.types[fqcn].type_params
        return ", ".join(tp) if tp else None
    simple = name.rsplit(".", 1)[-1]
    if simple in HIERARCHY.by_simple:
        return None  # a project type of that name, not resolved: do not guess
    if "." in name:
        qual = name.rsplit(".", 1)[0]
        # java.util.List, pt.tecnico.uilib.menus.Command, Map.Entry
        if not (qual.startswith("java.") or qual.startswith("pt.tecnico.")
                or qual in ("Map",)):
            return None
    return KNOWN_GENERICS.get(simple)


def _role(u):
    """Where the (outermost) type node u appears: (role, severity) or None."""
    p = u.parent
    if p is None:
        return None
    pt = p.type
    if pt == "object_creation_expression" and p.child_by_field_name("type") == u:
        return "object creation", "MEDIUM"
    if pt == "cast_expression":
        return "cast", "MEDIUM"
    if pt in ("array_creation_expression", "instanceof_expression",
              "class_literal", "method_reference", "scoped_type_identifier",
              "catch_type", "throws", "annotation", "marker_annotation",
              "import_declaration", "type_pattern", "record_pattern"):
        return None
    if pt == "field_declaration":
        return "field", "HIGH"
    if pt in ("formal_parameter", "spread_parameter"):
        return "parameter", "HIGH"
    if pt == "method_declaration":
        return "return type", "HIGH"
    if pt in ("local_variable_declaration", "resource", "enhanced_for_statement"):
        return "local variable", "HIGH"
    if pt == "type_arguments":
        return "type argument", "HIGH"
    if pt == "type_bound":
        return "type bound", "HIGH"
    if pt in ("superclass", "type_list"):
        return "supertype", "HIGH"
    if pt == "wildcard":
        return "wildcard bound", "HIGH"
    return "declaration", "HIGH"


def analyze(sf):
    findings = []
    package, imports = HIERARCHY.files.get(sf.path, ("", ((), ())))
    for node, stack, _in_code in javaast.walk(sf.root):
        if node.type not in ("type_identifier", "scoped_type_identifier"):
            continue
        p = node.parent
        if p is not None and p.type == "scoped_type_identifier":
            continue  # part of a qualified name (e.g. Map in Map.Entry)
        if p is not None and p.type == "generic_type":
            continue  # has type arguments (or the diamond)
        # type variables of enclosing generic declarations are not raw
        name = javaast.text(node)
        enclosing = ".".join(([package] if package else []) +
                             [c.name for c in stack if not c.is_local]) or None
        params = _type_params(name, package, imports, enclosing)
        if params is None:
            continue
        u = node
        while u.parent is not None and u.parent.type == "array_type":
            u = u.parent
        r = _role(u)
        if r is None:
            continue
        role, severity = r
        # `new X[n]` of a generic type is fine, but X[] as a declared type is not
        suggestion = "%s<%s>" % (name, params)
        detail = "%s %s: use %s" % (role, name, suggestion)
        note = ""
        if role == "object creation":
            detail = "new %s(...): use new %s<>(...)" % (name, name)
            decl = u.parent.parent
            if decl is not None and decl.type == "variable_declarator":
                ld = decl.parent
                if ld is not None and javaast.text(ld.child_by_field_name("type")) == "var":
                    note = "declared with `var`: the variable itself is raw"
        line, col = javaast.position(node)
        findings.append(Finding(
            sf.display, line, col, severity, javaast.member_path(node, stack),
            detail, note, {"type": name, "role": role, "parameters": params}))
    return findings


TOOL.analyze = analyze
TOOL.prepare = prepare

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
