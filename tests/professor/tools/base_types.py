#!/usr/bin/env python3
"""Detect code that does not program to base types (Java).

Objects should be used through the most general type that offers what the
code needs, and type-dependent behaviour should come from polymorphism, not
from testing types. Reported:

  HIGH    type tests: `instanceof` (including patterns), type patterns in
          `switch`, `getClass()` comparisons
  HIGH    downcasts: casts to reference types
  HIGH    Object as a declared type: fields, parameters, return types, local
          variables, for variables, type arguments (List<Object>), including
          Object[]; except where Java's signatures require it (the parameter
          of equals(Object); the return type of readResolve, writeReplace,
          clone)
  MEDIUM  class names used as data: getClass().getSimpleName() (or getName(),
          getTypeName(), getCanonicalName()), e.g. to print a type's name
          (HIGH when the name is compared: a type test)
  MEDIUM  concrete collection types (ArrayList, HashMap, TreeMap, ...) used
          as declared types (fields, parameters, return types, local
          variables, type arguments) instead of their interfaces
  LOW     project types used where a project base type suffices: a field,
          parameter, or local variable declared with a project class C,
          although every method called on it is available in a project
          supertype of C (heuristic; see below)

Exceptions: type tests and casts inside `equals(Object)` are allowed, and so
are casts and instanceof tests of `readObject()` results (deserialization),
directly or through a local variable holding the result.

The LOW check needs the project's class hierarchy, which is built from all
the files given (including skipped canonical files). Method availability is
decided by name only. A variable is only reported when all of its uses are
understood (method calls on it, assignments to it, null comparisons); if it
is passed as an argument, returned, etc., it is not reported, since the
specific type may be needed there.

Usage:
  base_types.py [options] PATH...
"""

import sys

import javaast
from javaast import Finding, Tool

# concrete collection type -> suggested declared type(s)
CONCRETE_COLLECTIONS = {
    "ArrayList": "List",
    "LinkedList": "List (or Deque/Queue)",
    "Vector": "List",
    "Stack": "Deque (or List)",
    "CopyOnWriteArrayList": "List",
    "HashMap": "Map",
    "LinkedHashMap": "Map",
    "TreeMap": "Map (or SortedMap/NavigableMap if ordered operations are used)",
    "Hashtable": "Map",
    "EnumMap": "Map",
    "IdentityHashMap": "Map",
    "WeakHashMap": "Map",
    "ConcurrentHashMap": "Map (or ConcurrentMap)",
    "HashSet": "Set",
    "LinkedHashSet": "Set",
    "TreeSet": "Set (or SortedSet/NavigableSet if ordered operations are used)",
    "EnumSet": "Set",
    "ArrayDeque": "Deque (or Queue)",
    "PriorityQueue": "Queue",
}

OBJECT_METHODS = {"toString", "equals", "hashCode", "getClass", "notify",
                  "notifyAll", "wait", "clone", "finalize"}

CLASS_NAME_METHODS = {"getSimpleName", "getName", "getTypeName",
                      "getCanonicalName"}

PRIMITIVE_TYPES = {"integral_type", "floating_point_type", "boolean_type"}

