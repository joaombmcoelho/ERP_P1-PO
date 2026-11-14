package erp;

import erp.exceptions.*;
import java.io.IOException;
import java.io.Serializable;

//imported by me
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.HashMap;

import erp.purchase.Purchase;
//import this to use
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.company.InventoryStatus;
import erp.client.Client;
import erp.product.Product;
import erp.product.ProductFactory;
import erp.company.ProductInventory;
import erp.basket.AddToBasketStatus;
import erp.basket.Basket;
import erp.basket.CheckoutStatus;
import erp.product.Category;
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
    //todo RITA OS TEUS ATRIBUTOS

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
        registerCompany(name);
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

        registerProduct(type, name, basePrice, Category, baseShippingTime, extraArgs);
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
        _companies = new HashMap<>();
        _products = new HashMap<>();
        _clients = new HashMap<>();

        //todo RITAAAAAAAA DPS POE AQUI OS ATRIBUTOS Q ACRESCENTASTE EM CIMA
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

    public void advanceDate(int daysToAdvance){
        _date += daysToAdvance;
        // todo se if we raise exception for negative days
    }

    /** just used for import file */
    public void addTransportMethod(String key, int shippingFee, int shippingTime){
        TransportMethod tm = new TransportMethod(key, shippingFee, shippingTime);

        _transportMethods.put(key.toLowerCase(), tm);
    }

    /******************** COMPANIES MENU *********************/

    public boolean registerCompany(String name){

        /** problem if name is an empty String */
        if (name.trim().isEmpty()){
            return false;
        }

        Company company = new Company(_nextCompanyId, name);
        _companies.put(_nextCompanyId, company);

        _nextCompanyId++;
        return true;
    }

    public String showCompany(int companyId) throws UnknownCompanyException{

        Company company = getCompany(companyId);
        
        return company.toString();
    }

    //todo RITA SHOW ALL COMPANIES

    public boolean changeInventory(int company_id, int product_id, int quantityToChange, double newRPFactor)
                    throws UnknownCompanyException, UnknownProductException{

        Company company = getCompany(company_id);
        Product product = getProduct(product_id);

        if (company.changeInventory(product, quantityToChange, newRPFactor) != InventoryStatus.SUCCESS){
            return false;
        }
        return true;
    }

    //todo RITA SHOW COMPANY PRODUCTS
    
    //todo RITA SHOW COMPANY CLIENTS

    /******************** PRODUCTS MENU *********************/

    /** auxiliar */
    public void registerProductFactory(ProductFactory pf){
        _productFactories.put(pf.getKey(), pf);
    }

    // todo convert category into map probabilly
    public boolean categoryExists(String category){
        for(Category c: Category.values()){
            if (category.equalsIgnoreCase(c.name())){
                return true;
            }
        }
        return false;
    }

    public boolean registerProduct(String type, String name, int basePrice, String category,
                                    int baseShippingTime, String[] extraArgs){
        
        /** problem if type is not registered in the system (never happens) */
        if (!(_productFactories.containsKey(type))){
            return false;
        }

        /** problem if arguments are invalid */
        if (name.trim().isEmpty() | basePrice <= 0 | baseShippingTime <= 0){
            return false;
        }

        /** problem if category doesnt exist */
        if (! categoryExists(category)){
            return false;
        }

        ProductFactory pf = _productFactories.get(type);

        try{
            Product product = pf.create(_nextProductId, name, category, basePrice, 
                                        baseShippingTime, extraArgs);
            _products.put(product.getID(), product);
            _nextProductId++;
            return true;
        }
        /** if extraArgs are invalid, verified in the factory */
        catch (InvalidProductException e){
            return false;
        }
    }

    //todo RITA SHOW PRODUCT

    //todo RITA SHOW ALL PRODUCTS

    //todo RITA SEARCH

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

    //todo RITA SHOW ALL CLIENTS

    //todo RITA SHOW CLIENT NOTIFICATIONS

    /******************** PURCHASE MENU *********************/

    //! EFETUAR COMPRA ABRE MENU DE CABAZ VER ESSA FUNCAO

    //todo RITA SHOW PURCHASE

    //todo RITA SHOW ALL PURCHASES

    /******************** BASKET MENU *********************/

    public AddToBasketStatus changeProductInBasket(Basket basket, int productId, int quantityToChange) 
        throws UnknownProductException{

        Product product = getProduct(productId);

        AddToBasketStatus message = basket.addToBasket(product, quantityToChange);
        
        // todo nao esquecer throwar product not supplied na app

        return message;
    }

    public String showBasket(Basket basket){
        return basket.toString();
    }

    //todo RITA SET ORDER SPLIT

    //todo RITA SET TRANSPORT METHOD

    //todo RITA ADD ADDITIONAL SERVICE

    /** finalizar compra do enunciado */
    public boolean checkout(Basket basket){

        if(basket == null || basket.canCheckout() != CheckoutStatus.SUCCESS){
            return false;
        }
        /** create the new purchase that results of basket checkout */
        Purchase purchase = basket.checkout(_nextPurchaseId, getCurrentDate());
        _purchases.put(_nextPurchaseId, purchase);
        _nextPurchaseId++;

        return true;
    }

    //todo RITA todos os lists keys em baixo

}











