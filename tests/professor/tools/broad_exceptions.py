#!/usr/bin/env python3
"""Detect catching or declaring overly broad exception types in Java code.

Exception, Throwable, RuntimeException and Error (with or without the
java.lang. prefix) say nothing about what went wrong. Reported, all with
HIGH severity:

  * catch clauses that catch one of them (including in multi-catch),
    whatever the catch block does;
  * throws clauses (methods and constructors) that declare one of them;
  * throw statements that create one of them (`throw new Exception(...)`).

Only `throw new T(...)` is checked: rethrowing a variable (`throw e`) needs
type resolution and is not reported.

Empty catch blocks are reported (also) by empty_catch.py.

Usage:
  broad_exceptions.py [options] PATH...
"""

import sys

import javaast
from javaast import Finding, Tool

TOOL = Tool(
    name="broad-exceptions",
    title="Broad exception types caught, declared, or thrown",
    description="Detect catch clauses, throws declarations, and throw "
                "statements that use Exception, Throwable, RuntimeException, "
                "or Error.",
    severity_labels={
        "HIGH": "broad exception type",
        "MEDIUM": "-",
        "LOW": "-",
    },
    explanations={
        "HIGH": (
            "Exception, Throwable, RuntimeException and Error are too general "
            "to be caught, declared, or thrown."
        ),
        "MEDIUM": "-",
        "LOW": "-",
    },
    kind_explanations={
        "catch": ("catching a broad type", (
            "Catching Exception, Throwable, RuntimeException or Error catches "
            "much more than the situations the code was written for: "
            "programming errors (NullPointerException, ClassCastException, "
            "IndexOutOfBoundsException, ...) and, for Throwable/Error, even "
            "failures of the virtual machine (e.g. OutOfMemoryError) end up "
            "in the same handler and are treated as if they were the expected "
            "problem, hiding bugs. The handler also cannot know what actually "
            "happened, so it cannot react appropriately.")),
        "throws": ("declaring a broad type", (
            "Declaring `throws Exception` (or Throwable, RuntimeException, "
            "Error) tells the caller nothing about what can go wrong, so the "
            "caller cannot handle specific situations and is pushed into "
            "catching Exception in turn. The method's contract should list the "
            "specific (preferably domain) exceptions it can throw. Declaring "
            "RuntimeException or Error is also meaningless: they are "
            "unchecked and never need to be declared.")),
        "throw": ("throwing a broad type", (
            "Throwing a new Exception, Throwable, RuntimeException or Error "
            "signals a problem without saying which: callers can only "
            "distinguish situations by parsing the message text, cannot catch "
            "this case without also catching everything else, and the "
            "information about what went wrong (e.g. the offending key) is "
            "reduced to a string. Each situation deserves its own exception "
            "class.")),
    },
    advice=(
        "Throw, declare, and catch only specific exceptions. In the core, "
        "define domain exceptions that describe each situation (with the "
        "relevant data, e.g. the unknown key), throw and declare those; in "
        "the app, catch them and translate them into the provided app "
        "exceptions."
    ),
    analyze=None,  # set below
    none_found="No issues found: no broad exception types caught, declared, "
               "or thrown.",
)


def analyze(sf):
    findings = []
    for node, stack, _in_code in javaast.walk(sf.root):
        if node.type == "catch_clause":
            param = next((c for c in node.named_children
                          if c.type == "catch_formal_parameter"), None)
            ctype = next((c for c in param.named_children
                          if c.type == "catch_type"), None) if param else None
            types = [javaast.text(t) for t in ctype.named_children] if ctype else []
            broad = [t for t in types if javaast.is_broad_exception(t)]
            if not broad:
                continue
            pname = javaast.text(param.child_by_field_name("name"))
            line, col = javaast.position(ctype)
            findings.append(Finding(
                sf.display, line, col, "HIGH",
                javaast.member_path(node, stack),
                "catch (%s %s)" % (" | ".join(types), pname), "",
                {"kind": "catch", "types": types, "broad": broad},
            ))

        elif node.type == "throws":
            types = [javaast.text(t) for t in node.named_children]
            broad = [t for t in types if javaast.is_broad_exception(t)]
            if not broad:
                continue
            method = node.parent
            if method.type == "constructor_declaration":
                name = javaast.text(method.child_by_field_name("name"))
            else:
                name = "%s %s" % (javaast.text(method.child_by_field_name("type")),
                                  javaast.text(method.child_by_field_name("name")))
            line, col = javaast.position(node)
            note = ""
            if any(t.split(".")[-1] in ("RuntimeException", "Error") for t in broad):
                note = "RuntimeException and Error are unchecked: declaring them is pointless"
            findings.append(Finding(
                sf.display, line, col, "HIGH",
                javaast.member_path(node, stack),
                "%s%s throws %s" % (name, javaast.signature(method),
                                    ", ".join(types)),
                note,
                {"kind": "throws", "types": types, "broad": broad},
            ))

        elif node.type == "throw_statement":
            expr = next((c for c in node.named_children
                         if c.type not in ("line_comment", "block_comment")), None)
            if expr is None or expr.type != "object_creation_expression":
                continue
            tnode = expr.child_by_field_name("type")
            tname = javaast.text(tnode)
            if not javaast.is_broad_exception(tname):
                continue
            detail = javaast.text(node)
            if len(detail) > 80:
                detail = detail[:77] + "..."
            line, col = javaast.position(node)
            note = ""
            if any(c.type == "class_body" for c in expr.children):
                note = "anonymous subclass of %s: still not a meaningful type" % tname
            findings.append(Finding(
                sf.display, line, col, "HIGH",
                javaast.member_path(node, stack), detail, note,
                {"kind": "throw", "types": [tname], "broad": [tname]},
            ))
    return findings


TOOL.analyze = analyze

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