TOOL = Tool(
    name="base-types",
    title="Not programming to base types",
    description="Detect type tests, downcasts, concrete collection types, and "
                "project types used where a base type suffices.",
    severity_labels={
        "HIGH": "type test (including class-name comparison) or downcast",
        "MEDIUM": "class name as data, or concrete collection type",
        "LOW": "project type where a base type suffices",
    },
    explanations={
        "HIGH": "Type tests and downcasts instead of polymorphism.",
        "MEDIUM": ("Class names used as data, or concrete collection types "
                   "instead of their interfaces."),
        "LOW": "Specific project types where a base type suffices.",
    },
    kind_explanations={
        "type-test": ("type test", (
            "Testing the type of an object (instanceof, a type pattern in a "
            "switch, comparing getClass()) to decide what to do replaces "
            "polymorphism with a hand-written dispatch: each new subtype "
            "requires finding and changing every such test, and the "
            "behaviour of a type is scattered over the code that tests it "
            "instead of being in the type itself. Declare the operation in "
            "the base type and redefine it in each subtype (or use a "
            "double-dispatch/visitor scheme). Only equals(Object) "
            "legitimately needs to test the type of its argument.")),
        "downcast": ("downcast", (
            "Casting a reference to a more specific type bypasses the type "
            "system: the compiler can no longer check it and the program fails "
            "at run time (ClassCastException) if the object is not of that "
            "type. A downcast means the code needs something the declared "
            "type does not offer: either the operation belongs in the base "
            "type (and is redefined in the subtypes), or the variable should "
            "have been declared with the more specific type in the first "
            "place (e.g. with proper generic types).")),
        "class-name": ("class name as data", (
            "Using the name of an object's class (getClass().getSimpleName(), "
            "getName(), ...) to produce output or to make decisions is a type "
            "test in disguise: the output depends on how the Java classes "
            "happen to be named, which is an implementation detail (renaming "
            "or restructuring the classes changes what the program prints), "
            "and comparing class names has all the problems of instanceof. "
            "Each type should provide what is needed through a method declared "
            "in the base type and redefined in each subtype (e.g. a method "
            "returning the type's label, as defined by the specification).")),
        "object-type": ("Object as a declared type", (
            "Declaring a field, parameter, return type, variable or type "
            "argument as Object throws away all knowledge of what it holds: "
            "nothing useful can be done with it without testing its type and "
            "casting it, and the compiler cannot check anything. Declare the "
            "type that the value actually has (e.g. assign the result of "
            "readObject() directly with a cast to the expected type, instead "
            "of keeping it in an Object variable), or use generics. Only "
            "equals(Object) and a few serialization methods require Object "
            "in their signatures.")),
        "concrete-collection": ("concrete collection type", (
            "Declaring a field, parameter, return type or variable with a "
            "concrete collection class (ArrayList, HashMap, TreeMap, ...) ties "
            "the code and its clients to one implementation: changing it "
            "(e.g. HashMap to TreeMap, to obtain ordered keys) forces changes "
            "everywhere the type is mentioned, and methods with such "
            "parameters reject other collections. Declare the interface (List, "
            "Map, Set, SortedMap, ...) and name the concrete class only where "
            "the object is created (new ArrayList<>()).")),
        "project-type": ("specific project type", (
            "The variable is declared with a specific class although every "
            "operation used on it is available in one of its supertypes. "
            "Declaring the base type states that the code works with any "
            "object of that family, and keeps the code independent of the "
            "concrete subclass (heuristic finding: check that the specific "
            "type is not needed elsewhere).")),
    },
    advice=(
        "Declare variables, fields, parameters, and return types with the "
        "most general type that offers the needed operations; name concrete "
        "classes only in `new`. Put type-dependent behaviour in the types "
        "themselves (methods declared in the base type and redefined in "
        "subtypes) instead of testing types and casting."
    ),
    analyze=None,   # set below
    prepare=None,   # set below
    none_found="No issues found.",
)


HIERARCHY = javaast.Hierarchy()


def prepare(analysed, others):
    global HIERARCHY
    HIERARCHY = javaast.build_hierarchy(analysed, others)


# ---------------------------------------------------------------------------
# Checks


def _in_equals(node):
    m = javaast.enclosing_method(node)
    if m is None or m.type != "method_declaration":
        return False
    if javaast.text(m.child_by_field_name("name")) != "equals":
        return False
    params = m.child_by_field_name("parameters")
    return params is not None and len([p for p in params.named_children
                                       if p.type == "formal_parameter"]) == 1


def _strip_parens(n):
    while n is not None and n.type == "parenthesized_expression":
        n = next((c for c in n.named_children), None)
    return n


def _is_get_class(n):
    n = _strip_parens(n)
    if n is None or n.type != "method_invocation":
        return False
    if javaast.text(n.child_by_field_name("name")) != "getClass":
        return False
    args = n.child_by_field_name("arguments")
    return args is not None and args.named_child_count == 0


def _is_read_object(n):
    n = _strip_parens(n)
    return n is not None and n.type == "method_invocation" and \
        javaast.text(n.child_by_field_name("name")) == "readObject"


