#!/usr/bin/env python3
"""Detect uses of `static` in Java code (constants excepted).

In an object-oriented design, state and behaviour belong to objects. Every
explicit use of `static` is reported, except `static final` fields
(constants):

  HIGH    static non-final fields     -- global mutable state
          static methods (except main) -- procedural code, no polymorphism
  MEDIUM  static initializer blocks   -- initialization of class-wide state
  LOW     static nested types (class, interface, enum, record)
                                      -- tolerable, but still worth a look

`main` methods (`static void main(String[])`) are not reported: the entry
point must be static.

Interface and annotation constants are implicitly `static final` and are not
reported. Static imports are not reported (whether the imported member is a
constant cannot be known without resolving it). Members of Message/Prompt/Label
types are not reported either: those types should not exist in student code
and are reported by ui_text_types.py.

Usage:
  static_usage.py [options] PATH...
"""

import sys

import javaast
from javaast import Finding, Tool
from ui_text_types import NAMES as UI_TEXT_TYPE_NAMES

TOOL = Tool(
    name="static-usage",
    title="Uses of static (constants excepted)",
    description="Detect uses of `static` in Java code, except constants "
                "(static final fields).",
    severity_labels={
        "HIGH": "static non-final field or static method",
        "MEDIUM": "static initializer",
        "LOW": "static nested type",
    },
    explanations={
        "HIGH": (
            "Global mutable state (static non-final fields) or procedural code "
            "that belongs to no object (static methods)."
        ),
        "MEDIUM": "Initialization of class-wide state (static initializers).",
        "LOW": "Nested types declared static.",
    },
    kind_explanations={
        "field": ("static non-final field", (
            "A static field that is not final is global mutable state: a "
            "single variable shared by every instance and by every part of the "
            "program that can reach it. Objects no longer own their state, any "
            "code can change it behind their backs, separate instances (e.g. "
            "two ERP objects, or a loaded and a new one) interfere with each "
            "other, and the value is not saved with the object when it is "
            "serialized. Counters and registries belong in an object (e.g. the "
            "one that creates or manages the instances).")),
        "method": ("static method", (
            "A static method belongs to no object: it cannot use instance "
            "state, cannot be overridden, and is not subject to dynamic "
            "dispatch, so it cannot take part in polymorphism. It is "
            "procedural code inside a class, and callers are hard-wired to "
            "that class. The behaviour usually belongs to one of the objects "
            "involved (often the one whose data it uses).")),
        "initializer": ("static initializer", (
            "A static initializer block runs once, when the class is loaded, "
            "and can only set up class-wide (static) data. If it initializes "
            "constants, a direct initializer on the `static final` field is "
            "clearer; otherwise it is preparing global state, which belongs "
            "in an object (e.g. set up by a constructor).")),
        "nested-class": ("static nested class", (
            "A static nested class is not tied to an instance of the enclosing "
            "class; that is sometimes the right choice, but check that it "
            "should not be a top-level class of its own (or an inner class).")),
        "nested-interface": ("static nested interface", (
            "Nested interfaces are implicitly static: writing `static` is "
            "redundant.")),
        "nested-enum": ("static nested enum", (
            "Nested enums are implicitly static: writing `static` is "
            "redundant.")),
        "nested-record": ("static nested record", (
            "Nested records are implicitly static: writing `static` is "
            "redundant.")),
        "nested-annotation": ("static nested annotation type", (
            "Nested annotation types are implicitly static: writing `static` "
            "is redundant.")),
    },
    advice=(
        "Keep `static` only for constants (`static final`). Move state into "
        "the objects that own it and behaviour into methods of those objects; "
        "use instance methods so subclasses can redefine them."
    ),
    analyze=None,  # set below
    none_found="No issues found: `static` is only used for constants.",
)


def _is_main(node, mods):
    if javaast.text(node.child_by_field_name("name")) != "main":
        return False
    if javaast.text(node.child_by_field_name("type")) != "void":
        return False
    return javaast.signature(node) in ("(String[])", "(String...)",
                                "(java.lang.String[])", "(java.lang.String...)")


def _decl(mods, *parts):
    return " ".join(p for p in (" ".join(javaast.sort_modifiers(mods)),) + parts if p)


def analyze(sf):
    findings = []

    def add(node, severity, symbol, detail, kind, note=""):
        line, col = javaast.position(node)
        findings.append(Finding(sf.display, line, col, severity, symbol,
                                detail, note, {"kind": kind}))

    for node, stack, _in_code in javaast.walk(sf.root):
        t = node.type
        if any(c.name in UI_TEXT_TYPE_NAMES for c in stack):
            continue  # Message/Prompt/Label types: see ui_text_types.py

        if t == "field_declaration":
            mods = javaast.modifiers(node)
            if "static" not in mods or "final" in mods:
                continue
            owner = javaast.type_path(stack)
            ftype = javaast.text(node.child_by_field_name("type"))
            for decl in node.children_by_field_name("declarator"):
                name_node = decl.child_by_field_name("name")
                name = javaast.text(name_node)
                dims = javaast.text(decl.child_by_field_name("dimensions"))
                note = ""
                if name == "serialVersionUID":
                    note = ("serialVersionUID must be `static final` (a "
                            "constant); without `final` it is a variable")
                add(name_node or decl, "HIGH", "%s.%s" % (owner, name),
                    _decl(mods, ftype, name + dims), "field", note)

        elif t == "method_declaration":
            mods = javaast.modifiers(node)
            if "static" not in mods:
                continue
            name_node = node.child_by_field_name("name")
            name = javaast.text(name_node)
            sig = javaast.signature(node)
            rtype = javaast.text(node.child_by_field_name("type"))
            if _is_main(node, mods):
                continue  # the entry point must be static
            add(name_node, "HIGH",
                "%s.%s%s" % (javaast.type_path(stack), name, sig),
                _decl(mods, rtype, name + sig), "method")

        elif t == "static_initializer":
            add(node, "MEDIUM", "%s.<static initializer>" %
                javaast.type_path(stack), "static { ... }", "initializer")

        elif t in javaast.TYPE_DECLARATIONS:
            mods = javaast.modifiers(node)
            if "static" not in mods:
                continue
            kind = javaast.TYPE_DECLARATIONS[t]
            name_node = node.child_by_field_name("name")
            keyword = {"annotation": "@interface"}.get(kind, kind)
            note = ""
            if kind in ("interface", "enum", "record", "annotation"):
                note = "nested %s types are implicitly static; `static` is " \
                       "redundant" % kind
            add(name_node or node, "LOW", javaast.type_path(stack),
                _decl(mods, keyword, javaast.text(name_node)),
                "nested-" + kind, note)

    return findings


TOOL.analyze = analyze

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
