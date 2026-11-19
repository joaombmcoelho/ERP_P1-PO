# Code analysis report

Generated on 2026-11-18 18:33 from `checked-out`: 98 projects analysed with 8 tools.

Each section below describes one problem and lists the projects where it was found, with the number of findings per severity (HIGH, MEDIUM, LOW). Expand a project to see its findings (`file:line:column [severity] location (detail)`). Files that students must not change are not analysed.

## Summary

| Problem | Projects | HIGH | MEDIUM | LOW |
|---|---:|---:|---:|---:|
| [Non-private instance fields](#non-private-instance-fields) | 10 | 8 | 13 | 13 |
| [Uses of static (constants excepted)](#uses-of-static-constants-excepted) | 22 | 69 | 4 | 4 |
| [Student-defined Message/Prompt/Label types](#student-defined-messagepromptlabel-types) | 1 | 6 | 0 | 0 |
| [Empty catch blocks](#empty-catch-blocks) | 14 | 45 | 14 | 0 |
| [Broad exception types caught, declared, or thrown](#broad-exception-types-caught-declared-or-thrown) | 12 | 22 | 0 | 0 |
| [Not programming to base types](#not-programming-to-base-types) | 43 | 113 | 128 | 32 |
| [Raw types (generic types without type arguments)](#raw-types-generic-types-without-type-arguments) | 2 | 1 | 5 | 0 |
| [Fields repeated along the class hierarchy](#fields-repeated-along-the-class-hierarchy) | 4 | 2 | 2 | 4 |

## Projects

Findings per project and problem (HIGH/MEDIUM/LOW); projects without findings are omitted (31 of 98).

| Project | [1](#non-private-instance-fields) | [2](#uses-of-static-constants-excepted) | [3](#student-defined-messagepromptlabel-types) | [4](#empty-catch-blocks) | [5](#broad-exception-types-caught-declared-or-thrown) | [6](#not-programming-to-base-types) | [7](#raw-types-generic-types-without-type-arguments) | [8](#fields-repeated-along-the-class-hierarchy) |
|---|---:|---:|---:|---:|---:|---:|---:|---:|
| [001](logs/ei/001.res.html) |  |  |  |  |  | 2/7/0 |  |  |
| [003](logs/ei/003.res.html) |  | 0/1/0 |  |  |  | 2/0/0 |  |  |
| [007](logs/ei/007.res.html) |  | 3/0/0 |  |  |  |  |  |  |
| [008](logs/ei/008.res.html) |  | 4/0/0 |  |  |  |  |  |  |
| [011](logs/ei/011.res.html) | 4/3/5 | 6/0/0 |  |  |  | 2/0/0 |  |  |
| [013](logs/ei/013.res.html) |  |  |  |  |  | 0/4/0 |  |  |
| [014](logs/ei/014.res.html) |  |  |  |  |  | 14/0/3 |  |  |
| [015](logs/ei/015.res.html) |  | 1/0/0 |  |  | 1/0/0 |  |  |  |
| [017](logs/ei/017.res.html) |  |  |  |  | 3/0/0 | 0/0/3 |  |  |
| [019](logs/ei/019.res.html) |  |  |  | 2/1/0 | 1/0/0 |  |  |  |
| [022](logs/ei/022.res.html) | 0/2/0 | 1/0/0 |  |  |  | 0/14/0 |  |  |
| [023](logs/ei/023.res.html) |  |  |  |  |  |  |  | 0/0/2 |
| [025](logs/ei/025.res.html) |  |  |  |  |  | 0/0/3 |  |  |
| [026](logs/ei/026.res.html) |  |  |  | 0/2/0 |  |  |  |  |
| [029](logs/ei/029.res.html) |  |  |  |  |  | 0/18/0 |  |  |
| [031](logs/ei/031.res.html) |  |  |  | 0/1/0 |  | 2/2/0 |  |  |
| [033](logs/ei/033.res.html) |  |  |  |  | 1/0/0 | 3/1/0 |  |  |
| [035](logs/ei/035.res.html) |  | 2/0/0 |  |  |  | 1/0/0 |  |  |
| [037](logs/ei/037.res.html) |  |  |  |  |  | 1/0/0 |  |  |
| [038](logs/ei/038.res.html) |  | 0/0/2 |  |  |  | 7/0/0 |  |  |
| [039](logs/ei/039.res.html) |  |  |  |  | 4/0/0 | 2/1/0 |  |  |
| [040](logs/ei/040.res.html) |  |  |  | 0/1/0 |  |  |  |  |
| [041](logs/ei/041.res.html) |  |  |  |  |  | 2/0/11 |  |  |
| [042](logs/ei/042.res.html) |  | 1/0/0 |  |  |  | 4/1/0 |  |  |
| [043](logs/ei/043.res.html) |  |  |  |  | 2/0/0 | 2/0/0 |  |  |
| [044](logs/ei/044.res.html) |  | 14/3/0 |  |  |  |  |  |  |
| [050](logs/ei/050.res.html) |  |  |  | 13/0/0 | 1/0/0 | 1/0/0 |  |  |
| [053](logs/ei/053.res.html) |  | 1/0/0 |  |  |  | 2/0/0 |  |  |
| [054](logs/ei/054.res.html) |  | 0/0/1 | 6/0/0 |  | 2/0/0 | 6/1/0 |  |  |
| [055](logs/ei/055.res.html) | 2/0/0 |  |  | 0/2/0 |  |  |  | 2/0/0 |
| [056](logs/ei/056.res.html) |  |  |  |  |  | 0/1/0 |  |  |
| [060](logs/ei/060.res.html) |  |  |  |  |  | 8/0/0 |  |  |
| [061](logs/ei/061.res.html) |  | 0/0/1 |  |  |  |  |  |  |
| [062](logs/ei/062.res.html) |  |  |  | 0/1/0 |  |  |  |  |
| [067](logs/ei/067.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [072](logs/ei/072.res.html) | 0/0/6 |  |  | 0/1/0 |  | 1/0/3 |  |  |
| [076](logs/ei/076.res.html) |  |  |  |  | 1/0/0 | 3/0/0 |  |  |
| [077](logs/ei/077.res.html) |  |  |  | 4/0/0 | 3/0/0 |  |  |  |
| [078](logs/ei/078.res.html) | 0/4/0 | 3/0/0 |  | 22/1/0 |  | 7/56/0 |  |  |
| [081](logs/ei/081.res.html) |  |  |  |  |  | 2/16/0 |  |  |
| [082](logs/ei/082.res.html) |  |  |  | 2/0/0 |  | 1/0/0 |  |  |
| [084](logs/ei/084.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [085](logs/ei/085.res.html) |  | 7/0/0 |  |  |  |  |  |  |
| [088](logs/ei/088.res.html) |  |  |  | 1/0/0 |  |  |  |  |
| [089](logs/ei/089.res.html) | 0/0/1 |  |  |  |  | 1/0/0 |  |  |
| [091](logs/ei/091.res.html) |  |  |  |  |  |  |  | 0/0/2 |
| [092](logs/ei/092.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [095](logs/ei/095.res.html) |  | 2/0/0 |  |  |  |  |  |  |
| [096](logs/ei/096.res.html) |  |  |  |  |  |  | 0/5/0 |  |
| [097](logs/ei/097.res.html) |  |  |  |  |  | 1/0/0 |  |  |
| [099](logs/ei/099.res.html) |  |  |  |  |  | 0/0/2 |  |  |
| [100](logs/ei/100.res.html) |  | 1/0/0 |  |  |  |  |  |  |
| [101](logs/ei/101.res.html) |  | 1/0/0 |  |  |  |  |  |  |
| [105](logs/ei/105.res.html) |  |  |  |  | 2/0/0 | 7/0/0 |  |  |
| [106](logs/ei/106.res.html) |  | 2/0/0 |  |  | 1/0/0 | 0/6/0 |  |  |
| [107](logs/ei/107.res.html) | 2/0/0 |  |  | 1/2/0 |  | 12/0/7 | 1/0/0 |  |
| [108](logs/ei/108.res.html) | 0/0/1 |  |  |  |  |  |  |  |
| [109](logs/ei/109.res.html) | 0/2/0 |  |  |  |  |  |  |  |
| [113](logs/ei/113.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [114](logs/ei/114.res.html) |  |  |  |  |  |  |  | 0/2/0 |
| [115](logs/ei/115.res.html) | 0/2/0 |  |  |  |  |  |  |  |
| [116](logs/ei/116.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [117](logs/ei/117.res.html) |  | 9/0/0 |  |  |  |  |  |  |
| [121](logs/ei/121.res.html) |  |  |  |  |  | 1/0/0 |  |  |
| [122](logs/ei/122.res.html) |  |  |  |  |  | 2/0/0 |  |  |
| [123](logs/ei/123.res.html) |  | 10/0/0 |  |  |  | 3/0/0 |  |  |
| [132](logs/ei/132.res.html) |  | 1/0/0 |  | 0/2/0 |  | 1/0/0 |  |  |

Columns: 1. Non-private instance fields; 2. Uses of static (constants excepted); 3. Student-defined Message/Prompt/Label types; 4. Empty catch blocks; 5. Broad exception types caught, declared, or thrown; 6. Not programming to base types; 7. Raw types (generic types without type arguments); 8. Fields repeated along the class hierarchy.

## Non-private instance fields

Detect non-private, non-static fields in Java code.

* **HIGH** (public): The field is public: any class in the program can read and modify it directly. The object can no longer guarantee its own invariants, the internal representation becomes part of the class's public contract, and changing it later breaks every client that uses it.
* **MEDIUM** (no modifier): The field has no access modifier (package-private): every class in the same package can read and modify it directly. This is often an oversight (a forgotten `private`) and couples the classes of the package to this class's internal representation.
* **LOW** (protected): The field is protected: subclasses (in any package) and all classes in the same package can access it directly. Subclasses become coupled to the superclass's representation and can break its invariants; prefer a private field with protected (or public) methods where needed.

**Fix:** Make the field private and, if other classes really need it, provide methods that express what they need (queries/operations), rather than plain getters/setters that merely re-expose the representation.

10 project(s):

* **[011](logs/ei/011.res.html)**: 4 HIGH, 3 MEDIUM, 5 LOW
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:34:29 [HIGH] Erp.categories  (public final Categories categories)
  erp-core/src/erp/client/Client.java:16:9 [MEDIUM] Client.gastoTotal  (int gastoTotal)
  erp-core/src/erp/product/Categories.java:15:27 [HIGH] Categories.FOOD  (public final Category FOOD)
  erp-core/src/erp/product/Categories.java:16:27 [HIGH] Categories.ELECTRONICS  (public final Category ELECTRONICS)
  erp-core/src/erp/product/Categories.java:17:27 [HIGH] Categories.CLOTHES  (public final Category CLOTHES)
  erp-core/src/erp/product/Product.java:11:9 [MEDIUM] Product.id  (int id)
  erp-core/src/erp/product/Product.java:12:9 [MEDIUM] Product.totalUnits  (int totalUnits)
  erp-core/src/erp/product/Product.java:13:22 [LOW] Product.name  (protected String name)
  erp-core/src/erp/product/Product.java:14:19 [LOW] Product.basePrice  (protected int basePrice)
  erp-core/src/erp/product/Product.java:15:24 [LOW] Product.category  (protected Category category)
  erp-core/src/erp/product/Product.java:16:19 [LOW] Product.baseDelay  (protected int baseDelay)
  erp-core/src/erp/product/Product.java:17:28 [LOW] Product.type  (protected final String type)
  ```

  </details>
* **[022](logs/ei/022.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:63:9 [MEDIUM] Erp._date  (int _date)
  erp-core/src/erp/notifications/NotificationMechanism.java:8:23 [MEDIUM] NotificationMechanism._notificationMessages  (ArrayList<String> _notificationMessages)
  ```

  </details>
* **[055](logs/ei/055.res.html)**: 2 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/basket/BasketLine.java:6:20 [HIGH] BasketLine._product  (public Product _product)
  erp-core/src/erp/basket/BasketLine.java:7:16 [HIGH] BasketLine._quantity  (public int _quantity)
  ```

  </details>
* **[072](logs/ei/072.res.html)**: 6 LOW
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/Product.java:11:19 [LOW] Product._id  (protected int _id)
  erp-core/src/erp/product/Product.java:12:22 [LOW] Product._name  (protected String _name)
  erp-core/src/erp/product/Product.java:13:24 [LOW] Product._category  (protected Category _category)
  erp-core/src/erp/product/Product.java:14:19 [LOW] Product._basePrice  (protected int _basePrice)
  erp-core/src/erp/product/Product.java:15:19 [LOW] Product._baseShippingTime  (protected int _baseShippingTime)
  erp-core/src/erp/product/Product.java:16:19 [LOW] Product._totalUnitsAvailable  (protected int _totalUnitsAvailable)
  ```

  </details>
* **[078](logs/ei/078.res.html)**: 4 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:37:9 [MEDIUM] Erp.size_products  (int size_products)
  erp-core/src/erp/Erp.java:44:9 [MEDIUM] Erp.sizeclients  (int sizeclients)
  erp-core/src/erp/product/Product.java:12:9 [MEDIUM] Product.prasoBaseEntrega  (int prasoBaseEntrega)
  erp-core/src/erp/supply/Supply.java:10:13 [MEDIUM] Supply._product  (Product _product)
  ```

  </details>
* **[089](logs/ei/089.res.html)**: 1 LOW
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/Product.java:12:22 [LOW] Product._recommendedPrice  (protected double _recommendedPrice)
  ```

  </details>
* **[107](logs/ei/107.res.html)**: 2 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/search/CategorySearchCriterion.java:7:25 [HIGH] CategorySearchCriterion._category  (public final String _category)
  erp-core/src/erp/search/NameSearchCriterion.java:7:25 [HIGH] NameSearchCriterion._name  (public final String _name)
  ```

  </details>
* **[108](logs/ei/108.res.html)**: 1 LOW
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/service/Service.java:10:34 [LOW] Service._wrapped  (protected final ServiceChain _wrapped)
  ```

  </details>
* **[109](logs/ei/109.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/exceptions/InvalidClientException.java:8:12 [MEDIUM] InvalidClientException._name  (String _name)
  erp-core/src/erp/exceptions/InvalidClientException.java:9:12 [MEDIUM] InvalidClientException._email  (String _email)
  ```

  </details>
* **[115](logs/ei/115.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/ImportedProduct.java:5:12 [MEDIUM] ImportedProduct._originCountry  (String _originCountry)
  erp-core/src/erp/product/ImportedProduct.java:6:12 [MEDIUM] ImportedProduct._costumsTAX  (double _costumsTAX)
  ```

  </details>

## Uses of static (constants excepted)

Detect uses of `static` in Java code, except constants (static final fields).

* **static non-final field**: A static field that is not final is global mutable state: a single variable shared by every instance and by every part of the program that can reach it. Objects no longer own their state, any code can change it behind their backs, separate instances (e.g. two ERP objects, or a loaded and a new one) interfere with each other, and the value is not saved with the object when it is serialized. Counters and registries belong in an object (e.g. the one that creates or manages the instances).
* **static method**: A static method belongs to no object: it cannot use instance state, cannot be overridden, and is not subject to dynamic dispatch, so it cannot take part in polymorphism. It is procedural code inside a class, and callers are hard-wired to that class. The behaviour usually belongs to one of the objects involved (often the one whose data it uses).
* **static initializer**: A static initializer block runs once, when the class is loaded, and can only set up class-wide (static) data. If it initializes constants, a direct initializer on the `static final` field is clearer; otherwise it is preparing global state, which belongs in an object (e.g. set up by a constructor).
* **static nested class**: A static nested class is not tied to an instance of the enclosing class; that is sometimes the right choice, but check that it should not be a top-level class of its own (or an inner class).
* **static nested interface**: Nested interfaces are implicitly static: writing `static` is redundant.
* **static nested enum**: Nested enums are implicitly static: writing `static` is redundant.
* **static nested record**: Nested records are implicitly static: writing `static` is redundant.
* **static nested annotation type**: Nested annotation types are implicitly static: writing `static` is redundant.

**Fix:** Keep `static` only for constants (`static final`). Move state into the objects that own it and behaviour into methods of those objects; use instance methods so subclasses can redefine them.

22 project(s):

* **[003](logs/ei/003.res.html)**: 1 MEDIUM (static initializer: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:51:5 [MEDIUM] Erp.<static initializer>  (static { ... })
  ```

  </details>
* **[007](logs/ei/007.res.html)**: 3 HIGH (static method: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:300:28 [HIGH] Erp.search(Collection<T>, SearchCriterion<T>, String, IntFunction<T[]>)  (private static T[] search(Collection<T>, SearchCriterion<T>, String, IntFunction<T[]>))
  erp-core/src/erp/product/ProductSearchCriterion.java:52:28 [HIGH] ProductSearchCriterion.names()  (public static String[] names())
  erp-core/src/erp/product/ProductSearchCriterion.java:69:42 [HIGH] ProductSearchCriterion.from(String)  (public static ProductSearchCriterion from(String))
  ```

  </details>
* **[008](logs/ei/008.res.html)**: 4 HIGH (static non-final field: 4)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Company.java:11:25 [HIGH] Company.serialVersionUID  (private static long serialVersionUID)
      note: serialVersionUID must be `static final` (a constant); without `final` it is a variable
  erp-core/src/erp/product/ProdImportado.java:7:25 [HIGH] ProdImportado.serialVersionUID  (private static long serialVersionUID)
      note: serialVersionUID must be `static final` (a constant); without `final` it is a variable
  erp-core/src/erp/product/ProdNacional.java:7:25 [HIGH] ProdNacional.serialVersionUID  (private static long serialVersionUID)
      note: serialVersionUID must be `static final` (a constant); without `final` it is a variable
  erp-core/src/erp/product/Product.java:13:25 [HIGH] Product.serialVersionUID  (private static long serialVersionUID)
      note: serialVersionUID must be `static final` (a constant); without `final` it is a variable
  ```

  </details>
* **[011](logs/ei/011.res.html)**: 6 HIGH (static method: 6)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:24:38 [HIGH] DoPerformSearch.getNomePredicate(String)  (public static Predicate<Product> getNomePredicate(String))
  erp-app/src/erp/app/product/DoPerformSearch.java:27:38 [HIGH] DoPerformSearch.getOriginPredicate(String)  (public static Predicate<Product> getOriginPredicate(String))
  erp-app/src/erp/app/product/DoPerformSearch.java:30:38 [HIGH] DoPerformSearch.getCategoryPredicate(String)  (public static Predicate<Product> getCategoryPredicate(String))
  erp-core/src/erp/Erp.java:105:36 [HIGH] Erp.getInfos(Map<?, T>, Predicate<? super T>)  (public static List<String> getInfos(Map<?, T>, Predicate<? super T>))
  erp-core/src/erp/Erp.java:108:36 [HIGH] Erp.getInfos(Map<?, T>, Predicate<? super T>, Function<T, String>)  (public static List<String> getInfos(Map<?, T>, Predicate<? super T>, Function<T, String>))
  erp-core/src/erp/Erp.java:116:36 [HIGH] Erp.getInfos(Map<?, T>)  (public static List<String> getInfos(Map<?, T>))
  ```

  </details>
* **[015](logs/ei/015.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:39:27 [HIGH] DoOpenFile.askFileName(String)  (private static String askFileName(String))
  ```

  </details>
* **[022](logs/ei/022.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/company/Company.java:116:39 [HIGH] Company.getCompanyComparator()  (public static Comparator<Company> getCompanyComparator())
  ```

  </details>
* **[035](logs/ei/035.res.html)**: 2 HIGH (static method: 2)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Product.java:49:19 [HIGH] Product.money(double)  (static String money(double))
  erp-core/src/erp/ProductCategory.java:6:28 [HIGH] ProductCategory.parse(String)  (static ProductCategory parse(String))
  ```

  </details>
* **[038](logs/ei/038.res.html)**: 2 LOW (static nested class: 2)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Company.java:103:26 [LOW] Company.Supply  (private static class Supply)
  erp-core/src/erp/Erp.java:377:26 [LOW] Erp.TransportRate  (private static class TransportRate)
  ```

  </details>
* **[042](logs/ei/042.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Provenance.java:29:38 [HIGH] Provenance.getAllProvenances()  (public static Collection<String> getAllProvenances())
  ```

  </details>
* **[044](logs/ei/044.res.html)**: 14 HIGH, 3 MEDIUM (static method: 14, static initializer: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:122:29 [HIGH] Erp.toCategory(String, String)  (private static Category toCategory(String, String))
  erp-core/src/erp/product/Product.java:69:29 [HIGH] Product.money(double)  (protected static String money(double))
  erp-core/src/erp/purchase/Purchase.java:49:28 [HIGH] Purchase.checkout(int, Basket, int)  (public static Purchase checkout(int, Basket, int))
  erp-core/src/erp/search/SearchCatalog.java:17:5 [MEDIUM] SearchCatalog.<static initializer>  (static { ... })
  erp-core/src/erp/search/SearchCatalog.java:25:25 [HIGH] SearchCatalog.register(SearchCriterion)  (private static void register(SearchCriterion))
  erp-core/src/erp/search/SearchCatalog.java:30:32 [HIGH] SearchCatalog.getKeys()  (public static List<String> getKeys())
  erp-core/src/erp/search/SearchCatalog.java:35:35 [HIGH] SearchCatalog.get(String)  (public static SearchCriterion get(String))
  erp-core/src/erp/shipping/ServiceCatalog.java:19:5 [MEDIUM] ServiceCatalog.<static initializer>  (static { ... })
  erp-core/src/erp/shipping/ServiceCatalog.java:27:32 [HIGH] ServiceCatalog.getKeys()  (public static List<String> getKeys())
  erp-core/src/erp/shipping/ServiceCatalog.java:32:27 [HIGH] ServiceCatalog.exists(String)  (public static boolean exists(String))
  erp-core/src/erp/shipping/ServiceCatalog.java:37:28 [HIGH] ServiceCatalog.apply(String, Shipping)  (public static Shipping apply(String, Shipping))
  erp-core/src/erp/split/SplitStrategyCatalog.java:17:5 [MEDIUM] SplitStrategyCatalog.<static initializer>  (static { ... })
  erp-core/src/erp/split/SplitStrategyCatalog.java:24:25 [HIGH] SplitStrategyCatalog.register(SplitStrategy)  (private static void register(SplitStrategy))
  erp-core/src/erp/split/SplitStrategyCatalog.java:29:33 [HIGH] SplitStrategyCatalog.getDefault()  (public static SplitStrategy getDefault())
  erp-core/src/erp/split/SplitStrategyCatalog.java:34:32 [HIGH] SplitStrategyCatalog.getKeys()  (public static List<String> getKeys())
  erp-core/src/erp/split/SplitStrategyCatalog.java:39:33 [HIGH] SplitStrategyCatalog.get(String)  (public static SplitStrategy get(String))
  erp-core/src/erp/util/Money.java:14:26 [HIGH] Money.format(double)  (public static String format(double))
  ```

  </details>
* **[053](logs/ei/053.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/ProductFormatter.java:9:19 [HIGH] ProductFormatter.format(String, Product, double)  (static String format(String, Product, double))
  ```

  </details>
* **[054](logs/ei/054.res.html)**: 1 LOW (static nested class: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/company/Company.java:18:23 [LOW] Company.ProductRegiment  (private static class ProductRegiment)
  ```

  </details>
* **[061](logs/ei/061.res.html)**: 1 LOW (static nested class: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/client/Client.java:47:26 [LOW] Client.NameComparator  (private static class NameComparator)
  ```

  </details>
* **[078](logs/ei/078.res.html)**: 3 HIGH (static non-final field: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/company/Company.java:15:24 [HIGH] Company.somadorE  (private static int somadorE)
  erp-core/src/erp/compras/Compras.java:16:24 [HIGH] Compras.somadorC  (private static int somadorC)
  erp-core/src/erp/product/Product.java:9:24 [HIGH] Product.somadorP  (private static int somadorP)
  ```

  </details>
* **[085](logs/ei/085.res.html)**: 7 HIGH (static method: 7)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/ProductCategory.java:10:29 [HIGH] ProductCategory.createNames()  (private static String[] createNames())
  erp-core/src/erp/product/ProductCategory.java:19:28 [HIGH] ProductCategory.getNames()  (public static String[] getNames())
  erp-core/src/erp/product/ProductCategory.java:23:27 [HIGH] ProductCategory.isValid(String)  (public static boolean isValid(String))
  erp-core/src/erp/product/SearchCriterion.java:26:29 [HIGH] SearchCriterion.createNames()  (private static String[] createNames())
  erp-core/src/erp/product/SearchCriterion.java:35:28 [HIGH] SearchCriterion.getNames()  (public static String[] getNames())
  erp-core/src/erp/strategy/DivisionStrategyTypes.java:18:29 [HIGH] DivisionStrategyTypes.createNames()  (private static String[] createNames())
  erp-core/src/erp/strategy/DivisionStrategyTypes.java:29:28 [HIGH] DivisionStrategyTypes.getNames()  (public static String[] getNames())
  ```

  </details>
* **[095](logs/ei/095.res.html)**: 2 HIGH (static method: 2)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:34:29 [HIGH] DoPerformSearch.options(List<String>)  (private static String[] options(List<String>))
  erp-app/src/erp/app/product/DoRegisterProduct.java:55:29 [HIGH] DoRegisterProduct.options(List<String>)  (private static String[] options(List<String>))
  ```

  </details>
* **[100](logs/ei/100.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/Product.java:76:27 [HIGH] Product.money(double)  (private static String money(double))
  ```

  </details>
* **[101](logs/ei/101.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Product.java:36:27 [HIGH] Product.money(double)  (private static String money(double))
  ```

  </details>
* **[106](logs/ei/106.res.html)**: 2 HIGH (static method: 2)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoSaveFile.java:27:17 [HIGH] DoSaveFile.save(ErpManager)  (static void save(ErpManager))
  erp-core/src/erp/Erp.java:137:28 [HIGH] Erp.blank(String)  (private static boolean blank(String))
  ```

  </details>
* **[117](logs/ei/117.res.html)**: 9 HIGH (static non-final field: 3, static method: 6)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/company/Company.java:16:24 [HIGH] Company._nextId  (private static int _nextId)
  erp-core/src/erp/product/Product.java:6:24 [HIGH] Product._nextId  (private static int _nextId)
  erp-core/src/erp/product/ProductCategory.java:13:35 [HIGH] ProductCategory.fromString(String)  (public static ProductCategory fromString(String))
  erp-core/src/erp/product/SearchCriterion.java:27:35 [HIGH] SearchCriterion.fromString(String)  (public static SearchCriterion fromString(String))
  erp-core/src/erp/receipt/Receipt.java:11:24 [HIGH] Receipt._nextId  (private static int _nextId)
  erp-core/src/erp/split/SplitStrategy.java:60:33 [HIGH] SplitStrategy.getDefault()  (public static SplitStrategy getDefault())
  erp-core/src/erp/transport/ModalityList.java:15:37 [HIGH] ModalityList.ModalityEnum.fromString(String)  (private static ModalityEnum fromString(String))
  erp-core/src/erp/transport/ModalityList.java:29:32 [HIGH] ModalityList.getInstance()  (public static ModalityList getInstance())
  erp-core/src/erp/transport/Service.java:19:39 [HIGH] Service.ServiceOption.fromString(String)  (private static ServiceOption fromString(String))
  ```

  </details>
* **[123](logs/ei/123.res.html)**: 10 HIGH (static method: 10)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/Presentation.java:23:26 [HIGH] Presentation.company(Company)  (public static String company(Company))
  erp-app/src/erp/app/Presentation.java:27:26 [HIGH] Presentation.client(Client)  (public static String client(Client))
  erp-app/src/erp/app/Presentation.java:32:26 [HIGH] Presentation.product(Product)  (public static String product(Product))
  erp-app/src/erp/app/Presentation.java:44:32 [HIGH] Presentation.basket(Basket)  (public static List<String> basket(Basket))
  erp-app/src/erp/app/Presentation.java:59:26 [HIGH] Presentation.purchase(Purchase)  (public static String purchase(Purchase))
  erp-app/src/erp/app/Presentation.java:65:32 [HIGH] Presentation.purchaseDetails(Purchase)  (public static List<String> purchaseDetails(Purchase))
  erp-app/src/erp/app/Presentation.java:79:26 [HIGH] Presentation.notification(DeliveryNotification)  (public static String notification(DeliveryNotification))
  erp-app/src/erp/app/Presentation.java:87:27 [HIGH] Presentation.orderHeader(int, Purchase.Order)  (private static String orderHeader(int, Purchase.Order))
  erp-app/src/erp/app/Presentation.java:92:27 [HIGH] Presentation.transport(TransportRate, List<ExtraService>)  (private static String transport(TransportRate, List<ExtraService>))
  erp-app/src/erp/app/Presentation.java:103:26 [HIGH] Presentation.supply(int, InventoryEntry)  (public static String supply(int, InventoryEntry))
  ```

  </details>
* **[132](logs/ei/132.res.html)**: 1 HIGH (static method: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/SearchCriteria.java:6:27 [HIGH] SearchCriteria.create(String, String)  (static SearchCriteria create(String, String))
  ```

  </details>

## Student-defined Message/Prompt/Label types

Detect Message/Prompt/Label types defined by students (outside the provided framework files).

* **HIGH** (in the core): The core (domain) defines a Message/Prompt/Label type, i.e. texts meant for the user. The core must not know how things are shown or worded: that is the job of the application (user interface) layer. The core should report situations with exceptions (whose contents are data, e.g. the unknown key), and return data or objects; the app turns them into messages using the provided Message/Prompt/Label interfaces.
* **MEDIUM** (in the app (not a provided file)): The application defines its own Message/Prompt/Label type outside the provided files. These interfaces are part of the provided framework: their canonical versions replace the students' copies before testing, so texts must come from the provided ones (and the provided files must not be changed). A separate copy duplicates the mechanism and its texts are not the expected ones.

**Fix:** Remove the type. In the core, signal problems with (domain) exceptions carrying the relevant data and let the app produce the texts; in the app, use the provided Message/Prompt/Label interfaces.

1 project(s):

* **[054](logs/ei/054.res.html)**: 6 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Message.java:3:11 [HIGH] erp.Message  (interface Message)
  erp-core/src/erp/exceptions/Message.java:3:11 [HIGH] erp.exceptions.Message  (interface Message)
  erp-core/src/erp/notification/Label.java:3:11 [HIGH] erp.notification.Label  (interface Label)
  erp-core/src/erp/notification/Message.java:3:11 [HIGH] erp.notification.Message  (interface Message)
  erp-core/src/erp/product/Label.java:3:18 [HIGH] erp.product.Label  (public interface Label)
  erp-core/src/erp/purchase/Label.java:3:18 [HIGH] erp.purchase.Label  (public interface Label)
  ```

  </details>

## Empty catch blocks

Detect empty catch blocks in Java code.

* **HIGH** (silently empty, or empty and catching a broad type): The catch block is empty: the exception is caught and silently discarded. The situation it signals (an unknown key, a missing file, a failed operation) is neither handled nor reported, so the program continues as if nothing had happened, often in an inconsistent state, and the cause of any later failure becomes very hard to find. When the caught type is Exception, Throwable, RuntimeException or Error, programming errors (e.g. NullPointerException, ClassCastException) are silently hidden too, whatever the comment says.
* **MEDIUM** (empty, but commented or marked as ignored): The catch block has no statements, although a comment or the parameter name shows that ignoring the exception was noticed. The exception is still swallowed: if it really cannot happen, the code is probably structured wrongly (e.g. the check should be made elsewhere, or the exception should not be declared); if it can happen, it must be handled or propagated.

**Fix:** Handle the exception (do what the situation requires), or let it propagate: declare it in `throws`, or wrap it in an exception that makes sense at the current level (e.g. in the app, translate a core exception into the corresponding provided app exception). Catch only the specific exception types that can actually occur.

14 project(s):

* **[019](logs/ei/019.res.html)**: 2 HIGH, 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/client/DoRegisterClient.java:26:9 [HIGH] DoRegisterClient.execute()  (catch (IllegalArgumentException e) { })
  erp-app/src/erp/app/client/DoShowClient.java:27:9 [HIGH] DoShowClient.execute()  (catch (UnknownClientException e) { })
  erp-app/src/erp/app/company/DoShowCompany.java:26:9 [MEDIUM] DoShowCompany.execute()  (catch (UnknownCompanyException e) { //??? })
  ```

  </details>
* **[026](logs/ei/026.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:342:18 [MEDIUM] Erp.showCompanyProducts(int)  (catch (UnknownProductException e) { /**CANNOT HAPPEN*/ })
  erp-core/src/erp/Erp.java:496:14 [MEDIUM] Erp.searchProduct(String, String)  (catch (UnknownProductException e) { /**Always exists!*/ })
  ```

  </details>
* **[031](logs/ei/031.res.html)**: 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:24:11 [MEDIUM] DoPerformSearch.execute()  (catch (CoreInvalidSearchTermException e) { /*No action is necessary*/ })
  ```

  </details>
* **[040](logs/ei/040.res.html)**: 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoShowProduct.java:27:11 [MEDIUM] DoShowProduct.execute()  (catch (UnknownProductKeyException e) { //adicionar algo })
  ```

  </details>
* **[050](logs/ei/050.res.html)**: 13 HIGH
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:46:23 [HIGH] DoOpenFile.execute()  (catch (MissingFileAssociationException | IOException ex) { })
  erp-app/src/erp/app/main/DoSaveFile.java:37:15 [HIGH] DoSaveFile.execute()  (catch (MissingFileAssociationException | IOException ex) { })
  erp-core/src/erp/Erp.java:93:14 [HIGH] Erp.listarClientes()  (catch (ClienteDesconhecido e) { })
  erp-core/src/erp/Erp.java:229:14 [HIGH] Erp.listarProdutos()  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/Erp.java:291:14 [HIGH] Erp.mostrarEmpresas()  (catch (EmpresaDesconhecida e) { })
  erp-core/src/erp/Erp.java:504:18 [HIGH] Erp.pesquisaProdutoNome(String)  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/Erp.java:524:18 [HIGH] Erp.pesquisaProdutoCategoria(String)  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/Erp.java:546:18 [HIGH] Erp.pesquisaProdutoPais(String)  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/ErpManager.java:106:10 [HIGH] ErpManager.saveAs(String)  (catch (IOException e) { })
  erp-core/src/erp/basket/Basket.java:141:15 [HIGH] Basket.getTotalCabaz()  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/basket/Basket.java:147:14 [HIGH] Basket.getTotalCabaz()  (catch (ProdutoNaoFornecido e) { })
  erp-core/src/erp/basket/Basket.java:171:15 [HIGH] Basket.Mostrarcabaz()  (catch (ProdutoDesconhecido e) { })
  erp-core/src/erp/basket/Basket.java:178:14 [HIGH] Basket.Mostrarcabaz()  (catch (ProdutoNaoFornecido e) { })
  ```

  </details>
* **[055](logs/ei/055.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:25:11 [MEDIUM] DoPerformSearch.execute()  (catch (UnrecognizedEntryException _) { // CANNOT HAPPEN })
  erp-app/src/erp/app/product/DoRegisterProduct.java:49:11 [MEDIUM] DoRegisterProduct.execute()  (catch (UnrecognizedEntryException _) { //CANNOT HAPPEN })
  ```

  </details>
* **[062](logs/ei/062.res.html)**: 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoAdvanceDate.java:25:11 [MEDIUM] DoAdvanceDate.execute()  (catch (InvalidTimeException e) { // do nothing })
  ```

  </details>
* **[072](logs/ei/072.res.html)**: 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:34:11 [MEDIUM] DoPerformSearch.execute()  (catch (InvalidSearchFilterException e) { /** never happens */ })
  ```

  </details>
* **[077](logs/ei/077.res.html)**: 4 HIGH
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:33:23 [HIGH] DoOpenFile.execute()  (catch (Exception ex) { })
      note: catches Exception: this also hides programming errors (e.g. NullPointerException)
  erp-app/src/erp/app/main/DoOpenFile.java:36:18 [HIGH] DoOpenFile.execute()  (catch (IOException e) { })
  erp-app/src/erp/app/main/DoSaveFile.java:33:23 [HIGH] DoSaveFile.execute()  (catch (Exception ex) { })
      note: catches Exception: this also hides programming errors (e.g. NullPointerException)
  erp-app/src/erp/app/main/DoSaveFile.java:36:10 [HIGH] DoSaveFile.execute()  (catch (IOException e) { })
  ```

  </details>
* **[078](logs/ei/078.res.html)**: 22 HIGH, 1 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/basket/DoAddService.java:23:9 [HIGH] DoAddService.execute()  (catch (UnknownWordException e) { })
  erp-app/src/erp/app/basket/DoChangeProduct.java:30:9 [HIGH] DoChangeProduct.execute()  (catch (ProductNotFoundException e) { })
  erp-app/src/erp/app/basket/DoChangeProduct.java:31:9 [HIGH] DoChangeProduct.execute()  (catch (WrongObjectException e) { })
  erp-app/src/erp/app/basket/DoCheckout.java:31:9 [HIGH] DoCheckout.execute()  (catch (ProductNotFoundException e) { })
  erp-app/src/erp/app/basket/DoCheckout.java:31:44 [HIGH] DoCheckout.execute()  (catch (WrongObjectException e) { })
  erp-app/src/erp/app/basket/DoCheckout.java:32:9 [HIGH] DoCheckout.execute()  (catch (CompraFaildException e) { })
  erp-app/src/erp/app/basket/DoCheckout.java:32:40 [HIGH] DoCheckout.execute()  (catch (CompanyNotFoundException e) { })
  erp-app/src/erp/app/basket/DoSetModality.java:28:9 [HIGH] DoSetModality.execute()  (catch (UnknownWordException e) { })
  erp-app/src/erp/app/basket/DoSetSplitStrategy.java:22:9 [HIGH] DoSetSplitStrategy.execute()  (catch (UnknownWordException e) { })
  erp-app/src/erp/app/basket/DoShowBasket.java:23:9 [HIGH] DoShowBasket.execute()  (catch (ProductNotFoundException e) { })
  erp-app/src/erp/app/basket/DoShowBasket.java:23:45 [HIGH] DoShowBasket.execute()  (catch (WrongObjectException e) { })
  erp-app/src/erp/app/basket/DoShowBasket.java:23:77 [HIGH] DoShowBasket.execute()  (catch (CompanyNotFoundException e) { })
  erp-app/src/erp/app/client/DoRegisterClient.java:24:9 [HIGH] DoRegisterClient.execute()  (catch (ClientRegistrationFailedException e) { })
  erp-app/src/erp/app/client/DoShowClient.java:24:9 [HIGH] DoShowClient.execute()  (catch (ClientNotFoundException e) { })
  erp-app/src/erp/app/company/DoShowCompanies.java:23:9 [MEDIUM] DoShowCompanies.execute()  (catch (CompanyNotFoundException e) { //não acontece })
  erp-app/src/erp/app/company/DoShowCompany.java:23:10 [HIGH] DoShowCompany.execute()  (catch (CompanyNotFoundException e) { })
  erp-app/src/erp/app/company/DoShowCompanyClients.java:24:10 [HIGH] DoShowCompanyClients.execute()  (catch (CompanyNotFoundException e) { })
  erp-app/src/erp/app/company/DoShowCompanyProducts.java:22:9 [HIGH] DoShowCompanyProducts.execute()  (catch (CompanyNotFoundException e) { })
  erp-app/src/erp/app/product/DoShowProduct.java:24:9 [HIGH] DoShowProduct.execute()  (catch (ProductNotFoundException e) { })
  erp-core/src/erp/Erp.java:121:13 [HIGH] Erp.Products_to_String()  (catch (ProductNotFoundException e) { })
  erp-core/src/erp/Erp.java:148:17 [HIGH] Erp.Search_Product_nome(String)  (catch (ProductNotFoundException e) { })
  erp-core/src/erp/Erp.java:169:17 [HIGH] Erp.Search_Product_categoria(String)  (catch (ProductNotFoundException e) { })
  erp-core/src/erp/Erp.java:188:21 [HIGH] Erp.Search_Product_origem(String)  (catch (ProductNotFoundException e) { })
  ```

  </details>
* **[082](logs/ei/082.res.html)**: 2 HIGH
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoSaveFile.java:33:13 [HIGH] DoSaveFile.execute()  (catch (MissingFileAssociationException | IOException e1) { })
  erp-app/src/erp/app/main/DoSaveFile.java:36:9 [HIGH] DoSaveFile.execute()  (catch (IOException e) { })
  ```

  </details>
* **[088](logs/ei/088.res.html)**: 1 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:58:11 [HIGH] Erp.importLine(String)  (catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | InvalidNameException | InvalidDataException e) { })
  ```

  </details>
* **[107](logs/ei/107.res.html)**: 1 HIGH, 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoAdvanceDate.java:24:9 [HIGH] DoAdvanceDate.execute()  (catch (InvalidParametersException e) { })
  erp-app/src/erp/app/main/DoSaveFile.java:31:13 [MEDIUM] DoSaveFile.execute()  (catch (IOException ioe) { /*FIXME do something */ })
      note: contains a FIXME/TODO comment: unfinished code
  erp-app/src/erp/app/main/DoSaveFile.java:33:9 [MEDIUM] DoSaveFile.execute()  (catch (IOException e) { /*FIXME do something */ })
      note: contains a FIXME/TODO comment: unfinished code
  ```

  </details>
* **[132](logs/ei/132.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:29:23 [MEDIUM] DoOpenFile.execute()  (catch (MissingFileAssociationException | IOException ex) { // ignore or handle })
  erp-app/src/erp/app/main/DoOpenFile.java:32:19 [MEDIUM] DoOpenFile.execute()  (catch (IOException e) { // ignore or handle })
  ```

  </details>

## Broad exception types caught, declared, or thrown

Detect catch clauses, throws declarations, and throw statements that use Exception, Throwable, RuntimeException, or Error.

* **catching a broad type**: Catching Exception, Throwable, RuntimeException or Error catches much more than the situations the code was written for: programming errors (NullPointerException, ClassCastException, IndexOutOfBoundsException, ...) and, for Throwable/Error, even failures of the virtual machine (e.g. OutOfMemoryError) end up in the same handler and are treated as if they were the expected problem, hiding bugs. The handler also cannot know what actually happened, so it cannot react appropriately.
* **declaring a broad type**: Declaring `throws Exception` (or Throwable, RuntimeException, Error) tells the caller nothing about what can go wrong, so the caller cannot handle specific situations and is pushed into catching Exception in turn. The method's contract should list the specific (preferably domain) exceptions it can throw. Declaring RuntimeException or Error is also meaningless: they are unchecked and never need to be declared.
* **throwing a broad type**: Throwing a new Exception, Throwable, RuntimeException or Error signals a problem without saying which: callers can only distinguish situations by parsing the message text, cannot catch this case without also catching everything else, and the information about what went wrong (e.g. the offending key) is reduced to a string. Each situation deserves its own exception class.

**Fix:** Throw, declare, and catch only specific exceptions. In the core, define domain exceptions that describe each situation (with the relevant data, e.g. the unknown key), throw and declare those; in the app, catch them and translate them into the provided app exceptions.

12 project(s):

* **[015](logs/ei/015.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Parser.java:47:18 [HIGH] Parser.parseLine(String)  (catch (Exception e))
  ```

  </details>
* **[017](logs/ei/017.res.html)**: 3 HIGH (catching a broad type: 2, throwing a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/client/DoRegisterClient.java:26:18 [HIGH] DoRegisterClient.execute()  (catch (Exception e))
  erp-app/src/erp/app/main/DoSaveFile.java:36:13 [HIGH] DoSaveFile.execute()  (throw new RuntimeException(e);)
  erp-core/src/erp/Erp.java:146:18 [HIGH] Erp.process(String...)  (catch (ClientAlreadyExistsException | InvalidInventoryChangeException | RuntimeException e))
  ```

  </details>
* **[019](logs/ei/019.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:78:14 [HIGH] Erp.process(String[])  (catch (RuntimeException | UnknownCompanyException | UnknownProductException e))
  ```

  </details>
* **[033](logs/ei/033.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ErpManager.java:73:18 [HIGH] ErpManager.importFile(String)  (catch (Exception e))
  ```

  </details>
* **[039](logs/ei/039.res.html)**: 4 HIGH (throwing a broad type: 4)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:72:17 [HIGH] DoOpenFile.saveCurrentState()  (throw new RuntimeException(saveException);)
  erp-app/src/erp/app/main/DoOpenFile.java:76:13 [HIGH] DoOpenFile.saveCurrentState()  (throw new RuntimeException(e);)
  erp-app/src/erp/app/main/DoSaveFile.java:42:17 [HIGH] DoSaveFile.execute()  (throw new RuntimeException(saveException);)
  erp-app/src/erp/app/main/DoSaveFile.java:46:13 [HIGH] DoSaveFile.execute()  (throw new RuntimeException(e);)
  ```

  </details>
* **[043](logs/ei/043.res.html)**: 2 HIGH (catching a broad type: 2)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:255:18 [HIGH] Erp.importEntry(String)  (catch (RuntimeException exception))
  erp-core/src/erp/ErpManager.java:54:18 [HIGH] ErpManager.load(String)  (catch (IOException | ClassNotFoundException | RuntimeException exception))
  ```

  </details>
* **[050](logs/ei/050.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Importar.java:62:16 [HIGH] Importar.casos(String[])  (catch (Exception e))
  ```

  </details>
* **[054](logs/ei/054.res.html)**: 2 HIGH (catching a broad type: 2)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:32:10 [HIGH] DoOpenFile.saveCurrentState()  (catch (Exception e))
  erp-app/src/erp/app/main/DoSaveFile.java:23:10 [HIGH] DoSaveFile.execute()  (catch (Exception e))
  ```

  </details>
* **[076](logs/ei/076.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:23:18 [HIGH] DoOpenFile.execute()  (catch (Exception e))
  ```

  </details>
* **[077](logs/ei/077.res.html)**: 3 HIGH (catching a broad type: 3)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/main/DoOpenFile.java:33:30 [HIGH] DoOpenFile.execute()  (catch (Exception ex))
  erp-app/src/erp/app/main/DoSaveFile.java:33:30 [HIGH] DoSaveFile.execute()  (catch (Exception ex))
  erp-core/src/erp/Erp.java:398:20 [HIGH] Erp.importFile(String)  (catch (DuplicateClientKeyException | NoSuchCompanyException | RuntimeException e))
  ```

  </details>
* **[105](logs/ei/105.res.html)**: 2 HIGH (catching a broad type: 2)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ErpManager.java:53:18 [HIGH] ErpManager.load(String)  (catch (Exception e))
  erp-core/src/erp/Parser.java:56:18 [HIGH] Parser.parseLine(String)  (catch (Exception e))
  ```

  </details>
* **[106](logs/ei/106.res.html)**: 1 HIGH (catching a broad type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:422:26 [HIGH] Erp.importFile(String)  (catch (RuntimeException | InvalidDataException | NoSuchCompanyException | NoSuchProductException e))
  ```

  </details>

## Not programming to base types

Detect type tests, downcasts, concrete collection types, and project types used where a base type suffices.

* **type test**: Testing the type of an object (instanceof, a type pattern in a switch, comparing getClass()) to decide what to do replaces polymorphism with a hand-written dispatch: each new subtype requires finding and changing every such test, and the behaviour of a type is scattered over the code that tests it instead of being in the type itself. Declare the operation in the base type and redefine it in each subtype (or use a double-dispatch/visitor scheme). Only equals(Object) legitimately needs to test the type of its argument.
* **downcast**: Casting a reference to a more specific type bypasses the type system: the compiler can no longer check it and the program fails at run time (ClassCastException) if the object is not of that type. A downcast means the code needs something the declared type does not offer: either the operation belongs in the base type (and is redefined in the subtypes), or the variable should have been declared with the more specific type in the first place (e.g. with proper generic types).
* **class name as data**: Using the name of an object's class (getClass().getSimpleName(), getName(), ...) to produce output or to make decisions is a type test in disguise: the output depends on how the Java classes happen to be named, which is an implementation detail (renaming or restructuring the classes changes what the program prints), and comparing class names has all the problems of instanceof. Each type should provide what is needed through a method declared in the base type and redefined in each subtype (e.g. a method returning the type's label, as defined by the specification).
* **Object as a declared type**: Declaring a field, parameter, return type, variable or type argument as Object throws away all knowledge of what it holds: nothing useful can be done with it without testing its type and casting it, and the compiler cannot check anything. Declare the type that the value actually has (e.g. assign the result of readObject() directly with a cast to the expected type, instead of keeping it in an Object variable), or use generics. Only equals(Object) and a few serialization methods require Object in their signatures.
* **concrete collection type**: Declaring a field, parameter, return type or variable with a concrete collection class (ArrayList, HashMap, TreeMap, ...) ties the code and its clients to one implementation: changing it (e.g. HashMap to TreeMap, to obtain ordered keys) forces changes everywhere the type is mentioned, and methods with such parameters reject other collections. Declare the interface (List, Map, Set, SortedMap, ...) and name the concrete class only where the object is created (new ArrayList&lt;&gt;()).
* **specific project type**: The variable is declared with a specific class although every operation used on it is available in one of its supertypes. Declaring the base type states that the code works with any object of that family, and keeps the code independent of the concrete subclass (heuristic finding: check that the specific type is not needed elsewhere).

**Fix:** Declare variables, fields, parameters, and return types with the most general type that offers the needed operations; name concrete classes only in `new`. Put type-dependent behaviour in the types themselves (methods declared in the base type and redefined in subtypes) instead of testing types and casting.

43 project(s):

* **[001](logs/ei/001.res.html)**: 2 HIGH, 7 MEDIUM (type test: 1, downcast: 1, concrete collection type: 7)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:43:17 [MEDIUM] Erp._companies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:45:17 [MEDIUM] Erp._products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:46:17 [MEDIUM] Erp._purchases  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:47:17 [MEDIUM] Erp._modalities  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/product/OriginSearch.java:16:13 [HIGH] OriginSearch.matches(Product, String)  (product instanceof ImportedProduct)
  erp-core/src/erp/product/OriginSearch.java:17:37 [HIGH] OriginSearch.matches(Product, String)  ((ImportedProduct) product)
  erp-core/src/erp/purchase/Purchase.java:17:19 [MEDIUM] Purchase._information  (field ArrayList: declare as List)
  erp-core/src/erp/purchase/Purchase.java:19:71 [MEDIUM] Purchase.<init>(int, Client, Company, int, ArrayList<PurchaseLine>)  (parameter ArrayList: declare as List)
  erp-core/src/erp/purchase/Purchase.java:43:12 [MEDIUM] Purchase.getInformation()  (return type ArrayList: declare as List)
  ```

  </details>
* **[003](logs/ei/003.res.html)**: 2 HIGH (type test: 1, Object as a declared type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:56:58 [HIGH] Erp.<static initializer>  (product instanceof ImportedProduct imported)
  erp-core/src/erp/ErpManager.java:66:13 [HIGH] ErpManager.load(String)  (local variable Object object)
      note: holds the result of readObject(): cast it directly to the expected type instead
  ```

  </details>
* **[011](logs/ei/011.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:28:31 [HIGH] DoPerformSearch.getOriginPredicate(String)  (p instanceof ImportedProduct)
  erp-app/src/erp/app/product/DoPerformSearch.java:28:64 [HIGH] DoPerformSearch.getOriginPredicate(String)  ((ImportedProduct) p)
  ```

  </details>
* **[013](logs/ei/013.res.html)**: 4 MEDIUM (concrete collection type: 4)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Company.java:25:19 [MEDIUM] Company._inventories  (field LinkedHashMap: declare as Map)
  erp-core/src/erp/Erp.java:17:19 [MEDIUM] Erp._products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:20:19 [MEDIUM] Erp._companies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:23:19 [MEDIUM] Erp._clients  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  ```

  </details>
* **[014](logs/ei/014.res.html)**: 14 HIGH, 3 LOW (type test: 7, downcast: 7, specific project type: 3)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoPerformSearch.java:33:8 [HIGH] DoPerformSearch.execute()  (p.getValue() instanceof NationalProduct)
  erp-app/src/erp/app/product/DoPerformSearch.java:34:21 [LOW] DoPerformSearch.execute()  (NationalProduct np: could be Product)
      note: methods used: getBasePrice, getCategory, getDeliveryTime, getName, getRecommendedPrice
  erp-app/src/erp/app/product/DoPerformSearch.java:34:26 [HIGH] DoPerformSearch.execute()  ((NationalProduct) p.getValue())
  erp-app/src/erp/app/product/DoPerformSearch.java:44:8 [HIGH] DoPerformSearch.execute()  (p.getValue() instanceof ImportedProduct)
  erp-app/src/erp/app/product/DoPerformSearch.java:45:27 [HIGH] DoPerformSearch.execute()  ((ImportedProduct) p.getValue())
  erp-app/src/erp/app/product/DoShowProduct.java:35:7 [HIGH] DoShowProduct.execute()  (product instanceof NationalProduct)
  erp-app/src/erp/app/product/DoShowProduct.java:36:20 [LOW] DoShowProduct.execute()  (NationalProduct np: could be Product)
      note: methods used: getBasePrice, getCategory, getDeliveryTime, getName, getRecommendedPrice
  erp-app/src/erp/app/product/DoShowProduct.java:36:25 [HIGH] DoShowProduct.execute()  ((NationalProduct) product)
  erp-app/src/erp/app/product/DoShowProduct.java:46:7 [HIGH] DoShowProduct.execute()  (product instanceof ImportedProduct)
  erp-app/src/erp/app/product/DoShowProduct.java:47:26 [HIGH] DoShowProduct.execute()  ((ImportedProduct) product)
  erp-app/src/erp/app/product/DoShowProducts.java:31:8 [HIGH] DoShowProducts.execute()  (p.getValue() instanceof NationalProduct)
  erp-app/src/erp/app/product/DoShowProducts.java:32:21 [LOW] DoShowProducts.execute()  (NationalProduct np: could be Product)
      note: methods used: getBasePrice, getCategory, getDeliveryTime, getName, getRecommendedPrice
  erp-app/src/erp/app/product/DoShowProducts.java:32:26 [HIGH] DoShowProducts.execute()  ((NationalProduct) p.getValue())
  erp-app/src/erp/app/product/DoShowProducts.java:42:8 [HIGH] DoShowProducts.execute()  (p.getValue() instanceof ImportedProduct)
  erp-app/src/erp/app/product/DoShowProducts.java:43:27 [HIGH] DoShowProducts.execute()  ((ImportedProduct) p.getValue())
  erp-core/src/erp/Erp.java:201:9 [HIGH] Erp.searchByOrigin(String)  (prod.getValue() instanceof ImportedProduct)
  erp-core/src/erp/Erp.java:202:27 [HIGH] Erp.searchByOrigin(String)  ((ImportedProduct) prod.getValue())
  ```

  </details>
* **[017](logs/ei/017.res.html)**: 3 LOW (specific project type: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:439:28 [LOW] Erp.checkout(String, int)  (CompanyProduct companyProduct: could be Line)
      note: methods used: getQuantity
  erp-core/src/erp/basket/Basket.java:94:20 [LOW] Basket.alterQuantity(Product, int)  (BasketLine line: could be Line)
      note: methods used: getQuantity, setQuantity
  erp-core/src/erp/basket/Basket.java:136:44 [LOW] Basket.getLinePrice(BasketLine)  (BasketLine line: could be Line)
      note: methods used: getProduct, getQuantity
  ```

  </details>
* **[022](logs/ei/022.res.html)**: 14 MEDIUM (concrete collection type: 14)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:234:13 [MEDIUM] Erp.showCompanyClients(int)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:257:13 [MEDIUM] Erp.showCompanyProducts(int)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:356:9 [MEDIUM] Erp.performSearch(String, String)  (local variable ArrayList: declare as List)
  erp-core/src/erp/client/Client.java:49:12 [MEDIUM] Client.getNotifications()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:19:13 [MEDIUM] Company._clients  (field ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:97:12 [MEDIUM] Company.getProducts()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:99:9 [MEDIUM] Company.getProducts()  (local variable ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:140:12 [MEDIUM] Company.getClients()  (return type ArrayList: declare as List)
  erp-core/src/erp/notifications/NotificationMechanism.java:8:5 [MEDIUM] NotificationMechanism._notificationMessages  (field ArrayList: declare as List)
  erp-core/src/erp/notifications/NotificationMechanism.java:13:12 [MEDIUM] NotificationMechanism.getNotificationMessages()  (return type ArrayList: declare as List)
  erp-core/src/erp/notifications/NotificationMechanism.java:14:9 [MEDIUM] NotificationMechanism.getNotificationMessages()  (local variable ArrayList: declare as List)
  erp-core/src/erp/product/Product.java:13:19 [MEDIUM] Product._supplies  (field ArrayList: declare as List)
  erp-core/src/erp/search/ProductSearchCriteria.java:12:13 [MEDIUM] ProductSearchCriteria.filterProductList(List<Product>, String)  (return type ArrayList: declare as List)
  erp-core/src/erp/search/ProductSearchCriteria.java:13:9 [MEDIUM] ProductSearchCriteria.filterProductList(List<Product>, String)  (local variable ArrayList: declare as List)
  ```

  </details>
* **[025](logs/ei/025.res.html)**: 3 LOW (specific project type: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:153:17 [LOW] Erp.ShowProduct(int)  (Produto produto: could be Visitable)
      note: methods used: acceptVisitor
  erp-core/src/erp/Erp.java:235:17 [LOW] Erp.ShowClient(String)  (Cliente cliente: could be Visitable)
      note: methods used: acceptVisitor
  erp-core/src/erp/Erp.java:352:17 [LOW] Erp.ShowCompany(int)  (Empresa empresa: could be Visitable)
      note: methods used: acceptVisitor
  ```

  </details>
* **[029](logs/ei/029.res.html)**: 18 MEDIUM (concrete collection type: 18)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/client/DoShowClients.java:21:9 [MEDIUM] DoShowClients.execute()  (local variable ArrayList: declare as List)
  erp-app/src/erp/app/company/DoShowCompanies.java:22:9 [MEDIUM] DoShowCompanies.execute()  (local variable ArrayList: declare as List)
  erp-app/src/erp/app/company/DoShowCompanyProducts.java:23:13 [MEDIUM] DoShowCompanyProducts.execute()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:35:13 [MEDIUM] Erp.products  (field ArrayList: declare as List)
  erp-core/src/erp/Erp.java:36:13 [MEDIUM] Erp.companies  (field ArrayList: declare as List)
  erp-core/src/erp/Erp.java:166:12 [MEDIUM] Erp.doShowClients()  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:167:9 [MEDIUM] Erp.doShowClients()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:196:12 [MEDIUM] Erp.doShowCompanies()  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:197:9 [MEDIUM] Erp.doShowCompanies()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:232:12 [MEDIUM] Erp.doShowCompanyProducts(int)  (return type ArrayList: declare as List)
  erp-core/src/erp/ErpManager.java:62:12 [MEDIUM] ErpManager.doShowCompanies()  (return type ArrayList: declare as List)
  erp-core/src/erp/ErpManager.java:70:12 [MEDIUM] ErpManager.doShowCompanyProducts(int)  (return type ArrayList: declare as List)
  erp-core/src/erp/client/Client.java:15:13 [MEDIUM] Client._baskets  (field ArrayList: declare as List)
  erp-core/src/erp/client/Client.java:17:13 [MEDIUM] Client._inbox  (field ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:43:12 [MEDIUM] Company.showProducts()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:44:9 [MEDIUM] Company.showProducts()  (local variable ArrayList: declare as List)
  erp-core/src/erp/delirevy/Delivery.java:12:13 [MEDIUM] Delivery._add_services  (field ArrayList: declare as List)
  erp-core/src/erp/delirevy/Delivery.java:17:67 [MEDIUM] Delivery.<init>(Date, Modality, int, ArrayList<AdditionalService>)  (parameter ArrayList: declare as List)
  ```

  </details>
* **[031](logs/ei/031.res.html)**: 2 HIGH, 2 MEDIUM (type test: 1, downcast: 1, concrete collection type: 2)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/company/Company.java:47:12 [MEDIUM] Company.getProducts()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:48:9 [MEDIUM] Company.getProducts()  (local variable ArrayList: declare as List)
  erp-core/src/erp/searcher/OriginSearch.java:8:13 [HIGH] OriginSearch.match(Product, String)  (product instanceof ImportedProduct)
  erp-core/src/erp/searcher/OriginSearch.java:9:33 [HIGH] OriginSearch.match(Product, String)  ((ImportedProduct) product)
  ```

  </details>
* **[033](logs/ei/033.res.html)**: 3 HIGH, 1 MEDIUM (type test: 1, downcast: 2, concrete collection type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:148:12 [MEDIUM] Erp.getProducts()  (return type TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:149:16 [HIGH] Erp.getProducts()  ((TreeMap<Integer, Product>) _products)
      note: cast to a generic type: unchecked
  erp-core/src/erp/ErpManager.java:149:25 [HIGH] ErpManager.productSearch(String, String)  (p instanceof ImportedProduct)
  erp-core/src/erp/ErpManager.java:150:47 [HIGH] ErpManager.productSearch(String, String)  ((ImportedProduct) p)
  ```

  </details>
* **[035](logs/ei/035.res.html)**: 1 HIGH (type test: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ProductSearchCriterion.java:16:20 [HIGH] ProductSearchCriterion.matches(Product, String)  (p instanceof ImportedProduct ip)
  ```

  </details>
* **[037](logs/ei/037.res.html)**: 1 HIGH (downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/OriginCriterion.java:8:33 [HIGH] OriginCriterion.matchCriterion(Product, String)  ((ImportedProduct) product)
  ```

  </details>
* **[038](logs/ei/038.res.html)**: 7 HIGH (type test: 7)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/DoShowProduct.java:36:27 [HIGH] DoShowProduct.execute()  (product instanceof ImportedProduct)
  erp-app/src/erp/app/product/DoShowProduct.java:52:17 [HIGH] DoShowProduct.execute()  (product instanceof ImportedProduct imported)
  erp-core/src/erp/Erp.java:169:34 [HIGH] Erp.searchProducts(String, String)  (product instanceof ImportedProduct imported)
  erp-core/src/erp/ErpManager.java:222:27 [HIGH] ErpManager.showProducts()  (product instanceof ImportedProduct)
  erp-core/src/erp/ErpManager.java:237:17 [HIGH] ErpManager.showProducts()  (product instanceof ImportedProduct imported)
  erp-core/src/erp/ErpManager.java:259:23 [HIGH] ErpManager.formatProduct(Product)  (product instanceof ImportedProduct)
  erp-core/src/erp/ErpManager.java:274:13 [HIGH] ErpManager.formatProduct(Product)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[039](logs/ei/039.res.html)**: 2 HIGH, 1 MEDIUM (type test: 1, Object as a declared type: 1, concrete collection type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:188:9 [MEDIUM] Erp.clients()  (local variable ArrayList: declare as List)
  erp-core/src/erp/ErpManager.java:106:13 [HIGH] ErpManager.load(String)  (local variable Object object)
      note: holds the result of readObject(): cast it directly to the expected type instead
  erp-core/src/erp/product/search/OriginSearchCriterion.java:18:15 [HIGH] OriginSearchCriterion.matches(Product, String)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[041](logs/ei/041.res.html)**: 2 HIGH, 11 LOW (downcast: 2, specific project type: 11)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/basket/Basket.java:140:39 [LOW] Basket.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/client/Client.java:70:39 [LOW] Client.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/client/Notification.java:38:39 [LOW] Notification.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/company/Company.java:66:39 [LOW] Company.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/company/Supply.java:51:39 [LOW] Supply.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/product/Category.java:22:39 [LOW] Category.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/product/ProductVisitor.java:17:22 [HIGH] ProductVisitor.visit(NationalProduct)  ((Product) product)
  erp-core/src/erp/product/ProductVisitor.java:21:22 [HIGH] ProductVisitor.visit(ImportedProduct)  ((Product) product)
  erp-core/src/erp/purchase/AdditionalService.java:38:39 [LOW] AdditionalService.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/purchase/Order.java:96:39 [LOW] Order.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/purchase/Purchase.java:107:39 [LOW] Purchase.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/purchase/PurchaseLine.java:43:39 [LOW] PurchaseLine.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  erp-core/src/erp/purchase/TransportModality.java:37:39 [LOW] TransportModality.accept(ErpVisitor<R>)  (ErpVisitor<R> visitor: could be ProductVisitor)
      note: methods used: visit
  ```

  </details>
* **[042](logs/ei/042.res.html)**: 4 HIGH, 1 MEDIUM (type test: 2, downcast: 2, concrete collection type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ByOrigin.java:12:14 [HIGH] ByOrigin.satisfies(Product)  (product.getProvenance() instanceof Imported)
  erp-core/src/erp/ByOrigin.java:15:29 [HIGH] ByOrigin.satisfies(Product)  ((Imported) product.getProvenance())
  erp-core/src/erp/Erp.java:200:9 [MEDIUM] Erp.showClients()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Product.java:53:12 [HIGH] Product.getOriginCountry()  (getProvenance() instanceof Imported)
  erp-core/src/erp/Product.java:55:33 [HIGH] Product.getOriginCountry()  ((Imported) getProvenance())
  ```

  </details>
* **[043](logs/ei/043.res.html)**: 2 HIGH (type test: 1, Object as a declared type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:141:48 [HIGH] Erp.searchProducts(String, String)  (product instanceof ImportedProduct imported)
  erp-core/src/erp/ErpManager.java:50:13 [HIGH] ErpManager.load(String)  (local variable Object state)
      note: holds the result of readObject(): cast it directly to the expected type instead
  ```

  </details>
* **[050](logs/ei/050.res.html)**: 1 HIGH (downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:542:37 [HIGH] Erp.pesquisaProdutoPais(String)  ((Importado) a.getProveniencia())
  ```

  </details>
* **[053](logs/ei/053.res.html)**: 2 HIGH (type test: 2)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/product/ProductFormatter.java:10:24 [HIGH] ProductFormatter.format(String, Product, double)  (p instanceof ImportedProduct)
  erp-app/src/erp/app/product/ProductFormatter.java:11:23 [HIGH] ProductFormatter.format(String, Product, double)  (p instanceof ImportedProduct i)
  ```

  </details>
* **[054](logs/ei/054.res.html)**: 6 HIGH, 1 MEDIUM (type test: 3, downcast: 3, concrete collection type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:136:37 [HIGH] Erp.getRecommendedPrice(int)  ((ImportedProvenance) product.getProvenance())
  erp-core/src/erp/ErpStringer.java:55:36 [HIGH] ErpStringer.getProductString(int)  ((ImportedProvenance) product.getProvenance())
  erp-core/src/erp/observer/DeliveryObserver.java:11:7 [HIGH] DeliveryObserver.update(ErpEvent)  (event instanceof DeliveryEvent)
  erp-core/src/erp/observer/DeliveryObserver.java:12:34 [HIGH] DeliveryObserver.update(ErpEvent)  ((DeliveryEvent) event)
  erp-core/src/erp/observer/DeliveryObserver.java:14:8 [HIGH] DeliveryObserver.update(ErpEvent)  (event instanceof DeliveryUpcomingEvent)
  erp-core/src/erp/observer/DeliveryObserver.java:17:13 [HIGH] DeliveryObserver.update(ErpEvent)  (event instanceof DeliveryDeliveredEvent)
  erp-core/src/erp/purchase/Purchase.java:24:16 [MEDIUM] Purchase._productPurchases  (field HashMap: declare as Map)
  ```

  </details>
* **[056](logs/ei/056.res.html)**: 1 MEDIUM (concrete collection type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:11:47 [MEDIUM] Erp  (declaration TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  ```

  </details>
* **[060](logs/ei/060.res.html)**: 8 HIGH (type test: 3, downcast: 4, Object as a declared type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:135:35 [HIGH] Erp.registerProduct(Provenance, String, int, Category, int)  (provenance instanceof Imported)
  erp-core/src/erp/Erp.java:136:31 [HIGH] Erp.registerProduct(Provenance, String, int, Category, int)  ((Imported) provenance)
  erp-core/src/erp/ErpManager.java:190:13 [HIGH] ErpManager.load(String)  (local variable Object loadedObject)
      note: holds the result of readObject(): cast it directly to the expected type instead
  erp-core/src/erp/product/Product.java:47:13 [HIGH] Product.toString(int)  (this.provenance instanceof Imported)
  erp-core/src/erp/product/Product.java:57:18 [HIGH] Product.toString(int)  ((Imported) provenance)
  erp-core/src/erp/product/Product.java:58:18 [HIGH] Product.toString(int)  ((Imported) provenance)
  erp-core/src/erp/search/OriginCriterion.java:16:13 [HIGH] OriginCriterion.matches(Product)  (provenance instanceof Imported)
  erp-core/src/erp/search/OriginCriterion.java:17:31 [HIGH] OriginCriterion.matches(Product)  ((Imported) provenance)
  ```

  </details>
* **[067](logs/ei/067.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/productSearchCriteria/OriginCriteria.java:9:25 [HIGH] OriginCriteria.match(Product, String)  (product instanceof ImportedProduct)
  erp-core/src/erp/productSearchCriteria/OriginCriteria.java:12:30 [HIGH] OriginCriteria.match(Product, String)  ((ImportedProduct) product)
  ```

  </details>
* **[072](logs/ei/072.res.html)**: 1 HIGH, 3 LOW (downcast: 1, specific project type: 3)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/ImportedProduct.java:41:29 [LOW] ImportedProduct.equals(Object)  (ImportedProduct p: could be Product)
      note: methods used: getBasePrice, getBaseShippingTime, getCategory, getID, getName
  erp-core/src/erp/product/OriginSearch.java:15:43 [HIGH] OriginSearch.search(Product, String)  ((ImportedProduct) product)
  erp-core/src/erp/purchase/GiftCard.java:26:22 [LOW] GiftCard.equals(Object)  (GiftCard gC: could be AdditionalService)
      note: methods used: getAdditionalCost, getAdditionalTime, getKey
  erp-core/src/erp/purchase/GiftWrap.java:26:22 [LOW] GiftWrap.equals(Object)  (GiftWrap gW: could be AdditionalService)
      note: methods used: getAdditionalCost, getAdditionalTime, getKey
  ```

  </details>
* **[076](logs/ei/076.res.html)**: 3 HIGH (type test: 1, downcast: 1, Object as a declared type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ErpManager.java:46:13 [HIGH] ErpManager.load(String)  (local variable Object object)
      note: holds the result of readObject(): cast it directly to the expected type instead
  erp-core/src/erp/product/Product.java:80:13 [HIGH] Product.toString(int)  (_provenance instanceof Imported)
  erp-core/src/erp/product/Product.java:81:33 [HIGH] Product.toString(int)  ((Imported) _provenance)
  ```

  </details>
* **[078](logs/ei/078.res.html)**: 7 HIGH, 56 MEDIUM (type test: 4, downcast: 3, concrete collection type: 56)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:36:13 [MEDIUM] Erp.products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:40:13 [MEDIUM] Erp.companys  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:43:13 [MEDIUM] Erp.clients  (field ArrayList: declare as List)
  erp-core/src/erp/Erp.java:46:13 [MEDIUM] Erp.compras  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:113:12 [MEDIUM] Erp.Products_to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:114:9 [MEDIUM] Erp.Products_to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:126:12 [MEDIUM] Erp.Search_Product(String, String)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:133:9 [MEDIUM] Erp.Search_Product(String, String)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:137:12 [MEDIUM] Erp.Search_Product_nome(String)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:138:9 [MEDIUM] Erp.Search_Product_nome(String)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:154:12 [MEDIUM] Erp.Search_Product_categoria(String)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:155:9 [MEDIUM] Erp.Search_Product_categoria(String)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:175:12 [MEDIUM] Erp.Search_Product_origem(String)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:176:9 [MEDIUM] Erp.Search_Product_origem(String)  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:180:16 [HIGH] Erp.Search_Product_origem(String)  (product instanceof ImportedProduct)
  erp-core/src/erp/Erp.java:181:39 [HIGH] Erp.Search_Product_origem(String)  ((ImportedProduct) product)
  erp-core/src/erp/Erp.java:228:12 [MEDIUM] Erp.Companys_to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:229:9 [MEDIUM] Erp.Companys_to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/Erp.java:238:12 [MEDIUM] Erp.Company_Clients_to_String(int)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:246:12 [MEDIUM] Erp.Company_Products_to_String(int)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:295:12 [MEDIUM] Erp.Basket_to_String(Basket)  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:342:12 [MEDIUM] Erp.Clients_to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/Erp.java:343:9 [MEDIUM] Erp.Clients_to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/basket/Basket.java:33:13 [MEDIUM] Basket.products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Basket.java:40:13 [MEDIUM] Basket.Services  (field ArrayList: declare as List)
  erp-core/src/erp/basket/Basket.java:98:12 [MEDIUM] Basket.getproducts()  (return type TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Basket.java:133:35 [MEDIUM] Basket.finalizeBasket(TreeMap<Integer, SupplyGlobal>, int)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Basket.java:164:12 [MEDIUM] Basket.to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/basket/Basket.java:166:9 [MEDIUM] Basket.to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/basket/Encomenda.java:13:13 [MEDIUM] Encomenda._encomenda  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Encomenda.java:13:29 [MEDIUM] Encomenda._encomenda  (type argument TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Encomenda.java:16:22 [MEDIUM] Encomenda.<init>(TreeMap<Integer,SupplyComp>, DivisaoEncomendas, Transport)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/client/Client.java:14:13 [MEDIUM] Client.baskets  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/client/Client.java:55:35 [MEDIUM] Client.finalizeBasket(TreeMap<Integer, SupplyGlobal>, int, Company)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:17:13 [MEDIUM] Company.Supplys  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:19:13 [MEDIUM] Company.clients  (field ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:27:12 [MEDIUM] Company.getSupllys()  (return type TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:31:12 [MEDIUM] Company.getclients()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:99:12 [MEDIUM] Company.Clients_to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:100:9 [MEDIUM] Company.Clients_to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:119:12 [MEDIUM] Company.Products_to_String()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:120:9 [MEDIUM] Company.Products_to_String()  (local variable ArrayList: declare as List)
  erp-core/src/erp/compras/Compras.java:20:13 [MEDIUM] Compras.basket  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/compras/Compras.java:26:62 [MEDIUM] Compras.<init>(Client, Company, int, TreeMap<Integer, SupplyGlobal>)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/compras/Compras.java:63:32 [MEDIUM] Compras.finalizeBasket(TreeMap<Integer, SupplyGlobal>)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/compras/Compras.java:66:9 [MEDIUM] Compras.finalizeBasket(TreeMap<Integer, SupplyGlobal>)  (local variable TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/DivisaoEncomendas.java:11:12 [MEDIUM] DivisaoEncomendas.empacota(TreeMap<Integer,SupplyComp>, Transport)  (return type TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/DivisaoEncomendas.java:11:28 [MEDIUM] DivisaoEncomendas.empacota(TreeMap<Integer,SupplyComp>, Transport)  (type argument TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/DivisaoEncomendas.java:11:66 [MEDIUM] DivisaoEncomendas.empacota(TreeMap<Integer,SupplyComp>, Transport)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:15:12 [MEDIUM] PRAZO.empacota(TreeMap<Integer,SupplyComp>, Transport)  (return type TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:15:28 [MEDIUM] PRAZO.empacota(TreeMap<Integer,SupplyComp>, Transport)  (type argument TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:15:66 [MEDIUM] PRAZO.empacota(TreeMap<Integer,SupplyComp>, Transport)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:17:9 [MEDIUM] PRAZO.empacota(TreeMap<Integer,SupplyComp>, Transport)  (local variable TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:17:25 [MEDIUM] PRAZO.empacota(TreeMap<Integer,SupplyComp>, Transport)  (type argument TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:38:41 [MEDIUM] PRAZO.add(SupplyComp, TreeMap<Integer, TreeMap<Integer,SupplyComp>>, int)  (parameter TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:38:58 [MEDIUM] PRAZO.add(SupplyComp, TreeMap<Integer, TreeMap<Integer,SupplyComp>>, int)  (type argument TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:40:13 [MEDIUM] PRAZO.add(SupplyComp, TreeMap<Integer, TreeMap<Integer,SupplyComp>>, int)  (local variable TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/divisao_encomendas/PRAZO.java:44:13 [MEDIUM] PRAZO.add(SupplyComp, TreeMap<Integer, TreeMap<Integer,SupplyComp>>, int)  (local variable TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/supply/SupplyGlobal.java:33:12 [HIGH] SupplyGlobal.calculaPrecoRecomendado()  (temp instanceof NationalProduct)
  erp-core/src/erp/supply/SupplyGlobal.java:41:17 [HIGH] SupplyGlobal.calculaPrecoRecomendado()  (temp instanceof ImportedProduct)
  erp-core/src/erp/supply/SupplyGlobal.java:42:33 [HIGH] SupplyGlobal.calculaPrecoRecomendado()  ((ImportedProduct) temp)
  erp-core/src/erp/supply/SupplyGlobal.java:55:12 [HIGH] SupplyGlobal.toString()  (product instanceof ImportedProduct)
  erp-core/src/erp/supply/SupplyGlobal.java:56:35 [HIGH] SupplyGlobal.toString()  ((ImportedProduct) product)
  ```

  </details>
* **[081](logs/ei/081.res.html)**: 2 HIGH, 16 MEDIUM (type test: 1, downcast: 1, concrete collection type: 16)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:40:13 [MEDIUM] Erp._products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:42:13 [MEDIUM] Erp._companies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:44:13 [MEDIUM] Erp._clients  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:46:13 [MEDIUM] Erp._productCriteria  (field LinkedHashMap: declare as Map)
  erp-core/src/erp/Erp.java:48:13 [MEDIUM] Erp._strategies  (field LinkedHashMap: declare as Map)
  erp-core/src/erp/basket/Basket.java:29:13 [MEDIUM] Basket._basketLines  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/client/Client.java:25:13 [MEDIUM] Client._notifications  (field ArrayList: declare as List)
  erp-core/src/erp/client/Client.java:27:19 [MEDIUM] Client._baskets  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/client/Client.java:61:12 [MEDIUM] Client.notifications()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:22:13 [MEDIUM] Company._supplies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:24:13 [MEDIUM] Company._clients  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:26:13 [MEDIUM] Company._purchases  (field ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:78:12 [MEDIUM] Company.getSupplies()  (return type ArrayList: declare as List)
  erp-core/src/erp/company/Company.java:82:12 [MEDIUM] Company.getClients()  (return type ArrayList: declare as List)
  erp-core/src/erp/product/Product.java:24:13 [MEDIUM] Product._supplies  (field ArrayList: declare as List)
  erp-core/src/erp/product/Product.java:60:12 [MEDIUM] Product.getSupplies()  (return type ArrayList: declare as List)
  erp-core/src/erp/search/OriginCriterion.java:17:13 [HIGH] OriginCriterion.matches(Product, String)  (product instanceof ImportedProduct)
  erp-core/src/erp/search/OriginCriterion.java:18:47 [HIGH] OriginCriterion.matches(Product, String)  ((ImportedProduct) product)
  ```

  </details>
* **[082](logs/ei/082.res.html)**: 1 HIGH (type test: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/search/SearchByOrigin.java:13:15 [HIGH] SearchByOrigin.matches(Product, String)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[084](logs/ei/084.res.html)**: 2 HIGH (type test: 1, Object as a declared type: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/ErpManager.java:77:13 [HIGH] ErpManager.load(String)  (local variable Object obj)
      note: holds the result of readObject(): cast it directly to the expected type instead
  erp-core/src/erp/product/OriginSearch.java:13:13 [HIGH] OriginSearch.matches(Product, String)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[089](logs/ei/089.res.html)**: 1 HIGH (downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/SearchByOrigin.java:16:22 [HIGH] SearchByOrigin.match(Product)  ((ImportedProduct) product)
  ```

  </details>
* **[092](logs/ei/092.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/OriginCriteria.java:12:12 [HIGH] OriginCriteria.test(Product)  (product instanceof Imported)
  erp-core/src/erp/product/OriginCriteria.java:13:33 [HIGH] OriginCriteria.test(Product)  ((Imported) product)
  ```

  </details>
* **[097](logs/ei/097.res.html)**: 1 HIGH (type test: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/OriginCriterion.java:8:12 [HIGH] OriginCriterion.match(Product, String)  (p instanceof ImportedProduct ip)
  ```

  </details>
* **[099](logs/ei/099.res.html)**: 2 LOW (specific project type: 2)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/client/DoShowClients.java:19:20 [LOW] DoShowClients.execute()  (TextWriter writer: could be EntityWriter)
      note: methods used: writeClient
  erp-app/src/erp/app/company/DoShowCompanyProducts.java:21:20 [LOW] DoShowCompanyProducts.execute()  (TextWriter writer: could be EntityWriter)
      note: methods used: writeSupply
  ```

  </details>
* **[105](logs/ei/105.res.html)**: 7 HIGH (type test: 1, downcast: 1, Object as a declared type: 5)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:104:105 [HIGH] Erp.registerProduct(String, String, int, String, int, Object...)  (parameter Object... extra)
  erp-core/src/erp/ErpManager.java:104:105 [HIGH] ErpManager.registerProduct(String, String, int, String, int, Object...)  (parameter Object... extra)
  erp-core/src/erp/product/ImportedProductFactory.java:10:97 [HIGH] ImportedProductFactory.create(int, String, int, Category, int, Object...)  (parameter Object... extra)
  erp-core/src/erp/product/ImportedProductFactory.java:12:78 [HIGH] ImportedProductFactory.create(int, String, int, Category, int, Object...)  ((String) extra[0])
  erp-core/src/erp/product/NationalProductFactory.java:9:97 [HIGH] NationalProductFactory.create(int, String, int, Category, int, Object...)  (parameter Object... extra)
  erp-core/src/erp/product/ProductFactory.java:8:90 [HIGH] ProductFactory.create(int, String, int, Category, int, Object...)  (parameter Object... extra)
  erp-core/src/erp/search/OriginFilter.java:13:17 [HIGH] OriginFilter.matches(Product, String)  (p instanceof ImportedProduct imp)
  ```

  </details>
* **[106](logs/ei/106.res.html)**: 6 MEDIUM (concrete collection type: 6)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:53:19 [MEDIUM] Erp._companies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:54:19 [MEDIUM] Erp._products  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:55:19 [MEDIUM] Erp._clients  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/Erp.java:56:19 [MEDIUM] Erp._purchases  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/basket/Basket.java:31:19 [MEDIUM] Basket._lines  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  erp-core/src/erp/company/Company.java:24:19 [MEDIUM] Company._supplies  (field TreeMap: declare as Map (or SortedMap/NavigableMap if ordered operations are used))
  ```

  </details>
* **[107](logs/ei/107.res.html)**: 12 HIGH, 7 LOW (type test: 5, downcast: 6, Object as a declared type: 1, specific project type: 7)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/client/Client.java:35:47 [LOW] Client.acceptInfoVisitor(InfoVisitor)  (InfoVisitor visitor: could be Visitor)
      note: methods used: visit
  erp-core/src/erp/client/Client.java:39:26 [HIGH] Client.compareTo(Object)  (parameter Object o)
  erp-core/src/erp/client/Client.java:40:23 [HIGH] Client.compareTo(Object)  ((Client) o)
  erp-core/src/erp/company/Company.java:109:47 [LOW] Company.acceptInfoVisitor(InfoVisitor)  (InfoVisitor visitor: could be Visitor)
      note: methods used: visit
  erp-core/src/erp/product/Product.java:74:47 [LOW] Product.acceptInfoVisitor(InfoVisitor)  (InfoVisitor visitor: could be Visitor)
      note: methods used: visit
  erp-core/src/erp/product/Product.java:78:47 [LOW] Product.acceptInfoVisitor(InfoVisitor, SearchCriterion<Product>)  (InfoVisitor visitor: could be Visitor)
      note: methods used: visit
  erp-core/src/erp/search/OriginSearchCriterion.java:15:12 [HIGH] OriginSearchCriterion.checkCriterion(Product)  (checked instanceof ImportedProduct)
  erp-core/src/erp/search/OriginSearchCriterion.java:16:37 [HIGH] OriginSearchCriterion.checkCriterion(Product)  ((ImportedProduct) checked)
  erp-core/src/erp/supply/Supply.java:65:47 [LOW] Supply.acceptInfoVisitor(InfoVisitor)  (InfoVisitor visitor: could be Visitor)
      note: methods used: visit
  erp-core/src/erp/visitor/InfoVisitor.java:29:12 [HIGH] InfoVisitor.visit(Product)  (product instanceof NationalProduct)
  erp-core/src/erp/visitor/InfoVisitor.java:30:29 [LOW] InfoVisitor.visit(Product)  (NationalProduct p: could be Product)
      note: methods used: getArrivalTime, getBasePrice, getCategory, getId, getName, getRecommendedPrice, getStock
  erp-core/src/erp/visitor/InfoVisitor.java:30:33 [HIGH] InfoVisitor.visit(Product)  ((NationalProduct) product)
  erp-core/src/erp/visitor/InfoVisitor.java:35:17 [HIGH] InfoVisitor.visit(Product)  (product instanceof ImportedProduct)
  erp-core/src/erp/visitor/InfoVisitor.java:36:33 [HIGH] InfoVisitor.visit(Product)  ((ImportedProduct) product)
  erp-core/src/erp/visitor/InfoVisitor.java:49:16 [HIGH] InfoVisitor.visit(Product, SearchCriterion<Product>)  (product instanceof NationalProduct)
  erp-core/src/erp/visitor/InfoVisitor.java:50:33 [LOW] InfoVisitor.visit(Product, SearchCriterion<Product>)  (NationalProduct p: could be Product)
      note: methods used: getArrivalTime, getBasePrice, getCategory, getId, getName, getRecommendedPrice, getStock
  erp-core/src/erp/visitor/InfoVisitor.java:50:37 [HIGH] InfoVisitor.visit(Product, SearchCriterion<Product>)  ((NationalProduct) product)
  erp-core/src/erp/visitor/InfoVisitor.java:55:21 [HIGH] InfoVisitor.visit(Product, SearchCriterion<Product>)  (product instanceof ImportedProduct)
  erp-core/src/erp/visitor/InfoVisitor.java:56:37 [HIGH] InfoVisitor.visit(Product, SearchCriterion<Product>)  ((ImportedProduct) product)
  ```

  </details>
* **[113](logs/ei/113.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Criterion.java:19:17 [HIGH] Criterion.matches(Product, String)  (p instanceof ImportedProduct)
  erp-core/src/erp/Criterion.java:20:38 [HIGH] Criterion.matches(Product, String)  ((ImportedProduct) p)
  ```

  </details>
* **[116](logs/ei/116.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/search/OriginSearch.java:15:17 [HIGH] OriginSearch.search(String, List<Product>)  (product instanceof ImportedProduct)
  erp-core/src/erp/search/OriginSearch.java:16:45 [HIGH] OriginSearch.search(String, List<Product>)  ((ImportedProduct) product)
  ```

  </details>
* **[121](logs/ei/121.res.html)**: 1 HIGH (type test: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/search/OriginSearchCriterion.java:21:13 [HIGH] OriginSearchCriterion.matches(Product, String)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[122](logs/ei/122.res.html)**: 2 HIGH (type test: 1, downcast: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/search/ByOrigin.java:17:13 [HIGH] ByOrigin.matches(Product, String)  (p instanceof ImportedProduct)
  erp-core/src/erp/product/search/ByOrigin.java:18:40 [HIGH] ByOrigin.matches(Product, String)  ((ImportedProduct) p)
  ```

  </details>
* **[123](logs/ei/123.res.html)**: 3 HIGH (type test: 3)
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/Presentation.java:33:28 [HIGH] Presentation.product(Product)  (product instanceof ImportedProduct)
  erp-app/src/erp/app/Presentation.java:38:13 [HIGH] Presentation.product(Product)  (product instanceof ImportedProduct entry)
  erp-core/src/erp/ErpManager.java:54:34 [HIGH] ErpManager.searchProducts(String, String)  (product instanceof ImportedProduct imported)
  ```

  </details>
* **[132](logs/ei/132.res.html)**: 1 HIGH (type test: 1)
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/product/SearchCriteria.java:11:36 [HIGH] SearchCriteria.create(String, String)  (p instanceof Imported imported)
  ```

  </details>

## Raw types (generic types without type arguments)

Detect generic types used without type arguments.

* **HIGH** (raw type in a declaration or supertype): A generic type is used without type arguments (a raw type) in a declaration: the compiler no longer knows what the collection (or other generic object) holds, so it cannot check what is put into it, everything taken out of it is just Object and must be cast, and mistakes only appear at run time as ClassCastException. A raw supertype (e.g. `implements Comparable`) forces the inherited methods to take Object parameters (e.g. compareTo(Object)), with casts inside. Raw types exist only for compatibility with pre-Java 5 code.
* **MEDIUM** (raw type in object creation or cast): A generic type is created or cast to without type arguments: `new ArrayList()` produces a raw object that is converted unchecked to the declared type (the compiler warns about unchecked operations), and a raw cast discards the type information. Use the diamond (`new ArrayList<>()`), which infers the type arguments from the declaration.

**Fix:** Always give generic types their type arguments (List&lt;Product&gt;, Map&lt;String, Client&gt;, Comparable&lt;Product&gt;, Command&lt;ErpManager&gt;); in `new`, use the diamond (new ArrayList&lt;&gt;()). Compile with -Xlint:unchecked to see the warnings raw types cause.

2 project(s):

* **[096](logs/ei/096.res.html)**: 5 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/Erp.java:36:52 [MEDIUM] Erp._companies  (new TreeMap(...): use new TreeMap<>(...))
  erp-core/src/erp/Erp.java:38:48 [MEDIUM] Erp._clients  (new TreeMap(...): use new TreeMap<>(...))
  erp-core/src/erp/Erp.java:40:51 [MEDIUM] Erp._products  (new TreeMap(...): use new TreeMap<>(...))
  erp-core/src/erp/client/Client.java:10:47 [MEDIUM] Client._basket  (new TreeMap(...): use new TreeMap<>(...))
  erp-core/src/erp/company/Company.java:10:58 [MEDIUM] Company._products  (new TreeMap(...): use new TreeMap<>(...))
  ```

  </details>
* **[107](logs/ei/107.res.html)**: 1 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/client/Client.java:10:46 [HIGH] Client  (supertype Comparable: use Comparable<T>)
  ```

  </details>

## Fields repeated along the class hierarchy

Detect subclass fields that repeat superclass fields, and fields repeated in sibling subclasses.

* **HIGH** (repeats a superclass field): The subclass declares a field that its superclass already has. Every object of the subclass then contains two variables for the same piece of data: the superclass's methods use one and the subclass's methods use the other, so they easily get out of sync (e.g. the subclass's constructor sets its own copy, while the inherited methods still see the superclass's). If the names are identical, the subclass field also hides the inherited one, which makes the code confusing. The subclass should use the inherited data (through the superclass's constructor and methods).
* **MEDIUM** (repeated in all subclasses): Every subclass of the same superclass declares this field: the data is common to the whole family and is repeated instead of being declared once in the superclass. The code that manages it (constructors, getters, operations) is usually repeated too, and a new subclass has to remember to repeat it again.
* **LOW** (repeated in some subclasses): Several (but not all) subclasses of the same superclass declare this field. Check whether it is common data that belongs in the superclass (or in an intermediate class shared by those subclasses).

**Fix:** Declare each piece of data once, in the most general class to which it applies (private, initialized by that class's constructor); subclasses pass the values to super(...) and use the inherited methods instead of keeping their own copies.

4 project(s):

* **[023](logs/ei/023.res.html)**: 2 LOW
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/basket/DoCheckout.java:13:26 [LOW] DoCheckout._basket  (Basket _basket also in DoSetModality: BasketCommand is provided (cannot be changed): check whether the data is already available from it (e.g. a command's receiver))
      note: declared in 2 of the 6 subclasses of BasketCommand
  erp-app/src/erp/app/basket/DoSetModality.java:12:26 [LOW] DoSetModality._basket  (Basket _basket also in DoCheckout: BasketCommand is provided (cannot be changed): check whether the data is already available from it (e.g. a command's receiver))
      note: declared in 2 of the 6 subclasses of BasketCommand
  ```

  </details>
* **[055](logs/ei/055.res.html)**: 2 HIGH
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/strategy/Prazo.java:9:26 [HIGH] Prazo._key  (String _key repeats SplitStrategy._key (String))
      note: same name: the subclass field hides the inherited one; the superclass field is private: the object holds two copies of the same data
  erp-core/src/erp/strategy/Unica.java:8:26 [HIGH] Unica._key  (String _key repeats SplitStrategy._key (String))
      note: same name: the subclass field hides the inherited one; the superclass field is private: the object holds two copies of the same data
  ```

  </details>
* **[091](logs/ei/091.res.html)**: 2 LOW
  <details><summary>findings</summary>

  ```
  erp-app/src/erp/app/visitor/DetailedPrinterVisitor.java:8:20 [LOW] DetailedPrinterVisitor._result  (String _result also in StandardPrinterVisitor: move to Visitor)
      note: declared in 2 of the 3 subclasses of Visitor
  erp-app/src/erp/app/visitor/StandardPrinterVisitor.java:15:20 [LOW] StandardPrinterVisitor._result  (String _result also in DetailedPrinterVisitor: move to Visitor)
      note: declared in 2 of the 3 subclasses of Visitor
  ```

  </details>
* **[114](logs/ei/114.res.html)**: 2 MEDIUM
  <details><summary>findings</summary>

  ```
  erp-core/src/erp/notification/Delivered.java:11:26 [MEDIUM] Delivered._message  (String _message also in UpcomingDelivery: move to Notification)
      note: declared in 2 of the 2 subclasses of Notification
  erp-core/src/erp/notification/UpcomingDelivery.java:11:26 [MEDIUM] UpcomingDelivery._message  (String _message also in Delivered: move to Notification)
      note: declared in 2 of the 2 subclasses of Notification
  ```

  </details>

## Files with syntax errors

These files were analysed as far as possible; findings near the errors may be incomplete.

* **011**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 20), `erp-app/src/erp/app/basket/DoSetModality.java` (line 21)
* **013**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18)
* **019**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18), `erp-core/src/erp/ErpManager.java` (line 73)
* **029**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18)
* **039**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18)
* **040**: `erp-core/src/erp/Erp.java` (line 119)
* **056**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18), `erp-core/src/erp/Erp.java` (line 11)
* **096**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18), `erp-app/src/erp/app/product/DoRegisterProduct.java` (line 20)
* **100**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18), `erp-app/src/erp/app/product/DoPerformSearch.java` (line 13)
* **107**: `erp-core/src/erp/product/ImportedProduct.java` (line 12)
* **121**: `erp-app/src/erp/app/basket/DoCheckout.java` (line 18), `erp-app/src/erp/app/basket/DoSetModality.java` (line 18)
* **124**: `erp-core/src/erp/company/Company.java` (line 64, 64)

