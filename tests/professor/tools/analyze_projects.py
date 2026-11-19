#!/usr/bin/env python3
"""Run all the code-quality tools over group projects.

Two modes:

  * all projects (default): analyse every NNN subdirectory of ROOT and write
    a Markdown report with one section per problem, each listing the
    projects that have it;
  * one project (--single DIR): print a plain-text report for that project
    (all tools), e.g. to append to its results file.

Files that students must not change (sane/) and po-uilib are skipped, as in
the individual tools.

Usage:
  analyze_projects.py [options] [ROOT]
  analyze_projects.py --single DIR

  ROOT defaults to `checked-out`; the report defaults to CODE-ANALYSIS.md.
"""

import argparse
import datetime
import os
import re
import sys

import javaast
import base_types
import broad_exceptions
import duplicate_fields
import empty_catch
import nonprivate_fields
import raw_types
import static_usage
import ui_text_types

# report order
TOOLS = [
    nonprivate_fields.TOOL,
    static_usage.TOOL,
    ui_text_types.TOOL,
    empty_catch.TOOL,
    broad_exceptions.TOOL,
    base_types.TOOL,
    raw_types.TOOL,
    duplicate_fields.TOOL,
]

PROJECT_RE = re.compile(r"^\d{3}$")


def find_projects(root, only=None):
    names = sorted(n for n in os.listdir(root)
                   if PROJECT_RE.match(n) and os.path.isdir(os.path.join(root, n)))
    if only:
        names = [n for n in names if n in only]
    return names


def analyse(root, projects, sane_suffixes, min_rank, progress=True):
    """results[tool.name][project] = Report; syntax[project] = [(file, errs)]"""
    results = {t.name: {} for t in TOOLS}
    syntax = {}
    for i, p in enumerate(projects, 1):
        if progress:
            print("\r[%d/%d] %s" % (i, len(projects), p), end="",
                  file=sys.stderr, flush=True)
        path = os.path.join(root, p)
        javaast.set_load_cache(True)
        for tool in TOOLS:
            r = javaast.run_analysis(tool, [path], sane_suffixes, base=path)
            r.findings = [f for f in r.findings
                          if javaast.SEVERITY_RANK[f.severity] <= min_rank]
            results[tool.name][p] = r
            if r.syntax_errors:
                syntax[p] = r.syntax_errors
        javaast.set_load_cache(False)
    if progress:
        print(file=sys.stderr)
    return results, syntax


# ---------------------------------------------------------------------------
# Markdown


def _counts_text(c):
    parts = ["%d %s" % (c[s], s) for s in javaast.SEVERITIES if c[s]]
    return ", ".join(parts) if parts else "0"


def _compact(c):
    return "/".join(str(c[s]) for s in javaast.SEVERITIES)


def _anchor(title):
    a = re.sub(r"[^\w\- ]", "", title.lower()).strip().replace(" ", "-")
    return re.sub(r"-+", "-", a)


def _md_escape(s):
    return s.replace("|", "\\|")


def _prose(s):
    """Escape < and > outside `code spans`, so that e.g. List<Product> is not
    taken as an HTML tag by Markdown renderers."""
    parts = s.split("`")
    for i in range(0, len(parts), 2):
        parts[i] = parts[i].replace("<", "&lt;").replace(">", "&gt;")
    return "`".join(parts)


def _finding_line(f):
    s = "%s:%d:%d [%s] %s  (%s)" % (f.file, f.line, f.column, f.severity,
                                    f.symbol, f.detail)
    if f.note:
        s += "\n    note: " + f.note
    return s


def _kinds_line(tool, report):
    if not tool.kind_explanations:
        return ""
    counts = {}
    for f in report.findings:
        k = f.extra.get("kind")
        if k in tool.kind_explanations:
            counts[k] = counts.get(k, 0) + 1
    if not counts:
        return ""
    order = list(tool.kind_explanations)
    return " (" + ", ".join("%s: %d" % (tool.kind_explanations[k][0], counts[k])
                            for k in sorted(counts, key=order.index)) + ")"


