#!/usr/bin/env python3
"""Detect student-defined Message/Prompt/Label types in Java code.

The `Message`, `Prompt`, and `Label` interfaces (user-facing texts) are part
of the provided application framework: their canonical versions (in `sane/`)
replace the students' copies before testing. Students should not define their
own. Any type (top-level or nested; class, interface, enum, record) with one
of these names (or their plurals) outside the provided files is reported:

  HIGH    in the core (package not under erp.app)
          -- presentation text inside the domain layer
  MEDIUM  in the app, but not one of the provided files
          -- a copy of the framework's mechanism outside its place

The provided files are skipped (they are replaced by the canonical versions),
so everything reported survives the cleaning done before testing.

Usage:
  ui_text_types.py [options] PATH...
"""

import os
import sys

import javaast
from javaast import Finding, Tool

APP_PACKAGE = "erp.app"
NAMES = {"Message", "Messages", "Prompt", "Prompts", "Label", "Labels"}

TOOL = Tool(
    name="ui-text-types",
    title="Student-defined Message/Prompt/Label types",
    description="Detect Message/Prompt/Label types defined by students "
                "(outside the provided framework files).",
    severity_labels={
        "HIGH": "in the core",
        "MEDIUM": "in the app (not a provided file)",
        "LOW": "-",
    },
    explanations={
        "HIGH": (
            "The core (domain) defines a Message/Prompt/Label type, i.e. texts "
            "meant for the user. The core must not know how things are shown "
            "or worded: that is the job of the application (user interface) "
            "layer. The core should report situations with exceptions (whose "
            "contents are data, e.g. the unknown key), and return data or "
            "objects; the app turns them into messages using the provided "
            "Message/Prompt/Label interfaces."
        ),
        "MEDIUM": (
            "The application defines its own Message/Prompt/Label type outside "
            "the provided files. These interfaces are part of the provided "
            "framework: their canonical versions replace the students' copies "
            "before testing, so texts must come from the provided ones (and "
            "the provided files must not be changed). A separate copy "
            "duplicates the mechanism and its texts are not the expected ones."
        ),
        "LOW": "-",
    },
    advice=(
        "Remove the type. In the core, signal problems with (domain) "
        "exceptions carrying the relevant data and let the app produce the "
        "texts; in the app, use the provided Message/Prompt/Label interfaces."
    ),
    analyze=None,  # set below
    none_found="No issues found: no student-defined Message/Prompt/Label types.",
)


def _package(root):
    for c in root.named_children:
        if c.type == "package_declaration":
            for n in c.named_children:
                if n.type in ("scoped_identifier", "identifier"):
                    return javaast.text(n)
    return None


def _in_app(sf, package):
    if package is not None:
        return package == APP_PACKAGE or package.startswith(APP_PACKAGE + ".")
    norm = os.path.abspath(sf.path).replace(os.sep, "/")
    return "/erp-app/" in norm


def analyze(sf):
    findings = []
    package = _package(sf.root)
    in_app = _in_app(sf, package)
    for node, stack, _in_code in javaast.walk(sf.root):
        kind = javaast.TYPE_DECLARATIONS.get(node.type)
        if kind is None:
            continue
        name_node = node.child_by_field_name("name")
        name = javaast.text(name_node)
        if name not in NAMES:
            continue
        mods = javaast.modifiers(node)
        keyword = {"annotation": "@interface"}.get(kind, kind)
        detail = " ".join(p for p in (" ".join(javaast.sort_modifiers(mods)),
                                      keyword, name) if p)
        symbol = javaast.type_path(stack)
        if package:
            symbol = package + "." + symbol
        line, col = javaast.position(name_node or node)
        findings.append(Finding(
            sf.display, line, col, "MEDIUM" if in_app else "HIGH",
            symbol, detail,
            "nested type" if len(stack) > 1 else "",
            {"kind": kind, "layer": "app" if in_app else "core",
             "package": package},
        ))
    return findings


TOOL.analyze = analyze

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
