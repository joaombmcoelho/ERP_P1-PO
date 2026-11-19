package erp;

import erp.exceptions.*;
import java.io.IOException;
import java.io.Serializable;

//imported by me
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import java.util.Arrays;
import java.util.LinkedHashMap;

import erp.purchase.Purchase;
//import this to use
import erp.purchase.TransportMethod;
import erp.purchase.AdditionalService;
import erp.purchase.OrderSplit;
import erp.company.Company;
import erp.company.InventoryStatus;
import erp.company.ProductInventory;
import erp.client.Client;
import erp.product.Product;
import erp.product.ProductFactory;
import erp.product.Search;
import erp.basket.AddToBasketStatus;
import erp.basket.Basket;
import erp.basket.CheckoutStatus;
import erp.product.Category;
import erp.product.OriginSearch;
import erp.product.NameSearch;
import erp.product.CategorySearch;
import erp.product.NationalProductFactory;
import erp.product.ImportedProductFactory;

/** The whole ERP system: holds all the state and the domain logic. */
class Erp implements Serializable {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** Current date of the system */
    private int _date;

    /** Id (map key) for the next company to get registered */
    private int _nextCompanyId;

    /** Id (map key) for the next product to get registered */
    private int _nextProductId;

    /** Id (map key) for the next purchase to get registered */
    private int _nextPurchaseId;

    /** Register of companies */
    private Map <Integer, Company> _companies;  

    /** Register of products */
    private Map <Integer, Product> _products;

    /** Register of clients */
    private Map <String, Client> _clients;

    /** Register of purchases */
    private Map <Integer, Purchase> _purchases;

    /** Register of transport methods (modalities) */
    private Map <String, TransportMethod> _transportMethods;

    /** Register of split strategies */
    private Map <String, OrderSplit> _orderSplit;

    /** Register of additional services */
    private Map <String, AdditionalService> _services;

    /** Register of product search criterions */
    private Map <String, Search> _productSearch;

    /** Register of product types */
    private Map <String, ProductFactory> _productFactories;

    /** Constructor. */
    Erp(){
        _date = 1;
        _nextCompanyId = 1;
        _nextProductId = 1;
        _nextPurchaseId = 1;
        _companies = new TreeMap<>();   /** Sort by key */
        _products = new TreeMap<>();
        /** Clients are sorted by email */
        _clients = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _purchases = new TreeMap<>();   
        _transportMethods = new LinkedHashMap<>();
        _orderSplit = new LinkedHashMap<>(); 
        _services = new LinkedHashMap<>(); /** Sort by insertion order */

        /** Start with the built-in search chriterions */
        _productSearch = new LinkedHashMap<>();
        registerProductSearch(new OriginSearch());
        registerProductSearch(new NameSearch());
        registerProductSearch(new CategorySearch());

        /** Start with the built-in product types */
        _productFactories = new LinkedHashMap<>();
        registerProductFactory(new NationalProductFactory());
        registerProductFactory(new ImportedProductFactory());
    }

    // --- text file -------------------------------------------------

    /**
     * Reads a file. 
     * @param filename name of the file to import.
     * @throws UnrecognizedEntryException if a file line is invalid.
     * @throws IOException if I/O error happens in the reading.
     */
    void importFile(String filename) throws UnrecognizedEntryException, 
                                            IOException {
        
        List<String> fileLines = Files.readAllLines(Path.of(filename));
        int num_lines = fileLines.size();

        for(int i = 0; i < num_lines; i++){
            parseLine(fileLines.get(i));
        }
    }