def render_single(path, sane_suffixes):
    """Plain-text report of all tools for one project directory."""
    javaast.set_load_cache(True)
    reports = [(t, javaast.run_analysis(t, [path], sane_suffixes, base=path))
               for t in TOOLS]
    javaast.set_load_cache(False)
    out = []
    width = max(len(t.title) for t in TOOLS) + 3
    out.append("Findings per problem (HIGH/MEDIUM/LOW):")
    out.append("")
    for t, r in reports:
        out.append("  %s %s" % ((t.title + " ").ljust(width, "."), _compact(r.counts())))
    out.append("")
    syntax = reports[0][1].syntax_errors
    if syntax:
        out.append("Files with syntax errors (analysed as far as possible; "
                   "findings near the errors may be incomplete):")
        for name, errs in syntax:
            out.append("  %s: line %s" % (name, ", ".join(
                str(l) for l, _ in errs[:5]) + (", ..." if len(errs) > 5 else "")))
        out.append("")
    for t, r in reports:
        if not r.findings:
            continue
        out.append("---------- %s ----------" % t.title)
        out.append("")
        out.append(javaast.render_text(t, r, explain=True, stats=False))
        out.append("")
    if not any(r.findings for _, r in reports):
        out.append("No issues found.")
    return "\n".join(out)


def render(root, projects, results, syntax, details=True, link=None):
    out = []
    now = datetime.datetime.now().strftime("%Y-%m-%d %H:%M")
    out.append("# Code analysis report")
    out.append("")
    out.append("Generated on %s from `%s`: %d projects analysed with %d tools."
               % (now, root, len(projects), len(TOOLS)))
    out.append("")
    out.append("Each section below describes one problem and lists the projects "
               "where it was found, with the number of findings per severity "
               "(HIGH, MEDIUM, LOW). Expand a project to see its findings "
               "(`file:line:column [severity] location (detail)`). Files that "
               "students must not change are not analysed.")
    out.append("")

    # -- contents and summary
    out.append("## Summary")
    out.append("")
    out.append("| Problem | Projects | HIGH | MEDIUM | LOW |")
    out.append("|---|---:|---:|---:|---:|")
    for tool in TOOLS:
        reps = results[tool.name]
        affected = [p for p in projects if reps[p].findings]
        tot = {s: sum(reps[p].counts()[s] for p in projects)
               for s in javaast.SEVERITIES}
        out.append("| [%s](#%s) | %d | %d | %d | %d |" % (
            _md_escape(tool.title), _anchor(tool.title), len(affected),
            tot["HIGH"], tot["MEDIUM"], tot["LOW"]))
    out.append("")

    # -- per-project overview
    flagged = [p for p in projects
               if any(results[t.name][p].findings for t in TOOLS)]
    out.append("## Projects")
    out.append("")
    if flagged:
        out.append("Findings per project and problem (HIGH/MEDIUM/LOW); "
                   "projects without findings are omitted (%d of %d)."
                   % (len(projects) - len(flagged), len(projects)))
        out.append("")
        header = "| Project | " + " | ".join(
            "[%d](#%s)" % (i, _anchor(t.title)) for i, t in enumerate(TOOLS, 1)) + " |"
        out.append(header)
        out.append("|---|" + "---:|" * len(TOOLS))
        for p in flagged:
            cells = []
            for t in TOOLS:
                c = results[t.name][p].counts()
                cells.append(_compact(c) if any(c.values()) else "")
            name = "[%s](%s)" % (p, link.format(project=p)) if link else p
            out.append("| %s | %s |" % (name, " | ".join(cells)))
        out.append("")
        out.append("Columns: " + "; ".join(
            "%d. %s" % (i, t.title) for i, t in enumerate(TOOLS, 1)) + ".")
    else:
        out.append("No findings in any project.")
    out.append("")

    # -- one section per problem
    for tool in TOOLS:
        reps = results[tool.name]
        affected = [p for p in projects if reps[p].findings]
        out.append("## %s" % tool.title)
        out.append("")
        out.append(_prose(tool.description))
        out.append("")
        if tool.kind_explanations:
            for k, (label, expl) in tool.kind_explanations.items():
                out.append("* **%s**: %s" % (label, _prose(expl)))
        else:
            for s in javaast.SEVERITIES:
                if tool.explanations.get(s, "-") != "-":
                    out.append("* **%s** (%s): %s" % (
                        s, _prose(tool.severity_labels[s]),
                        _prose(tool.explanations[s])))
        out.append("")
        out.append("**Fix:** " + _prose(tool.advice))
        out.append("")
        if not affected:
            out.append("_No project has this problem._")
            out.append("")
            continue
        out.append("%d project(s):" % len(affected))
        out.append("")
        for p in affected:
            r = reps[p]
            name = "[%s](%s)" % (p, link.format(project=p)) if link else p
            line = "* **%s**: %s%s" % (name, _counts_text(r.counts()),
                                       _kinds_line(tool, r))
            out.append(line)
            if details:
                out.append("  <details><summary>findings</summary>")
                out.append("")
                out.append("  ```")
                for f in r.findings:
                    for l in _finding_line(f).split("\n"):
                        out.append("  " + l)
                out.append("  ```")
                out.append("")
                out.append("  </details>")
        out.append("")

    # -- syntax errors
    out.append("## Files with syntax errors")
    out.append("")
    if syntax:
        out.append("These files were analysed as far as possible; findings near "
                   "the errors may be incomplete.")
        out.append("")
        for p in projects:
            if p in syntax:
                files = ", ".join("`%s` (line %s)" % (
                    f, ", ".join(str(l) for l, _ in errs[:3]) +
                    (", ..." if len(errs) > 3 else ""))
                    for f, errs in syntax[p])
                out.append("* **%s**: %s" % (p, files))
    else:
        out.append("_None._")
    out.append("")
    return "\n".join(out)