def _read_object_vars(method):
    """Names of variables that hold the result of readObject() in a method
    (declared with it as initializer, or assigned it)."""
    names = set()
    if method is None:
        return names
    todo = [method]
    while todo:
        n = todo.pop()
        if n.type == "variable_declarator" and \
           _is_read_object(n.child_by_field_name("value")):
            names.add(javaast.text(n.child_by_field_name("name")))
        elif n.type == "assignment_expression" and \
                _is_read_object(n.child_by_field_name("right")):
            left = n.child_by_field_name("left")
            if left is not None and left.type == "identifier":
                names.add(javaast.text(left))
        todo.extend(n.named_children)
    return names


def _is_deserialized(node, operand):
    """True if `operand` (of a cast or instanceof) is the result of
    readObject(), directly or through a variable holding it."""
    operand = _strip_parens(operand)
    if operand is None:
        return False
    if _is_read_object(operand):
        return True
    if operand.type == "identifier":
        return javaast.text(operand) in _read_object_vars(
            javaast.enclosing_method(node))
    return False


def _is_compared(node):
    """True if the value of `node` is compared (==, !=, equals,
    equalsIgnoreCase, or used as a switch selector)."""
    p = node.parent
    while p is not None and p.type == "parenthesized_expression":
        node, p = p, p.parent
    if p is None:
        return False
    if p.type == "binary_expression":
        op = p.child_by_field_name("operator")
        return op is not None and op.type in ("==", "!=")
    if p.type == "method_invocation" and \
       javaast.text(p.child_by_field_name("name")) in ("equals", "equalsIgnoreCase"):
        return True
    if p.type == "argument_list" and p.parent is not None and \
       p.parent.type == "method_invocation" and \
       javaast.text(p.parent.child_by_field_name("name")) in ("equals",
                                                              "equalsIgnoreCase"):
        return True
    if p.type in ("switch_expression", "switch_statement"):
        return True
    return False


def _type_tests(node):
    """(description) if node is a type test, else None."""
    t = node.type
    if t == "instanceof_expression":
        return javaast.text(node)
    if t in ("type_pattern", "record_pattern"):
        # patterns inside switch labels (instanceof patterns are covered by
        # the instanceof_expression itself)
        p = node.parent
        while p is not None and p.type in ("pattern", "record_pattern",
                                           "record_pattern_body",
                                           "record_pattern_component"):
            p = p.parent
        if p is not None and p.type == "switch_label":
            return "case " + javaast.text(node)
        return None
    if t == "binary_expression":
        op = node.child_by_field_name("operator")
        if op is not None and op.type in ("==", "!="):
            if _is_get_class(node.child_by_field_name("left")) or \
               _is_get_class(node.child_by_field_name("right")):
                return javaast.text(node)
        return None
    if t == "method_invocation":
        if javaast.text(node.child_by_field_name("name")) != "equals":
            return None
        obj = node.child_by_field_name("object")
        args = node.child_by_field_name("arguments")
        if _is_get_class(obj) or (args is not None and any(
                _is_get_class(a) for a in args.named_children)):
            return javaast.text(node)
    return None


OBJECT_RETURN_METHODS = {"readResolve", "writeReplace", "clone"}