    /**
     * Parses a line of a file by parsing each field splitted by ":". 
     * @param line line to parse.
     * @throws UnrecognizedEntryException if the line is invalid.
     */
    private void parseLine(String line) throws UnrecognizedEntryException {

        String[] lineContent;

        line = line.trim();
        lineContent = line.split(":");

        try{
            switch (lineContent[0]) {

                /* Import company to the system */
                case "COMPANY"           ->  parseCompany(lineContent);

                /* Import company product inventory to the system */
                case "SUPPLY"            ->  parseSupply(lineContent);

                /* Import product to the system */
                case "NATIONAL", "IMPORTED" ->  parseProduct(lineContent);

                /* Import client to the system */
                case "CLIENT"            ->  parseClient(lineContent);

                /* Import transport method to the system */
                case "RATE"              ->  parseRate(lineContent);

                /* Invalid line - never happens */
                default ->  throw new UnrecognizedEntryException(line);
            }
        }
        catch (InvalidCompanyNameException | InvalidProductFieldsException |
                UnknownCompanyException | UnknownProductException e){
            /* Never happens because imported files are all valid */
            throw new UnrecognizedEntryException(line, e);
        }
    }

    /**
     * Helps Parsing a split company line. Adds it to the system. 
     * @param lineContent line fields already split.
     * @throws InvalidCompanyNameException if company name is empty.
     */
    private void parseCompany(String[] lineContent) 
                                           /* never happens */
                                throws InvalidCompanyNameException {
        String name = lineContent[1];
        registerCompany(name);
    }

    /**
     * Helps Parsing a company product inventoy line. Adds it to the system. 
     * @param lineContent line fields already split.
     * @throws InvalidProductFieldsException
     */
    private void parseSupply (String[] lineContent) 
            throws UnknownCompanyException, UnknownProductException {
        int companyId = Integer.parseInt(lineContent[1]);
        int productId = Integer.parseInt(lineContent[2]);
        int quantityToChange = Integer.parseInt(lineContent[3]);
        double factor = Double.parseDouble(lineContent[4]);

        changeInventory(companyId, productId, quantityToChange, factor);
    }

    /**
     * Helps Parsing a product line. Adds it to the system. 
     * @param lineContent line fields already split.
     * @throws InvalidProductFieldsException
     */
    private void parseProduct(String[] lineContent)
                                throws InvalidProductFieldsException {
        String type = lineContent[0].toUpperCase();  
        String name = lineContent[1];
        int basePrice = Integer.parseInt(lineContent[2]);
        String Category = lineContent[3];   
        int baseShippingTime = Integer.parseInt(lineContent[4]);
        String[] extraArgs = Arrays.copyOfRange(lineContent, 5, 
                                                lineContent.length);
        registerProduct(type, name, basePrice, Category, 
                        baseShippingTime, extraArgs);
    }

    /**
     * Helps Parsing a client line. Adds it to the system. 
     * @param lineContent line fields already split.
     */
    private void parseClient(String[] lineContent) {
        String name = lineContent[1];
        String email = lineContent[2];
        registerClient(email, name);
    }

    /**
     * Helps Parsing a split transport method line. Adds it to the system. 
     * @param lineContent line fields already split.
     */
    private void parseRate(String[] lineContent) {
        String key = lineContent[1];
        int shippingFee = Integer.parseInt(lineContent[2]);
        int shippingTime = Integer.parseInt(lineContent[3]);
        addTransportMethod(key, shippingFee, shippingTime);
    }

    // --- getters ---------------------------------------------------

    /**
     * @return Current date on the system.
     */
    public int getCurrentDate(){
        return _date;
    }

    /**
     * @param companyId Id of the company to get.
     * @return Company with the given id.
     * @throws UnknownCompanyException if there is no company with given id.
     */
    public Company getCompany(int companyId) throws UnknownCompanyException {

        Company company = _companies.get(companyId);
        if (company == null){
            throw new UnknownCompanyException(companyId);
        }
        return company;
    }

    /**
     * @param productId Id of the product to get.
     * @return Product with the given id.
     * @throws UnknownProductException if there is no product with given id.
     */
    public Product getProduct(int productId) throws UnknownProductException {
        
        Product product = _products.get(productId);
        if (product == null){
            throw new UnknownProductException(productId);
        }
        return product;
    }

    /**
     * @param email Id of the client to get.
     * @return Client with the given id (email).
     * @throws UnknownClientException if there is no client with given id.
     */
    public Client getClient(String email) throws UnknownClientException {

        Client client = _clients.get(email.toLowerCase());
        if (client == null){
            throw new UnknownClientException(email);
        }
        return client;
    }