def main(argv=None):
    ap = argparse.ArgumentParser(
        description="Run all code-quality tools over the group projects and "
                    "write a Markdown report.")
    ap.add_argument("root", nargs="?", default="checked-out",
                    help="directory containing the NNN project directories "
                         "(default: %(default)s)")
    ap.add_argument("--single", metavar="DIR",
                    help="analyse only this project directory and print a "
                         "plain-text report (all tools)")
    ap.add_argument("--link", metavar="FORMAT",
                    help="link each project to a per-project page; FORMAT "
                         "contains {project}, e.g. logs/ei/{project}.res.html")
    ap.add_argument("-o", "--output", default="CODE-ANALYSIS.md",
                    help="report file (default: %(default)s; '-' for stdout)")
    ap.add_argument("--only", nargs="+", metavar="NNN",
                    help="analyse only these projects")
    ap.add_argument("--min-severity", choices=javaast.SEVERITIES, default="LOW",
                    help="report only this severity or higher")
    ap.add_argument("--no-details", action="store_true",
                    help="list projects and counts only, without findings")
    ap.add_argument("--sane-dir", default=javaast.DEFAULT_SANE_DIR,
                    help="directory with canonical files to skip "
                         "(default: %(default)s)")
    ap.add_argument("-q", "--quiet", action="store_true",
                    help="no progress output")
    args = ap.parse_args(argv)

    if args.single:
        if not os.path.isdir(args.single):
            ap.error("no such directory: %s" % args.single)
        print(render_single(args.single,
                            javaast.load_sane_suffixes(args.sane_dir)))
        return 0

    if not os.path.isdir(args.root):
        ap.error("no such directory: %s" % args.root)
    projects = find_projects(args.root, set(args.only) if args.only else None)
    if not projects:
        ap.error("no NNN project directories in %s" % args.root)

    sane = javaast.load_sane_suffixes(args.sane_dir)
    results, syntax = analyse(args.root, projects, sane,
                              javaast.SEVERITY_RANK[args.min_severity],
                              progress=not args.quiet)
    md = render(args.root, projects, results, syntax,
                details=not args.no_details, link=args.link)
    if args.output == "-":
        print(md)
    else:
        with open(args.output, "w", encoding="utf-8") as f:
            f.write(md + "\n")
        if not args.quiet:
            print("Report written to %s" % args.output, file=sys.stderr)
    return 0


if __name__ == "__main__":
    sys.exit(main())