def _object_type(node):
    """For a type name node naming Object in a declaration, return
    (role, note), or None if it should not be reported."""
    name = javaast.text(node)
    if name not in ("Object", "java.lang.Object"):
        return None
    if node.type == "type_identifier" and node.parent is not None and \
       node.parent.type == "scoped_type_identifier":
        return None
    if name == "Object" and "Object" in HIERARCHY.by_simple:
        return None  # a project type named Object (!)
    u = node
    while u.parent is not None and u.parent.type == "array_type":
        u = u.parent
    p = u.parent
    if p is None:
        return None
    pt = p.type
    def names(decl):
        return ", ".join(javaast.text(d.child_by_field_name("name"))
                         for d in decl.children_by_field_name("declarator"))

    if pt == "field_declaration":
        return "field %s" % names(p), ""
    if pt in ("formal_parameter", "spread_parameter"):
        m = p.parent.parent if p.parent is not None else None
        if m is not None and m.type == "method_declaration" and \
           javaast.text(m.child_by_field_name("name")) == "equals" and \
           u == node:
            return None  # equals(Object)
        pn = p.child_by_field_name("name")
        if pn is None:  # spread_parameter: name inside a variable_declarator
            vd = next((c for c in p.named_children
                       if c.type == "variable_declarator"), None)
            pn = vd.child_by_field_name("name") if vd is not None else None
        return "parameter %s" % javaast.text(pn), ""
    if pt == "method_declaration":
        if javaast.text(p.child_by_field_name("name")) in OBJECT_RETURN_METHODS \
           and u == node:
            return None
        return "return type of %s" % javaast.text(p.child_by_field_name("name")), ""
    if pt in ("local_variable_declaration", "resource"):
        note = ""
        read_vars = _read_object_vars(javaast.enclosing_method(p))
        for d in p.children_by_field_name("declarator"):
            if _is_read_object(d.child_by_field_name("value")) or \
               javaast.text(d.child_by_field_name("name")) in read_vars:
                note = ("holds the result of readObject(): cast it directly "
                        "to the expected type instead")
        if p.type == "resource":
            return "local variable %s" % javaast.text(p.child_by_field_name("name")), note
        return "local variable %s" % names(p), note
    if pt == "enhanced_for_statement":
        return "for variable %s" % javaast.text(p.child_by_field_name("name")), ""
    if pt == "type_arguments":
        return "type argument", ""
    return None  # casts, instanceof, new, class literals, bounds, supertypes


def _concrete_collection(node):
    """For a type name node naming a concrete collection, return
    (simple name, role) or None if it should not be reported."""
    if node.type == "type_identifier":
        if node.parent is not None and node.parent.type == "scoped_type_identifier":
            return None
        name = javaast.text(node)
    elif node.type == "scoped_type_identifier":
        if node.parent is not None and node.parent.type == "scoped_type_identifier":
            return None
        name = javaast.text(node).rsplit(".", 1)[-1]
        if not javaast.text(node).startswith("java."):
            return None
    else:
        return None
    if name not in CONCRETE_COLLECTIONS or name in HIERARCHY.by_simple:
        return None

    u = node
    if u.parent is not None and u.parent.type == "generic_type":
        u = u.parent
    while u.parent is not None and u.parent.type == "array_type":
        u = u.parent
    p = u.parent
    if p is None:
        return None
    pt = p.type
    if pt in ("object_creation_expression", "array_creation_expression") and \
       p.child_by_field_name("type") == u:
        return None
    if pt in ("superclass", "type_list", "cast_expression",
              "instanceof_expression", "class_literal", "method_reference",
              "scoped_type_identifier"):
        return None
    if pt == "field_declaration":
        return name, "field"
    if pt in ("formal_parameter", "spread_parameter", "catch_formal_parameter"):
        return name, "parameter"
    if pt == "method_declaration":
        return name, "return type"
    if pt in ("local_variable_declaration", "resource", "enhanced_for_statement"):
        return name, "local variable"
    if pt == "type_arguments":
        return name, "type argument"
    if pt == "type_bound":
        return name, "type bound"
    return name, "declaration"


def _scope_of(decl_node, kind, stack):
    if kind == "field":
        return stack[-1].node.child_by_field_name("body") if stack else None
    if kind == "parameter":
        m = decl_node.parent.parent if decl_node.parent is not None else None
        return m.child_by_field_name("body") if m is not None else None
    return decl_node.parent  # local: the enclosing block


