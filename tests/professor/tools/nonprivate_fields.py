#!/usr/bin/env python3
"""Detect non-private, non-static fields (encapsulation violations) in Java code.

Each instance field should be private: it is part of the object's internal
representation. This tool flags every instance (non-static) field that is not
private and explains the problem. Severity depends on how far the field is
exposed:

  HIGH    public           -- any class, anywhere, can read and change it
  MEDIUM  (no modifier)    -- package-private: any class in the same package
  LOW     protected        -- subclasses (any package) and the same package

Static fields are not considered. Interface and annotation constants are
implicitly `public static final` and are not fields in this sense. Record
components are implicitly private.

Usage:
  nonprivate_fields.py [options] PATH...
"""

import sys

import javaast
from javaast import Finding, Tool

SEVERITY_BY_ACCESS = {"public": "HIGH", None: "MEDIUM", "protected": "LOW"}

TOOL = Tool(
    name="nonprivate-fields",
    title="Non-private instance fields",
    description="Detect non-private, non-static fields in Java code.",
    severity_labels={"HIGH": "public", "MEDIUM": "no modifier",
                     "LOW": "protected"},
    explanations={
        "HIGH": (
            "The field is public: any class in the program can read and modify "
            "it directly. The object can no longer guarantee its own "
            "invariants, the internal representation becomes part of the "
            "class's public contract, and changing it later breaks every "
            "client that uses it."
        ),
        "MEDIUM": (
            "The field has no access modifier (package-private): every class in "
            "the same package can read and modify it directly. This is often "
            "an oversight (a forgotten `private`) and couples the classes of "
            "the package to this class's internal representation."
        ),
        "LOW": (
            "The field is protected: subclasses (in any package) and all "
            "classes in the same package can access it directly. Subclasses "
            "become coupled to the superclass's representation and can break "
            "its invariants; prefer a private field with protected (or public) "
            "methods where needed."
        ),
    },
    advice=(
        "Make the field private and, if other classes really need it, provide "
        "methods that express what they need (queries/operations), rather "
        "than plain getters/setters that merely re-expose the representation."
    ),
    analyze=None,  # set below
    none_found="No issues found: all instance fields are private.",
)


def analyze(sf):
    findings = []
    for node, stack, _in_code in javaast.walk(sf.root):
        if node.type != "field_declaration" or not stack:
            continue
        if stack[-1].kind in ("interface", "annotation"):
            continue  # cannot happen in valid Java; be safe with broken code
        mods = javaast.modifiers(node)
        if "static" in mods:
            continue
        acc = javaast.access(mods)
        if acc == "private":
            continue

        ftype = javaast.text(node.child_by_field_name("type"))
        mod_text = " ".join(javaast.sort_modifiers(mods))
        note = javaast.visibility_note(stack)
        if note:
            note += "; still, it should be private"
        owner = javaast.type_path(stack)
        for decl in node.children_by_field_name("declarator"):
            name_node = decl.child_by_field_name("name")
            name = javaast.text(name_node)
            dims = javaast.text(decl.child_by_field_name("dimensions"))
            line, col = javaast.position(name_node or decl)
            findings.append(Finding(
                file=sf.display,
                line=line,
                column=col,
                severity=SEVERITY_BY_ACCESS[acc],
                symbol="%s.%s" % (owner, name),
                detail=" ".join(p for p in (mod_text, ftype, name + dims) if p),
                note=note,
                extra={"access": acc or "package-private",
                       "modifiers": javaast.sort_modifiers(mods),
                       "type": ftype + dims,
                       "annotations": javaast.annotations(node)},
            ))
    return findings


TOOL.analyze = analyze

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