    /**
     * @param purchaseId Id of the purchase to get.
     * @return Purchase with the given id.
     * @throws UnknownPurchaseException if there is no purchase with 
     * the given id.
     */
    public Purchase getPurchase(int purchaseId) 
                        throws UnknownPurchaseException {

        Purchase purchase = _purchases.get(purchaseId);
        if (purchase == null){
            throw new UnknownPurchaseException(purchaseId);
        }
        return purchase;
    }

    /**
     * Gets or create a basket of a company client.
     * @param email Client's basket email.
     * @param companyId Company that the basket belongs.
     * @return Basket
     * @throws UnknownClientException if there is no client with given id.
     * @throws UnknownCompanyException if there is no company with given id.
     */
    public Basket getBasket(String email, int companyId)
                    throws UnknownClientException, UnknownCompanyException {
        
        /* clients map keys are set to lowercase */
        Client client = getClient(email.toLowerCase());
        Company company = getCompany(companyId);

        Basket basket = client.getBasket(companyId);

        /* create a new basket if client doesnt have one for that company */
        if (basket == null){
            basket = new Basket(client, company);
            client.addBasket(companyId, basket);
        }

        return basket;
    }

    // --- setters ---------------------------------------------------

    /**
     * Advances the system date.
     * @param daysToAdvance
     * @return true if the days to advance are positive. false othersise.
     */
    public boolean advanceDate(int daysToAdvance) {

        if (daysToAdvance <= 0){
            return false;
        }
        else{
            _date += daysToAdvance;
            return true;
        }

        //todo 2nd delivery - notifications
    }

    /**
     * Adds a transport method (modality) to the system.
     * Private because only import file can use it.
     * @param key name of the transport method.
     * @param Fee cost of the transport method.
     * @param Time days this transport method takes to deliver.
     */
    private void addTransportMethod(String key, int Fee, int Time) {
        TransportMethod tm = new TransportMethod(key, Fee, Time);
        _transportMethods.put(key.toLowerCase(), tm);
    }

    // --- companies -------------------------------------------------

    /**
     * Registers a company in the system.
     * @param name name of the company.
     * @return the created company id.
     * @throws  InvalidCompanyNameException if given name is empty.
     */
    public int registerCompany(String name) 
                                throws InvalidCompanyNameException {

        /* problem if name is an empty String */
        if (name.trim().isEmpty()){
            throw new InvalidCompanyNameException();
        }

        Company company = new Company(_nextCompanyId, name);
        _companies.put(_nextCompanyId, company);

        _nextCompanyId++;
        return company.getId(); /* the app needs it for a message */
    }

    /**
     * Returns a string representation of a company.
     * @param companyId id of the company to show.
     * @throws UnknownCompanyException if company doesnt exist.
     * @return the company representation.
     */
    public String showCompany(int companyId) throws UnknownCompanyException{

        Company company = getCompany(companyId); /* throws exception already */
        return company.toString();
    }

    /**
     * Creates a list of strings of all companies in the system.
     * 
     * @return list of strings of the companies.
     */
    public List<String> showAllCompanies(){
        List<String> _companiesString = new ArrayList<>();

        for(Company c: _companies.values()){
            _companiesString.add(c.toString());
        }

        return _companiesString;
    }

    /**
     * Add, decrease or remove inventory of a product.
     * @param companyId id of the company to change inventory.
     * @param productId id of the product to change inventory.
     * @param quantityToChange quantity to add or decrease.
     * @param newRPFactor recommended price factor to change.
     * @return true if it changes the inventory, false otherwise.
     * @throws UnknownCompanyException if company doesnt exist.
     * @throws UnknownProductException if product doenst exist.
     */
    public boolean changeInventory(int companyId, int productId, 
                                    int quantityToChange, double newRPFactor)
                    throws UnknownCompanyException, UnknownProductException {

        Company company = getCompany(companyId); /* throws exception already */
        Product product = getProduct(productId); /* throws exception already */

        if (company.changeInventory(product, quantityToChange, newRPFactor)
                                            != InventoryStatus.SUCCESS) {
            /* ERROR, INVALID_RP_FACTOR, ERROR_ADD_0_UNITS
             * NOT_ENOUGH_TO_REMOVE
             */
            return false;
        }

        return true;
    }