def _uses(scope, name, exclude):
    """Method names called on variable `name` in `scope`, or None if some use
    is not understood (passed as argument, returned, ...)."""
    methods = set()
    seen = 0
    todo = [scope]
    while todo:
        n = todo.pop()
        if n.type == "identifier" and javaast.text(n) == name and n != exclude:
            p = n.parent
            if p is None:
                return None
            if p.type == "method_invocation" and p.child_by_field_name("name") == n:
                pass  # a method with the same name, not the variable
            elif p.type == "method_invocation" and p.child_by_field_name("object") == n:
                methods.add(javaast.text(p.child_by_field_name("name")))
                seen += 1
            elif p.type == "field_access" and p.child_by_field_name("field") == n:
                obj = p.child_by_field_name("object")
                if obj is None or obj.type != "this":
                    pass  # other.name: not this variable
                else:
                    gp = p.parent
                    if gp is not None and gp.type == "method_invocation" and \
                       gp.child_by_field_name("object") == p:
                        methods.add(javaast.text(gp.child_by_field_name("name")))
                        seen += 1
                    elif gp is not None and gp.type == "assignment_expression" and \
                         gp.child_by_field_name("left") == p:
                        seen += 1
                    else:
                        return None
            elif p.type == "field_access" and p.child_by_field_name("object") == n:
                return None  # direct field access on the variable
            elif p.type == "assignment_expression" and p.child_by_field_name("left") == n:
                seen += 1
            elif p.type == "binary_expression" and \
                 p.child_by_field_name("operator").type in ("==", "!=") and \
                 any(javaast.text(s) == "null" for s in
                     (p.child_by_field_name("left"), p.child_by_field_name("right"))):
                seen += 1
            elif p.type in ("variable_declarator", "formal_parameter",
                            "catch_formal_parameter", "lambda_expression",
                            "inferred_parameters", "enhanced_for_statement"):
                return None  # redeclared (shadowing): give up
            elif p.type in ("method_declaration", "class_declaration",
                            "scoped_identifier", "scoped_type_identifier",
                            "type_identifier", "labeled_statement",
                            "break_statement", "continue_statement"):
                pass  # not a variable use
            else:
                return None
        todo.extend(n.named_children)
    return methods if seen else None


def _project_type_finding(sf, decl, tnode, names, kind, stack, package, imports):
    if tnode is None or tnode.type not in ("type_identifier",
                                           "scoped_type_identifier",
                                           "generic_type"):
        return []
    raw = javaast.type_base_name(tnode)
    enclosing = ".".join(([package] if package else []) +
                         [c.name for c in stack if not c.is_local]) or None
    fqcn = HIERARCHY.resolve(raw, package, imports, enclosing)
    if fqcn is None:
        return []
    supers = HIERARCHY.all_supers(fqcn)
    if not supers:
        return []
    scope = _scope_of(decl, kind, stack)
    if scope is None:
        return []
    out = []
    for name_node in names:
        name = javaast.text(name_node)
        used = _uses(scope, name, name_node)
        if not used:
            continue
        needed = used - OBJECT_METHODS
        if not needed:
            continue  # only Object methods: no evidence about the needed type
        cands = [s for s in supers
                 if needed <= HIERARCHY.available_methods(s)]
        if not cands:
            continue
        # all suitable supertypes, nearest first (all_supers is BFS order)
        general = cands
        simple = lambda f: HIERARCHY.types[f].simple
        line, col = javaast.position(name_node)
        out.append(Finding(
            sf.display, line, col, "LOW",
            javaast.member_path(name_node, stack) if kind != "field"
            else "%s.%s" % (javaast.type_path(stack), name),
            "%s %s: could be %s" % (javaast.text(tnode), name,
                                    " or ".join(simple(g) for g in general)),
            "methods used: %s" % (", ".join(sorted(used)) if used else "none"),
            {"kind": "project-type", "role": kind, "declared": fqcn,
             "suggested": general, "methods": sorted(used)},
        ))
    return out


def _overrides(method, stack, package):
    """True if the method may override/implement a supertype method (its
    parameter types are then fixed by the supertype's signature)."""
    if method.type != "method_declaration":
        return False
    if any(a.lstrip("@").split("(")[0].endswith("Override")
           for a in javaast.annotations(method)):
        return True
    if not stack or stack[-1].is_local:
        return stack[-1].kind == "anonymous" if stack else False
    fqcn = ".".join(([package] if package else []) + [c.name for c in stack])
    if fqcn not in HIERARCHY.types:
        return False
    name = javaast.text(method.child_by_field_name("name"))
    return any(name in HIERARCHY.types[s].methods
               for s in HIERARCHY.all_supers(fqcn))


