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

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    //FIXME maybe define attributes
    //FIXME maybe implement constructor
    //FIXME maybe implement methods

    private int _date;
    private int _nextCompanyId;
    private int _nextProductId;
    private int _nextPurchaseId;
    private Map <Integer, Company> _companies;
    private Map <Integer, Product> _products;
    private Map <String, Client> _clients;  //! AO PROCURAR E INSERIR POR email.toLowerCase()

    private Map <Integer, Purchase> _purchases;
    private Map <String, TransportMethod> _transportMethods;
    // todo added
    private Map <String, OrderSplit> _orderSplit;
    private Map <String, AdditionalService> _services;
    private Map <String, Search> _productSearch;

    private Map <String, ProductFactory> _productFactories;

    // --- text file import --------------------------------------------------

    void importFile(String filename) throws UnrecognizedEntryException, IOException
                       /*TODO  FIXME maybe other exceptions */ {
        
        List<String> fileLines = Files.readAllLines(Path.of(filename));
        int num_lines = fileLines.size();

        for(int i = 0; i < num_lines; i++){
            parseLine(fileLines.get(i));
        }
    }

    private void parseLine(String line) throws UnrecognizedEntryException {

        String[] lineContent;

        line = line.trim();
        lineContent = line.split(":");

        try{
            switch (lineContent[0]) {
                case "RATE"     ->  parseRate(lineContent);
                case "COMPANY"  ->  parseCompany(lineContent);
                case "CLIENT"   ->  parseClient(lineContent);
                case "NATIONAL", "IMPORTED" ->  parseProduct(lineContent);
                case "SUPPLY"   ->  parseSupply(lineContent);
                default         ->  throw new UnrecognizedEntryException(line);
            }
        }
        catch (Exception e) {   //TODO SEE WHAT EXCEPTION
            throw new UnrecognizedEntryException(line, e);
        }
    }

    private void parseRate(String[] lineContent){
        String key = lineContent[1];
        int shippingFee = Integer.parseInt(lineContent[2]);
        int shippingTime = Integer.parseInt(lineContent[3]);
        addTransportMethod(key, shippingFee, shippingTime);
    }

    private void parseCompany(String[] lineContent){
        String name = lineContent[1];
        try {
            registerCompany(name);
        }
        catch (InvalidCompanyNameException e){
            /** never happens in files, ignore */
        }
    }

    private void parseClient(String[] lineContent){
        String name = lineContent[1];
        String email = lineContent[2];
        registerClient(email, name);
    }

    private void parseProduct(String[] lineContent){
        String type = lineContent[0].toUpperCase();  
        String name = lineContent[1];
        int basePrice = Integer.parseInt(lineContent[2]);
        String Category = lineContent[3];   //!category eh string???
        int baseShippingTime = Integer.parseInt(lineContent[4]);
        String[] extraArgs = Arrays.copyOfRange(lineContent, 5, lineContent.length);

        try{
            registerProduct(type, name, basePrice, Category, baseShippingTime, extraArgs);
        }
        catch (InvalidProductFieldsException e){
            /** never happens in files, ignore */
        }
    }
                                                            /** never happens */    /** never happens */
    private void parseSupply (String[] lineContent) throws UnknownCompanyException, UnknownProductException{
        int companyId = Integer.parseInt(lineContent[1]);
        int productId = Integer.parseInt(lineContent[2]);
        int quantityToChange = Integer.parseInt(lineContent[3]);
        double factor = Double.parseDouble(lineContent[4]);

        changeInventory(companyId, productId, quantityToChange, factor);
    }

    /** constructor */

    Erp(){
        _date = 1;
        _nextCompanyId = 1;
        _nextProductId = 1;
        _nextPurchaseId = 1;
        _companies = new TreeMap<>();
        _products = new TreeMap<>();
        _clients = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        _purchases = new TreeMap<>();   /** sort by key */
        _transportMethods = new LinkedHashMap<>();/** Sort by insertion order */

        //todo added: not sure product search TreeMap or LinkedHash
        _orderSplit = new LinkedHashMap<>();
        _services = new LinkedHashMap<>();

        _productSearch = new LinkedHashMap<>();
        registerProductSearch(new OriginSearch());
        registerProductSearch(new NameSearch());
        registerProductSearch(new CategorySearch());

        _productFactories = new LinkedHashMap<>();
        registerProductFactory(new NationalProductFactory());
        registerProductFactory(new ImportedProductFactory());
    }

    /******************** GETTERS *********************/

    public int getCurrentDate(){
        return _date;
    }

    public Company getCompany(int companyId) throws UnknownCompanyException{

        Company company = _companies.get(companyId);
        if (company == null){
            throw new UnknownCompanyException(companyId);
        }
        return company;
    }

    public Product getProduct(int productId) throws UnknownProductException{
        
        Product product = _products.get(productId);
        if (product == null){
            throw new UnknownProductException(productId);
        }
        return product;
    }

    public Client getClient(String email) throws UnknownClientException{

        Client client = _clients.get(email.toLowerCase());
        if (client == null){
            throw new UnknownClientException(email);
        }
        return client;
    }

    public Purchase getPurchase(int purchaseId) throws UnknownPurchaseException{

        Purchase purchase = _purchases.get(purchaseId);
        if (purchase == null){
            throw new UnknownPurchaseException(purchaseId);
        }
        return purchase;
    }

    public Basket getBasket(String email, int companyId)
                    throws UnknownClientException, UnknownCompanyException{
        
        Client client = getClient(email.toLowerCase());
        Company company = getCompany(companyId);

        Basket basket = client.getBasket(companyId);

        /** create a new basket if client doesnt have one for that company */
        if (basket == null){
            basket = new Basket(client, company);
            client.addBasket(companyId, basket);
        }

        return basket;
    }

    /******************** SETTERS *********************/

    public boolean advanceDate(int daysToAdvance){

        if (daysToAdvance <= 0){
            return false;
        }
        else{
            _date += daysToAdvance;
            return true;
        }
    }

    /** just used for import file */
    private void addTransportMethod(String key, int shippingFee, int shippingTime){
        TransportMethod tm = new TransportMethod(key, shippingFee, shippingTime);

        _transportMethods.put(key.toLowerCase(), tm);
    }

    /******************** COMPANIES MENU *********************/

    public int registerCompany(String name) throws InvalidCompanyNameException{

        /** problem if name is an empty String */
        if (name.trim().isEmpty()){
            throw new InvalidCompanyNameException();
        }

        Company company = new Company(_nextCompanyId, name);
        _companies.put(_nextCompanyId, company);

        _nextCompanyId++;
        return company.getId(); /** the app needs it for a message */
    }

    public String showCompany(int companyId) throws UnknownCompanyException{

        Company company = getCompany(companyId); /** throws exception already */
        
        return company.toString();
    }

    //todo RITA SHOW ALL COMPANIES
    public List<String> showAllCompanies(){
        List<String> _companiesString = new ArrayList<>();

        for(Company c: _companies.values()){
            _companiesString.add(c.toString());
        }

        return _companiesString;
    }


    public boolean changeInventory(int companyId, int productId, int quantityToChange, double newRPFactor)
                    throws UnknownCompanyException, UnknownProductException{

        Company company = getCompany(companyId); /** throws exception already */
        Product product = getProduct(productId); /** throws exception already */

        if (company.changeInventory(product, quantityToChange, newRPFactor) != InventoryStatus.SUCCESS){
            return false;
        }
        return true;
    }

    //! SHOW COMPANY PRODDUCTS todo not sure verificar!!!!!!
    public List<String> showCompanyProducts(int companyID) throws UnknownCompanyException {
        Company company = getCompany(companyID);
        List<String> companyProdString = new ArrayList<>();

        if (company == null){
            throw new UnknownCompanyException(companyID);
        }

        for(ProductInventory i: company.getAllInventory().values()) {
            companyProdString.add(companyID + " - " + i.toString());
        }

        return companyProdString;
    }
    
    //todo RITA SHOW COMPANY CLIENTS (n necessario na entrega)
    public List<String> showCompanyClients (int companyID) throws UnknownCompanyException {
        Company company = getCompany(companyID);
        List<String> companyClientString = new ArrayList<>();

        if (company == null){
            throw new UnknownCompanyException(companyID);
        }

        for(Client c: company.getAllClients()) {
            companyClientString.add(companyID + " - " + c.toString());
        }

        return companyClientString;
    }

    /******************** PRODUCTS MENU *********************/

    /** auxiliar */
    private void registerProductFactory(ProductFactory pf){
        _productFactories.put(pf.getKey(), pf);
    }

    // todo convert category into map probabilly
    private boolean categoryExists(String category){
        for(Category c: Category.values()){
            if (category.equalsIgnoreCase(c.name())){
                return true;
            }
        }
        return false;
    }

    public int registerProduct(String type, String name, int basePrice, String category,
                                int baseShippingTime, String[] extraArgs) throws InvalidProductFieldsException {
        
        /** convert to portuguese, because files handle it in english, but user dont */
        if (type.equals("NATIONAL")) {
            type = "NACIONAL";
        }
        else if (type.equals("IMPORTED")) {
            type = "IMPORTADO";
        }
        
        /** problem if type is not registered in the system (never happens) */
        if (!(_productFactories.containsKey(type))){
            throw new InvalidProductFieldsException();
        }

        /** problem if arguments are invalid */
        if (name.trim().isEmpty() | basePrice <= 0 | baseShippingTime <= 0){
            throw new InvalidProductFieldsException();
        }

        /** problem if category doesnt exist */
        if (!categoryExists(category)){
            throw new InvalidProductFieldsException();
        }

        ProductFactory pf = _productFactories.get(type);

        try{
            Product product = pf.create(_nextProductId, name, category, basePrice, 
                                        baseShippingTime, extraArgs);
            _products.put(product.getID(), product);
            _nextProductId++;
            return product.getID();
        }
        /** if extraArgs are invalid, verified in the factory */
        catch (InvalidProductException e){
            throw new InvalidProductFieldsException();
        }
    }

    //todo RITA SHOW PRODUCT
    public String showProduct(int productID) throws UnknownProductException{
        Product product = getProduct(productID);

        if (product == null){
            throw new UnknownProductException(productID);
        }
        return product.toString();
    }

    //todo RITA SHOW ALL PRODUCTS
    public List<String> showAllProducts(){
        List<String> _productsString = new ArrayList<>();

        for(Product p: _products.values()){
            _productsString.add(p.toString());
        }

        return _productsString;
    }

    /** Auxiliar  */
    private void registerProductSearch(Search search){
        _productSearch.put(search.getKey(), search);
    }

    //todo SEARCH
    public List<String> search(String filter, String term) throws InvalidSearchFilterException{
        Search s = _productSearch.get(filter/**.toUpperCase()*/);   //public test only accepts uppercase

        if (s == null){
            throw new InvalidSearchFilterException();
        }
        List<String> searchList = new ArrayList<>();

        for (Product p: _products.values()) {
            if(s.search(p, term)) {
                searchList.add(p.toString());
            }
        }

        if (searchList.isEmpty()){
            throw new InvalidSearchFilterException();
        }

        return searchList;
        
    }

    /******************** CLIENTS MENU *********************/

    public boolean registerClient(String email, String name){

        /** problem if name or email are empty Strings */
        if (email.trim().isEmpty() | name.trim().isEmpty()){
            return false;
        }

        /** problem if given email is already registered */
        if(_clients.containsKey(email)){
            return false;
        }

        Client client = new Client(email, name);
        _clients.put(email.toLowerCase(), client);
        
        return true;
    }

    //todo RITA SHOW CLIENT 
    public String showClient(String email) throws UnknownClientException{
        Client client = _clients.get(email.toLowerCase());

        if (client == null){
            throw new UnknownClientException(email);
        }

        return client.toString();
    }

    //todo RITA SHOW ALL CLIENTS
    public List<String> showAllClients(){
        List<String> _clientsString = new ArrayList<>();

        for(Client c: _clients.values()){
            _clientsString.add(c.toString());
        }

        return _clientsString;
    }
    

    //todo RITA SHOW CLIENT NOTIFICATIONS   /** FAZ NO FIM - NAO OBRIGATORIO NESTA ENTREGA  */

    /******************** PURCHASE MENU *********************/

    //todo RITA SHOW PURCHASE   /** FAZ NO FIM - NAO OBRIGATORIO NESTA ENTREGA  */

    //todo RITA SHOW ALL PURCHASES  /** FAZ NO FIM - NAO OBRIGATORIO NESTA ENTREGA  */

    /******************** BASKET MENU *********************/

    public AddToBasketStatus changeProductInBasket(Basket basket, int productId, int quantityToChange) 
        throws UnknownProductException, ProductNotSuppliedException{

        Product product = getProduct(productId); /** throws exception already */

        AddToBasketStatus message = basket.addToBasket(product, quantityToChange);
        
        if(message == AddToBasketStatus.NOT_SUPPLIED){
            throw new ProductNotSuppliedException();
        }

        return message;
    }

    public String showBasket(Basket basket){
        return basket.toString();
    }

    //todo RITA SET ORDER SPLIT
    public void addOrderSplit(OrderSplit newOrderSplit){
        _orderSplit.put(newOrderSplit.getKey(), newOrderSplit);
    }

    //todo RITA SET TRANSPORT METHOD
    public void addTransportMethod(TransportMethod newTransportMethod){
        _transportMethods.put(newTransportMethod.getKey(), newTransportMethod);
    }

    //todo RITA ADD ADDITIONAL SERVICE
    public void addAdditionalService(AdditionalService newAdditionalService){
        _services.put(newAdditionalService.getKey(), newAdditionalService);
    }

    /** finalizar compra do enunciado */
    public Purchase checkout(Basket basket) throws InsufficientInventoryException{

        if(basket == null){
            return null;
        }

        CheckoutStatus status = basket.canCheckout();
        /** NOT_ENOUGH_INVENTORY */
        if (status == CheckoutStatus.NOT_ENOUGH){
            throw new InsufficientInventoryException();
        }

        /** CANT_CHECKOUT_EMPTY_BASKET, NO_TRANSPORT_METHOD */
        if (status != CheckoutStatus.SUCCESS){
            return null;  
        }

        /** create the new purchase that results of basket checkout */
        Purchase purchase = basket.checkout(_nextPurchaseId, getCurrentDate());
        _purchases.put(_nextPurchaseId, purchase);
        _nextPurchaseId++;

        return purchase;
    }

    //todo RITA todos os lists keys em baixo
    public List<String> listProductTypeKeys(){
        List<String> productTypeKeys = new ArrayList<>();

        for(Product p: _products.values()){
            productTypeKeys.add(p.getType());
        }

        return productTypeKeys;
    }

    public List<String> listCategoryKeys(){
        List<String> categoryKeys = new ArrayList<>();

        for(Product p: _products.values()){
            categoryKeys.add(p.getCategory().name());
        }

        return categoryKeys;
    }

    public List<String> searchFilterKeys(){
        List<String> searchFilterKeys = new ArrayList<>();

        for(Search s: _productSearch.values()){
            searchFilterKeys.add(s.getKey());
        }

        return searchFilterKeys;
    }

    public List<String> listOrderSplitKeys(){
        List<String> orderSplitKeys = new ArrayList<>();

        for(OrderSplit oS: _orderSplit.values()){
            orderSplitKeys.add(oS.getKey());
        }

        return orderSplitKeys;
    }

    public List<String> listTransportMethodsKeys(){
        List<String> transportKeys = new ArrayList<>();

        for(TransportMethod tM: _transportMethods.values()){
            transportKeys.add(tM.getKey());
        }

        return transportKeys;
    }

    public List<String> listAdditionalServicesKeys(){
        List<String> servicesKeys = new ArrayList<>();

        for(AdditionalService aS: _services.values()){
            servicesKeys.add(aS.getKey());
        }

        return servicesKeys;
    }
}