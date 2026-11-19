#!/usr/bin/env python3
"""Detect fields repeated along the class hierarchy (Java).

A subclass inherits the representation of its superclass; declaring the same
data again in the subclass, or in each of several sibling subclasses, is a
repetition. Only instance (non-static) fields of project classes are
considered, and only `extends` relationships between classes:

  HIGH    a subclass declares a field that a project superclass (at any level)
          already declares: same name, or a name that differs only in
          underscores or letter case (e.g. `_name` / `name`). This holds even
          if the superclass field is private: the object then holds two copies
          of the same data.
  MEDIUM  all the direct subclasses (at least two) of a project class
          declare the same field (same name and type, up to underscores and
          case), which their superclass does not have: it belongs in the
          superclass.
  LOW     the same, but only some (at least two) of the direct subclasses.

Usage:
  duplicate_fields.py [options] PATH...
"""

import re
import sys

import javaast
from javaast import Finding, Tool

TOOL = Tool(
    name="duplicate-fields",
    title="Fields repeated along the class hierarchy",
    description="Detect subclass fields that repeat superclass fields, and "
                "fields repeated in sibling subclasses.",
    severity_labels={
        "HIGH": "repeats a superclass field",
        "MEDIUM": "repeated in all subclasses",
        "LOW": "repeated in some subclasses",
    },
    explanations={
        "HIGH": (
            "The subclass declares a field that its superclass already has. "
            "Every object of the subclass then contains two variables for the "
            "same piece of data: the superclass's methods use one and the "
            "subclass's methods use the other, so they easily get out of sync "
            "(e.g. the subclass's constructor sets its own copy, while the "
            "inherited methods still see the superclass's). If the names are "
            "identical, the subclass field also hides the inherited one, which "
            "makes the code confusing. The subclass should use the inherited "
            "data (through the superclass's constructor and methods)."
        ),
        "MEDIUM": (
            "Every subclass of the same superclass declares this field: the "
            "data is common to the whole family and is repeated instead of "
            "being declared once in the superclass. The code that manages it "
            "(constructors, getters, operations) is usually repeated too, and "
            "a new subclass has to remember to repeat it again."
        ),
        "LOW": (
            "Several (but not all) subclasses of the same superclass declare "
            "this field. Check whether it is common data that belongs in the "
            "superclass (or in an intermediate class shared by those "
            "subclasses)."
        ),
    },
    advice=(
        "Declare each piece of data once, in the most general class to which "
        "it applies (private, initialized by that class's constructor); "
        "subclasses pass the values to super(...) and use the inherited "
        "methods instead of keeping their own copies."
    ),
    analyze=None,   # set below
    prepare=None,   # set below
    none_found="No issues found: no fields repeated along the class hierarchy.",
)

FINDINGS = {}   # SourceFile.path -> [Finding]


def _norm_name(n):
    return n.strip("_").lower()


_QUALIFIER = re.compile(r"\b(?:[a-z_][\w]*\.)+(?=[A-Z_])")


def _norm_type(t):
    """Type text without spaces and package qualifiers
    (java.util.List<String> -> List<String>)."""
    return _QUALIFIER.sub("", t.replace(" ", ""))


def _local_name(h, fqcn):
    info = h.types[fqcn]
    pkg = info.package
    return fqcn[len(pkg) + 1:] if pkg and fqcn.startswith(pkg + ".") else fqcn


def _add(h, info, f, severity, detail, note, extra):
    FINDINGS.setdefault(info.file, []).append(Finding(
        info.display, f.line, f.column, severity,
        "%s.%s" % (_local_name(h, info.fqcn), f.name), detail, note, extra))


def prepare(analysed, others):
    FINDINGS.clear()
    h = javaast.build_hierarchy(analysed, others)
    analysed_paths = {sf.path for sf in analysed}

    def instance_fields(info):
        return [f for f in info.fields if "static" not in f.modifiers]

    # -- HIGH: repeats a field of a superclass (any level)
    repeated = set()   # (fqcn, field name) already reported as HIGH
    for info in h.types.values():
        if info.kind != "class" or not info.superclass:
            continue
        chain = h.superclass_chain(info.fqcn)
        for f in instance_fields(info):
            match = None
            for s in chain:
                for g in instance_fields(h.types[s]):
                    if _norm_name(g.name) == _norm_name(f.name):
                        match = (s, g)
                        break
                if match:
                    break
            if match is None:
                continue
            repeated.add((info.fqcn, f.name))
            if info.file not in analysed_paths:
                continue
            s, g = match
            notes = []
            if g.name != f.name:
                notes.append("names differ only in underscores/case: %s vs %s"
                             % (f.name, g.name))
            else:
                notes.append("same name: the subclass field hides the inherited one")
            if "private" in g.modifiers:
                notes.append("the superclass field is private: the object holds "
                             "two copies of the same data")
            if _norm_type(g.type) != _norm_type(f.type):
                notes.append("different types: %s vs %s" % (f.type, g.type))
            _add(h, info, f, "HIGH",
                 "%s %s repeats %s.%s (%s)" % (f.type, f.name,
                                               _local_name(h, s), g.name, g.type),
                 "; ".join(notes),
                 {"kind": "superclass", "superclass": s, "superclass_field": g.name})

    # -- MEDIUM/LOW: repeated in sibling subclasses
    for parent in h.types.values():
        subs = [s for s in h.direct_subclasses(parent.fqcn)
                if h.types[s].kind == "class"]
        if len(subs) < 2:
            continue
        groups = {}   # (norm name, norm type) -> [(sub fqcn, FieldInfo)]
        for s in subs:
            seen = set()
            for f in instance_fields(h.types[s]):
                if (s, f.name) in repeated:
                    continue
                key = (_norm_name(f.name), _norm_type(f.type))
                if key in seen:
                    continue
                seen.add(key)
                groups.setdefault(key, []).append((s, f))
        for key, members in groups.items():
            if len(members) < 2:
                continue
            severity = "MEDIUM" if len(members) == len(subs) else "LOW"
            provided = parent.file not in analysed_paths
            pname = _local_name(h, parent.fqcn)
            for s, f in members:
                info = h.types[s]
                if info.file not in analysed_paths:
                    continue
                others_ = [_local_name(h, o) for o, _ in members if o != s]
                if provided:
                    action = ("%s is provided (cannot be changed): check "
                              "whether the data is already available from it "
                              "(e.g. a command's receiver)" % pname)
                else:
                    action = "move to %s" % pname
                _add(h, info, f, severity,
                     "%s %s also in %s: %s" % (
                         f.type, f.name, ", ".join(others_), action),
                     "declared in %d of the %d subclasses of %s"
                     % (len(members), len(subs), pname),
                     {"kind": "siblings", "superclass": parent.fqcn,
                      "superclass_provided": provided,
                      "siblings": [o for o, _ in members]})


def analyze(sf):
    return FINDINGS.get(sf.path, [])


TOOL.analyze = analyze
TOOL.prepare = prepare

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