    /**
     * Creates a list of strings of all the products of a company.
     * @param companyID id of the company to show all products.
     * @throws UnknownCompanyException if company doesnt exist.
     * @return list of strings of the products.
     */
    public List<String> showCompanyProducts(int companyID)
                          throws UnknownCompanyException {
        Company company = getCompany(companyID); /* throws exception already */
        List<String> companyProdString = new ArrayList<>();

        for(ProductInventory i: company.getAllInventory().values()) {
            companyProdString.add(companyID + " - " + i.toString());
        }

        return companyProdString;
    }
    
    //todo finished? but 2nd delivery
    /**
     * Creates a list of strings of all the clients of a company.
     * @param companyID id of the company to show all clients.
     * @throws UnknownCompanyException if company doesnt exist.
     * @return list of strings of the clients.
     */
    public List<String> showCompanyClients (int companyID) throws UnknownCompanyException {
        Company company = getCompany(companyID); /* throws exception already */
        List<String> companyClientString = new ArrayList<>();

        for(Client c: company.getAllClients()) {
            companyClientString.add(companyID + " - " + c.toString());
        }

        return companyClientString;
    }

    // --- products --------------------------------------------------

    /**
     * Registers a product factory (type) in the system.
     * @param pf product factory
     */
    private void registerProductFactory(ProductFactory pf){
        _productFactories.put(pf.getKey(), pf);
    }

    /**
     * @param category name of the category to check if exists
     * @return true if category exists, false otherwise.
     */
    private boolean categoryExists(String category){
        for(Category c: Category.values()){
            if (category.equalsIgnoreCase(c.name())){
                return true;
            }
        }
        return false;
    }

    /**
     * Registers a new product in the system.
     * @param type product type 
     * @param name product name
     * @param basePrice product base price
     * @param category product category
     * @param baseShippingTime product base shipping delay
     * @param extraArgs buffer that stores the extra arguments different
     * types like imported needs.
     * @return the new product id.
     * @throws InvalidProductFieldsException if at least one of the
     * given fields is invalid, even the ExtraArgs. 
     */
    public int registerProduct(String type, String name, int basePrice, 
                                String category, int baseShippingTime, 
                                String[] extraArgs) 
                                throws InvalidProductFieldsException {
        
        /* Convert to portuguese, because files handle it in english, 
         * but user types in portuguese */
        if (type.equals("NATIONAL")) {
            type = "NACIONAL";
        }
        else if (type.equals("IMPORTED")) {
            type = "IMPORTADO";
        }
        
        /* Problem if type is not registered in the system */
        if (!(_productFactories.containsKey(type))){
            throw new InvalidProductFieldsException();
            /** should never happen */
        }

        /* Problem if arguments are invalid */
        if (name.trim().isEmpty() | basePrice <= 0 | baseShippingTime <= 0){
            throw new InvalidProductFieldsException();
        }

        /* Problem if category doesnt exist */
        if (!categoryExists(category)){
            throw new InvalidProductFieldsException();
        }

        /* get the factory of the given type */
        ProductFactory pf = _productFactories.get(type);

        /* create the new product*/
        /* if extraArgs are invalid, create throws exception */
        Product product = pf.create(_nextProductId, name, category, 
                                    basePrice, baseShippingTime, 
                                    extraArgs);
        
        _products.put(product.getID(), product);
        _nextProductId++;
        return product.getID(); /* the app needs it */
    }

    /**
     * Returns a string representation of a product.
     * @param productID id of the product to show.
     * @throws UnknownProductException if product doesnt exist.
     * @return the product representation.
     */
    public String showProduct(int productID) throws UnknownProductException {
        Product product = getProduct(productID); /* throws exception already */

        return product.toString();
    }

    /**
     * Creates a list of strings of all products in the system.
     * 
     * @return list of strings representation of the products.
     */
    public List<String> showAllProducts(){
        List<String> _productsString = new ArrayList<>();

        for(Product p: _products.values()){
            _productsString.add(p.toString());
        }

        return _productsString;
    }

