#!/usr/bin/env python3
"""Detect empty catch blocks in Java code.

A catch block without statements silently swallows the exception: the
situation it signals is neither handled nor reported. Severity depends on
whether the code shows any sign that ignoring the exception was deliberate:

  HIGH    no statements, no comments, ordinary parameter name
          -- the exception silently disappears
          or the caught type is very broad (Exception, Throwable,
          RuntimeException, Error), whatever the comments
          -- this also silently hides programming errors
  MEDIUM  only comments, or a parameter named to signal intent
          (ignored, ignore, expected, unused, _)
          -- acknowledged, but the exception is still swallowed

A note is added when the comments are FIXME/TODO markers (unfinished code).
Catching broad types is also reported (for non-empty blocks too) by
broad_exceptions.py.

Usage:
  empty_catch.py [options] PATH...
"""

import re
import sys

import javaast
from javaast import Finding, Tool

INTENT_NAMES = {"ignored", "ignore", "expected", "unused", "_"}
TODO_RE = re.compile(r"\b(FIXME|TODO|XXX)\b", re.IGNORECASE)

TOOL = Tool(
    name="empty-catch",
    title="Empty catch blocks",
    description="Detect empty catch blocks in Java code.",
    severity_labels={
        "HIGH": "silently empty, or empty and catching a broad type",
        "MEDIUM": "empty, but commented or marked as ignored",
        "LOW": "-",
    },
    explanations={
        "HIGH": (
            "The catch block is empty: the exception is caught and silently "
            "discarded. The situation it signals (an unknown key, a missing "
            "file, a failed operation) is neither handled nor reported, so "
            "the program continues as if nothing had happened, often in an "
            "inconsistent state, and the cause of any later failure becomes "
            "very hard to find. When the caught type is Exception, Throwable, "
            "RuntimeException or Error, programming errors (e.g. "
            "NullPointerException, ClassCastException) are silently hidden too, "
            "whatever the comment says."
        ),
        "MEDIUM": (
            "The catch block has no statements, although a comment or the "
            "parameter name shows that ignoring the exception was noticed. The "
            "exception is still swallowed: if it really cannot happen, the "
            "code is probably structured wrongly (e.g. the check should be "
            "made elsewhere, or the exception should not be declared); if it "
            "can happen, it must be handled or propagated."
        ),
        "LOW": "-",
    },
    advice=(
        "Handle the exception (do what the situation requires), or let it "
        "propagate: declare it in `throws`, or wrap it in an exception that "
        "makes sense at the current level (e.g. in the app, translate a core "
        "exception into the corresponding provided app exception). Catch only "
        "the specific exception types that can actually occur."
    ),
    analyze=None,  # set below
    none_found="No issues found: no empty catch blocks.",
)


def analyze(sf):
    findings = []
    for node, stack, _in_code in javaast.walk(sf.root):
        if node.type != "catch_clause":
            continue
        body = node.child_by_field_name("body")
        if body is None:
            continue
        comments = [c for c in body.named_children
                    if c.type in ("line_comment", "block_comment")]
        if len(comments) != len(body.named_children):
            continue  # has statements

        param = next((c for c in node.named_children
                      if c.type == "catch_formal_parameter"), None)
        ctype = next((c for c in param.named_children
                      if c.type == "catch_type"), None) if param else None
        types = [javaast.text(t) for t in ctype.named_children] if ctype else []
        pname = javaast.text(param.child_by_field_name("name")) if param else ""

        intent = bool(comments) or pname in INTENT_NAMES
        broad = [t for t in types if javaast.is_broad_exception(t)]
        severity = "HIGH" if broad or not intent else "MEDIUM"

        notes = []
        if broad:
            notes.append("catches %s: this also hides programming errors "
                         "(e.g. NullPointerException)" % ", ".join(broad))
        if any(TODO_RE.search(javaast.text(c)) for c in comments):
            notes.append("contains a FIXME/TODO comment: unfinished code")

        comment_text = " ".join(javaast.text(c) for c in comments)
        if len(comment_text) > 60:
            comment_text = comment_text[:57] + "..."
        detail = "catch (%s %s) { %s}" % (" | ".join(types) or "?", pname,
                                          comment_text + " " if comment_text else "")

        line, col = javaast.position(node)
        findings.append(Finding(
            sf.display, line, col, severity,
            javaast.member_path(node, stack), detail, "; ".join(notes),
            {"types": types, "parameter": pname,
             "comments": [javaast.text(c) for c in comments]},
        ))
    return findings


TOOL.analyze = analyze

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