def analyze(sf):
    findings = []
    package, imports = HIERARCHY.files.get(sf.path, ("", ((), ())))

    def add(node, severity, kind, detail, stack, note="", **extra):
        line, col = javaast.position(node)
        findings.append(Finding(sf.display, line, col, severity,
                                javaast.member_path(node, stack), detail, note,
                                {"kind": kind, **extra}))

    for node, stack, _in_code in javaast.walk(sf.root):
        t = node.type

        # -- type tests
        desc = _type_tests(node)
        if desc is not None:
            if t == "instanceof_expression" and \
               _is_deserialized(node, node.child_by_field_name("left")):
                continue  # checking what readObject() returned
            if not _in_equals(node):
                if len(desc) > 80:
                    desc = desc[:77] + "..."
                add(node, "HIGH", "type-test", desc, stack)
            continue

        # -- class names used as data
        if t == "method_invocation" and \
           javaast.text(node.child_by_field_name("name")) in CLASS_NAME_METHODS and \
           _is_get_class(node.child_by_field_name("object")):
            m = javaast.enclosing_method(node)
            note = ""
            if m is not None and m.type == "method_declaration" and \
               javaast.text(m.child_by_field_name("name")) == "toString":
                note = "in toString(): the printed text depends on the Java class name"
            severity = "MEDIUM"
            if _is_compared(node):
                severity = "HIGH"
                note = "the class name is compared: this is a type test"
            add(node, severity, "class-name", javaast.text(node), stack, note)
            continue

        # -- downcasts
        if t == "cast_expression":
            ttype = node.child_by_field_name("type")
            if ttype is None or ttype.type in PRIMITIVE_TYPES:
                continue
            if _in_equals(node):
                continue
            value = _strip_parens(node.child_by_field_name("value"))
            if _is_deserialized(node, value):
                continue  # deserialization: the cast is unavoidable
            target = javaast.text(ttype)
            note = ""
            if ttype.type == "generic_type":
                note = "cast to a generic type: unchecked"
            add(node, "HIGH", "downcast", "(%s) %s" % (target, javaast.text(value)[:50]),
                stack, note, target=target)
            continue

        # -- Object as a declared type
        if t in ("type_identifier", "scoped_type_identifier"):
            r = _object_type(node)
            if r is not None:
                role, note = r
                tname = javaast.text(node.parent) \
                    if node.parent.type == "array_type" else javaast.text(node)
                if node.parent.type == "spread_parameter":
                    tname += "..."
                words = role.split(" ")
                # "local variable o" -> "local variable Object o"
                if role.startswith(("field ", "parameter ", "local variable ",
                                    "for variable ")):
                    k = 2 if role.startswith(("local ", "for ")) else 1
                    detail = " ".join(words[:k] + [tname] + words[k:])
                else:
                    detail = "%s: %s" % (role, tname)
                add(node, "HIGH", "object-type", detail, stack, note,
                    role=" ".join(words[:2]) if role.startswith(("local", "for"))
                    else words[0])
                continue

        # -- concrete collections
        if t in ("type_identifier", "scoped_type_identifier"):
            r = _concrete_collection(node)
            if r is not None:
                name, role = r
                add(node, "MEDIUM", "concrete-collection",
                    "%s %s: declare as %s" % (role, javaast.text(node),
                                              CONCRETE_COLLECTIONS[name]),
                    stack, collection=name, role=role)
            continue

        # -- project types where a base type suffices
        if t == "field_declaration":
            names = [d.child_by_field_name("name")
                     for d in node.children_by_field_name("declarator")]
            findings.extend(_project_type_finding(
                sf, node, node.child_by_field_name("type"), names, "field",
                stack, package, imports))
        elif t == "local_variable_declaration":
            names = [d.child_by_field_name("name")
                     for d in node.children_by_field_name("declarator")]
            findings.extend(_project_type_finding(
                sf, node, node.child_by_field_name("type"), names, "local",
                stack, package, imports))
        elif t == "formal_parameter" and node.parent is not None and \
                node.parent.type == "formal_parameters" and \
                node.parent.parent is not None and node.parent.parent.type in (
                    "method_declaration", "constructor_declaration") and \
                not _overrides(node.parent.parent, stack, package):
            findings.extend(_project_type_finding(
                sf, node, node.child_by_field_name("type"),
                [node.child_by_field_name("name")], "parameter",
                stack, package, imports))

    return findings


TOOL.analyze = analyze
TOOL.prepare = prepare

if __name__ == "__main__":
    sys.exit(javaast.main(TOOL))