    /* Auxiliar  */
    private void registerProductSearch(Search search){
        _productSearch.put(search.getKey(), search);
    }
    
    /**
     * Does a search of products in the system with a given filter 
     * and search term. Creates a list of strings of the products
     * that match the search criterion.
     * @param filter search filter.
     * @param term search term.
     * @throws InvalidSearchFilterException if filter is not found.
     * @return list of representation of products.
     */
    public List<String> search(String filter, String term) 
                      throws InvalidSearchFilterException{
        Search s = _productSearch.get(filter); /* only accepts uppercase */
        if (s == null){
            throw new InvalidSearchFilterException();
        }

        List<String> searchList = new ArrayList<>();

        for (Product p: _products.values()) {
            if(s.search(p, term)) {
                searchList.add(p.toString());
            }
        }

        /* if search list is empty, search invalid */
        if (searchList.isEmpty()){
            throw new InvalidSearchFilterException();
        }

        return searchList; 
    }

    // --- clients ---------------------------------------------------

    /**
     * Registers a new client in the system.
     * @param email client email 
     * @param name client name
     * @return true in case of success, false otherwise.
     */
    public boolean registerClient(String email, String name){

        /* problem if name or email are empty strings */
        if (email.trim().isEmpty() | name.trim().isEmpty()){
            return false;
        }

        /* problem if given email is already registered */
        if(_clients.containsKey(email)){
            return false;
        }

        Client client = new Client(email, name);
        /* clients map keys are set to lowercase */
        _clients.put(email.toLowerCase(), client);
        
        return true;
    }

    /**
     * Returns a string representation of a client.
     * @param email email of the client to show.
     * @throws UnknownClientException if client doesnt exist.
     * @return the client representation.
     */
    public String showClient(String email) throws UnknownClientException{
        Client client = _clients.get(email.toLowerCase());

        if (client == null){
            throw new UnknownClientException(email);
        }

        return client.toString();
    }

    /**
     * Creates a list of strings of all clients in the system.
     * 
     * @return list of strings  representation of clients.
     */
    public List<String> showAllClients(){
        List<String> _clientsString = new ArrayList<>();

        for(Client c: _clients.values()){
            _clientsString.add(c.toString());
        }

        return _clientsString;
    }
    
    //todo show client notifications  2nd delivery

    // --- purchases -------------------------------------------------

    //todo show purchase 2nd delivery

    //todo show all purchases 2nd delivery

    // --- basket ----------------------------------------------------

    /**
     * Change the quantity of a product in basket, or remove it.
     * @param basket client's basket.
     * @param productId id of the product to change quantity.
     * @param quantityToChange
     * @return AddToBasketStatus - What the change did:
     * ADDED, if it resulted in a new product or quanity changed.
     * REMOVED, if the product was removed from the basket.
     * NOT_IN_BASKET, if the product to change is not in basket.
     * NOT_ENOUGH, if company doenst provide the quantity user wants to add.
     * NOT_SUPPLIED, if company doenst provide that product.
     * 
     * @throws UnknownProductException if the product to add doesnt exist
     * @throws ProductNotSuppliedException if company doesnt provide the product.
     */
    public AddToBasketStatus changeProductInBasket(Basket basket, 
                                int productId, int quantityToChange) 
                                throws UnknownProductException, 
                                        ProductNotSuppliedException {

        Product product = getProduct(productId); /** throws exception already */
        
        /* call the function that does all the work */
        AddToBasketStatus msg = basket.addToBasket(product, 
                                                    quantityToChange);
        
        /* the app needs it */
        if(msg == AddToBasketStatus.NOT_SUPPLIED){
            throw new ProductNotSuppliedException();
        }

        return msg;
    }

    /**
     * Returns a string representation of a basket.
     * @param basket 
     * @return the basket representation.
     */
    public String showBasket(Basket basket){
        return basket.toString();
    }

    /**
     * Adds an order split.
     * 
     * @param newOrderSplit.
     */
    public void addOrderSplit(OrderSplit newOrderSplit){
        _orderSplit.put(newOrderSplit.getKey(), newOrderSplit);
    }

