# Code-quality analysis tools

Python tools that each look at one aspect of a project's Java code, report the
problems they find, and explain why each one is a problem.

Requirements: Python 3 and `pip install tree-sitter tree-sitter-java`.

## Common behaviour

All the tools share `javaast.py`, which handles parsing, file selection,
reports and command-line options.

- **Parser:** [tree-sitter](https://tree-sitter.github.io/) with the Java
  grammar. It understands current Java (records, switch expressions, text
  blocks, patterns, sealed types). Files with syntax errors are still
  analysed; they are listed separately, since results near the errors may be
  incomplete.
- **Skipped files:** the ones that students must not change (those under
  `../sane/`) and po-uilib. po-uilib is recognized by its directory
  (`po-uilib*`, `pt/tecnico/uilib/`) or by its `pt.tecnico.uilib` package,
  wherever it is stored. Use `--no-skip-sane` to include the `sane/` files.
- **Severities:** `HIGH`, `MEDIUM` and `LOW`. The meaning of each is defined
  per tool and explained at the end of the report.
- **Options:**
  - `--format text|json`
  - `--no-explain`: leave out the explanation section.
  - `--min-severity S`: report only findings at `S` or above.
  - `--fail-on S`: exit with status 1 if there is any finding at `S` or above.
  - `--sane-dir DIR`, `--no-skip-sane`

```sh
python tools/<tool>.py checked-out/001            # text report
python tools/<tool>.py --format json checked-out/001
```

Writing a new tool: define a `javaast.Tool` (name, title, severity labels and
explanations, advice) and an `analyze(SourceFile) -> [Finding]` function, then
call `javaast.main(TOOL)`. `javaast.walk()` yields every node together with its
chain of enclosing types.

## Driver: `analyze_projects.py`

Runs all the tools over the projects, in one of two modes:

```sh
python3 tools/analyze_projects.py --single checked-out/001   # one project: text report
python3 tools/analyze_projects.py                            # all NNN dirs in checked-out -> CODE-ANALYSIS.md
python3 tools/analyze_projects.py ../../prj -o /tmp/report.md
python3 tools/analyze_projects.py --only 001 014 -o -        # some projects, to stdout
```

- **`--single DIR`:** a plain-text report for one project. It starts with a
  table of findings per problem (HIGH/MEDIUM/LOW) and the files with syntax
  errors. Then, for each problem found, it gives the findings and the
  explanation.
- **All projects:** a Markdown report with these parts:
  - **Summary:** one row per problem, with the number of projects that have
    it and the total findings per severity.
  - **Projects:** one row per project with findings, and one column per
    problem with its counts.
  - **One section per problem:** the explanation and fix, then the list of
    projects that have it, each with its counts and a collapsible block of
    findings.
  - **Files with syntax errors.**

Options:

- `--link FORMAT`: link each project to a per-project page, e.g.
  `logs/ei/{project}.res.html`.
- `--min-severity S`: drop lower-severity findings.
- `--no-details`: counts only, without the findings.
- `--only NNN...`: analyse only these projects.
- `-q`: no progress output.

Each project's files are parsed only once (`javaast.set_load_cache`).

### In the pipeline

The analysis runs for the `ei` and `ef` deliveries, only on projects that
delivered (not "Bad project"):

- `4-test.sh` appends `--single` output to each group's
  `logs/$DELIVERY/NNN.res.html`, under a `CODE ANALYSIS` banner.
- `5-summarize.sh` writes `CODE-ANALYSIS.md` (linked to the `.res` files) and
  `git add`s it; `6-end.sh` then commits and pushes it.

Both steps are skipped if `python3` cannot import `tree_sitter` and
`tree_sitter_java`.

Because the analysis may contain arbitrary words from student code (method
names, comments), the patterns that `5-summarize.sh` looks for in the `.res`
files are anchored to the start of the line: `^!!! Bad project`,
`^-- successfully completed build` and `^Total: `.

## `nonprivate_fields.py`: non-private instance fields

Flags every non-static field that is not `private`:

| Severity | Access                        | Exposed to                      |
|----------|-------------------------------|---------------------------------|
| HIGH     | `public`                      | every class                     |
| MEDIUM   | no modifier (package-private) | classes in the same package     |
| LOW      | `protected`                   | subclasses and the same package |

Static fields are ignored. Interface and annotation constants are implicitly
`public static final`, and record components are implicitly private, so
neither is reported. For fields in private nested, local, or anonymous
classes, the report adds a note that their real visibility is narrower.

## `static_usage.py`: uses of `static` (constants excepted)

Every explicit `static` is reported, except on `static final` fields
(constants):

| Severity | Construct                                   | Why                                   |
|----------|---------------------------------------------|---------------------------------------|
| HIGH     | static non-final field                      | global mutable state                  |
| HIGH     | static method (except `main`)               | procedural code, no polymorphism      |
| MEDIUM   | static initializer block                    | initialization of class-wide state    |
| LOW      | static nested class/interface/enum/record   | check whether it belongs nested; `static` is redundant on interfaces, enums and records |

The following are not reported:

- `main(String[])` methods, because the entry point has to be static.
- Interface and annotation constants, which are implicitly `static final`.
- Static imports, since whether the imported member is a constant is not
  known without resolving it.

The report explains each kind of finding separately (via
`Tool.kind_explanations`).

Members of `Message`/`Prompt`/`Label` types are not reported here; the types
themselves are reported by `ui_text_types.py`.

## `ui_text_types.py`: student-defined Message/Prompt/Label types

The `Message`, `Prompt` and `Label` interfaces hold user-facing texts and
belong to the provided framework. Students should not define their own. Any
type with one of these names (or the plural forms), top-level or nested, that
is not one of the provided files is reported:

| Severity | Where                                  | Why                                          |
|----------|----------------------------------------|----------------------------------------------|
| HIGH     | core (package not under `erp.app`)     | presentation text inside the domain layer    |
| MEDIUM   | app, outside the provided files        | copy of the framework mechanism; not removed by `2-clean.sh` |

The layer is taken from the `package` declaration, or from the path
(`erp-app/`) if the file has none. The provided files are skipped, so every
finding survives the cleaning done before testing.

## `empty_catch.py`: empty catch blocks

Reports every `catch` block that has no statements (an empty statement `;`
counts as nothing):

| Severity | Case                                                         |
|----------|--------------------------------------------------------------|
| HIGH     | no comments and an ordinary parameter name: silently swallowed |
| HIGH     | catches `Exception`, `Throwable`, `RuntimeException` or `Error`, whatever the comments or parameter name: also hides programming errors |
| MEDIUM   | only comments, or a parameter named `ignored`, `ignore`, `expected`, `unused` or `_`: noticed, but still swallowed |

A note is added when the comments contain FIXME, TODO or XXX, which marks
unfinished code. Catching broad types is also reported by
`broad_exceptions.py`, for non-empty blocks too.

Each finding names the enclosing member (method, constructor, initializer or
field), e.g. `Erp.registerFromFields(String[])`.

## `broad_exceptions.py`: broad exception types caught, declared, or thrown

Reports, all with HIGH severity, every use of `Exception`, `Throwable`,
`RuntimeException` or `Error` (with or without the `java.lang.` prefix) in:

- a `catch` clause, including multi-catch, whatever the block does;
- a `throws` clause of a method or constructor, including interface methods.
- a `throw new T(...)` statement, including anonymous subclasses.
  Rethrowing a variable (`throw e`) needs type resolution and is not checked.

Declaring `RuntimeException` or `Error` also gets a note, since they are
unchecked and never need to be declared. The report explains catching,
declaring and throwing separately.

## `base_types.py`: not programming to base types

| Severity | Kind                | What                                                                 |
|----------|---------------------|----------------------------------------------------------------------|
| HIGH     | type test           | `instanceof` (including patterns), type patterns in `switch`, `getClass()` comparisons (`==`, `!=`, `.equals`) |
| HIGH     | downcast            | casts to reference types                                             |
| HIGH     | Object type         | `Object` (or `Object[]`) as the declared type of a field, parameter, return value, local or `for` variable, or type argument (`List<Object>`). `Object o = in.readObject();` is included, with a note to cast directly. Exempt: the parameter of `equals(Object)`, and the return type of `readResolve`, `writeReplace` and `clone` |
| MEDIUM   | class name          | `getClass().getSimpleName()` (or `getName()`, `getTypeName()`, `getCanonicalName()`), e.g. to print a type's name; noted when inside `toString()`. HIGH when the name is compared (`==`, `equals`, `switch`), since that is a type test |
| MEDIUM   | concrete collection | `ArrayList`, `HashMap`, `TreeMap`, `HashSet`, ... as the declared type of a field, parameter, return value, local variable, or type argument (not in `new ...`) |
| LOW      | project type        | a field, parameter or local variable declared as project class `C`, although every method called on it is available in a project supertype of `C` |

Not reported:

- Type tests and casts inside `equals(Object)`.
- Casts and `instanceof` tests of `readObject()` results (deserialization),
  directly or through a variable that holds the result. The `Object`
  variable itself is reported as an Object type, so the problem appears
  once.
- Primitive casts.
- Collection names that the project defines itself.

The project-type check is a heuristic. It builds the project's class
hierarchy from every file given, including the skipped canonical ones, and
matches methods by name only. It stays silent when unsure:

- if the variable is used in any way other than method calls, assignments
  and null comparisons (e.g. passed as an argument or returned);
- if no method is called on it, or only `Object` methods are (`getClass`,
  `toString`, ...);
- for parameters of methods that override or implement a supertype method,
  since their types are fixed by the supertype.

The suggestion lists every suitable base type, nearest first.

This tool uses `Tool.prepare`, a whole-project step that runs before the
per-file analysis.

## `raw_types.py`: raw types (generic types without type arguments)

| Severity | Where                                                                  |
|----------|------------------------------------------------------------------------|
| HIGH     | declarations (field, parameter, return type, local variable, type argument, type bound, wildcard bound) and supertypes (`implements Comparable`, `extends Command`) |
| MEDIUM   | object creation (`new ArrayList()`; noted when declared with `var`) and casts |

The diamond (`new ArrayList<>()`) is fine. The following are not reported,
because Java requires or allows the raw form there: `List.class`,
`instanceof`, `ArrayList::new`, and `new List[n]`.

Generic types are recognized from two sources:

- a list of common JDK types and the po-uilib types `Command`, `Field` and
  `FieldParsed` (`2-clean.sh` removes the po-uilib sources);
- the project's own generic declarations.

A project type with the same name as a listed one takes precedence, e.g. a
project class `Stack` that is not generic.

The class hierarchy shared with `base_types.py` lives in `javaast.py`
(`Hierarchy`, `build_hierarchy`).

## `duplicate_fields.py`: fields repeated along the class hierarchy

Only instance fields of project classes and `extends` relationships are
considered:

| Severity | Case                                                                 |
|----------|----------------------------------------------------------------------|
| HIGH     | a subclass declares a field that a superclass (at any level) already has: same name (hiding), or a name differing only in underscores/case (`_name` vs `name`). This applies even if the superclass field is private, since the object then holds two copies of the data |
| MEDIUM   | all direct subclasses (at least 2) of a class declare the same field (name and type, ignoring underscores, case and package qualifiers) that the superclass lacks: move it up |
| LOW      | the same, but only some (at least 2) of the direct subclasses        |

Notes say whether the names are identical (hiding) or only similar, whether
the superclass field is private, and whether the types differ. Static fields
are not considered; they are covered by `static_usage.py`.

`javaast.Hierarchy` records each type's `extends` superclass and fields for
this purpose (`superclass_chain`, `direct_subclasses`, `FieldInfo`).