    /**
     * Adds a transport method.
     * 
     * @param newTransportMethod
     */
    public void addTransportMethod(TransportMethod newTransportMethod){
        _transportMethods.put(newTransportMethod.getKey(), newTransportMethod);
    }

    /**
     * Adds an additional service.
     * 
     * @param newAdditionalService
     */
    public void addAdditionalService(AdditionalService newAdditionalService){
        _services.put(newAdditionalService.getKey(), newAdditionalService);
    }

    /**
     * Turns the basket to a purchase and splits it in orders.
     * @param basket client's basket.
     * @return the resulting purchase.
     * @throws InsufficientInventoryException if in the moment of the
     * checkout the company had already sold needed inventory.
     */
    public Purchase checkout(Basket basket) 
                                throws InsufficientInventoryException {

        if(basket == null){
            return null; //todo throw dedicated exception
        }

        /* Checkout Status cases:
        * ERROR,
        * CANT_CHECKOUT_EMPTY_BASKET, if basket is empty
        * NO_TRANSPORT_METHOD, a set modality is mandatory.
        * NOT_ENOUGH, if in the moment of the checkout the company had 
        * already sold needed inventory.
        * SUCCESS, the checkout turned into a puchase.
        */

        /* Check if the checkout is possible */
        CheckoutStatus status = basket.canCheckout();

        if (status == CheckoutStatus.NOT_ENOUGH){
            throw new InsufficientInventoryException();
        }

        /* CANT_CHECKOUT_EMPTY_BASKET, NO_TRANSPORT_METHOD */
        if (status != CheckoutStatus.SUCCESS){
            return null;  //todo throw dedicated exception for each case
        }

        /* the function that does all the work */
        /* create the new purchase that results of basket checkout */
        Purchase purchase = basket.checkout(_nextPurchaseId, getCurrentDate());
        _purchases.put(_nextPurchaseId, purchase);
        _nextPurchaseId++;

        return purchase;
    }

    /**
     * Creates a list of strings of product type keys.
     * 
     * @return list of strings of product type keys.
     */
    public List<String> listProductTypeKeys(){
        List<String> productTypeKeys = new ArrayList<>();

        for(Product p: _products.values()){
            productTypeKeys.add(p.getType());
        }

        return productTypeKeys;
    }

    /**
     * Creates a list of strings of category keys.
     * 
     * @return list of strings of category keys.
     */
    public List<String> listCategoryKeys(){
        List<String> categoryKeys = new ArrayList<>();

        for(Product p: _products.values()){
            categoryKeys.add(p.getCategory().name());
        }

        return categoryKeys;
    }

    /**
     * Creates a list of strings of search filter keys.
     * 
     * @return list of strings of search filter keys.
     */
    public List<String> searchFilterKeys(){
        List<String> searchFilterKeys = new ArrayList<>();

        for(Search s: _productSearch.values()){
            searchFilterKeys.add(s.getKey());
        }

        return searchFilterKeys;
    }

    /**
     * Creates a list of strings of order split keys.
     * 
     * @return list of strings of order split keys.
     */
    public List<String> listOrderSplitKeys(){
        List<String> orderSplitKeys = new ArrayList<>();

        for(OrderSplit oS: _orderSplit.values()){
            orderSplitKeys.add(oS.getKey());
        }

        return orderSplitKeys;
    }

    /**
     * Creates a list of strings of transport method keys.
     * 
     * @return list of strings of transport method keys.
     */
    public List<String> listTransportMethodsKeys(){
        List<String> transportKeys = new ArrayList<>();

        for(TransportMethod tM: _transportMethods.values()){
            transportKeys.add(tM.getKey());
        }

        return transportKeys;
    }

    /**
     * Creates a list of strings of additional services keys.
     * 
     * @return list of strings of additional services keys.
     */
    public List<String> listAdditionalServicesKeys(){
        List<String> servicesKeys = new ArrayList<>();

        for(AdditionalService aS: _services.values()){
            servicesKeys.add(aS.getKey());
        }

        return servicesKeys;
    }

}