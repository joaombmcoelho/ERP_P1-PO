package erp;

import erp.exceptions.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.List;

import erp.purchase.Purchase;
import erp.purchase.TransportMethod;
import erp.purchase.OrderSplit;
import erp.purchase.AdditionalService;
import erp.company.Company;
import erp.client.Client;
import erp.product.Product;
import erp.basket.AddToBasketStatus;
import erp.basket.Basket;

/**
 * The façade of the ERP core: the single entry point through which the
 * application interacts with the domain.
 * Façade pattern.
 */
public class ErpManager {

    /** The object doing all the actual work. */
    private Erp _erp = new Erp();

    /** Filename of the file to save, or the file that was loaded */
    private String _filename = "";  /** for files functions */

    /** Flag that stores if there are unsaved changes */
    private boolean _fileChanged = false;

    /** Constructor. */
    public ErpManager(){
        /** EMPTY */
    }

    // --- persistence -----------------------------------------------

    /**
     * Saves the current state of the system into associated file. 
     * @throws MissingFileAssociationException if there is no file
     * associated with the system yet.
     * @throws IOException if I/O error happens in the writing.
     */
    public void save() throws MissingFileAssociationException, IOException {

        if (_filename == null || _filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        /* using try with resources */
        try (ObjectOutputStream oos =   new ObjectOutputStream(
                                        new BufferedOutputStream(
                                        new FileOutputStream(_filename)))){
            oos.writeObject(_erp);  /** save */
        }
        _fileChanged = false;  
    }

    /**
     * Saves the current state of the system into the file with
     * the given filename.
     * @param filename name of the destination file.
     * @throws MissingFileAssociationException if the name is invalid.
     * @throws IOException if I/O error happens in the writing.
     */
    public void saveAs(String filename) 
                throws MissingFileAssociationException, IOException {
        
        /* empty filenames are invalid */
        if (filename == null || filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        _filename = filename;
        save();

        _fileChanged = false;
    }

    /**
     * Opens a file and associates it to the system.
     * @param filename name of the file to open.
     * @throws UnavailableFileException if the file user wants to open
     * doesnt exist.
     */
    public void load(String filename) throws UnavailableFileException {
        
        /* empty filenames dont exist */
        if (filename == null || filename.isEmpty()){
            throw new UnavailableFileException(filename);
        }

        /* using try with resources */
        try (ObjectInputStream ois =    new ObjectInputStream(
                                        new BufferedInputStream(
                                        new FileInputStream(filename)))){
            /* attatch the new filename and erp to the manager */
            _erp = (Erp) ois.readObject();
            _filename = filename;
        }
        /* if error or file doenst exist */
        catch(IOException | ClassNotFoundException e){
            throw new UnavailableFileException(filename);
        }

        _fileChanged = false;
    }

    /**
     * Imports a file with ERP data before program starts.
     * @param filename name of the file to import.
     * @throws ImportFileException if coundnt import the file.
     */
    public void importFile(String filename) throws ImportFileException {
        try {
            if (filename != null && !filename.isEmpty())
                _erp.importFile(filename);
        } catch (IOException | UnrecognizedEntryException e) {
            throw new ImportFileException(filename, e);
        }

        _fileChanged = true;
    }
    
    // --- getters ---------------------------------------------------

    /**
     * @return Current date on the system.
     */
    public int getCurrentDate(){
        return _erp.getCurrentDate();
    }

    /**
     * @param companyId Id of the company to get.
     * @return Company with the given id.
     * @throws UnknownCompanyException if there is no company with given id.
     */
    public Company getCompany(int companyId) throws UnknownCompanyException{
        return _erp.getCompany(companyId);
    }

    /**
     * @param productId Id of the product to get.
     * @return Product with the given id.
     * @throws UnknownProductException if there is no product with given id.
     */
    public Product getProduct(int productId) throws UnknownProductException{
        return _erp.getProduct(productId);
    }

    /**
     * @param email Id of the client to get.
     * @return Client with the given id (email).
     * @throws UnknownClientException if there is no client with given id.
     */
    public Client getClient(String email) throws UnknownClientException{
        return _erp.getClient(email);
    }

    /**
     * @param purchaseId Id of the purchase to get.
     * @return Purchase with the given id.
     * @throws UnknownPurchaseException if there is no purchase with 
     * the given id.
     */
    public Purchase getPurchase(int purchaseId) throws UnknownPurchaseException{
        return _erp.getPurchase(purchaseId);
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
                        throws UnknownClientException, UnknownCompanyException{
        return _erp.getBasket(email, companyId);
    }

    /**
     * Tells if the system has unsaved changes.
     * @return true if so. false othersise.
     */
    public boolean hasUnsavedChanges(){
        return _fileChanged;
    }

    // --- setters ---------------------------------------------------

    /**
     * Advances the system date.
     * @param daysToAdvance
     * @return true if the days to advance are positive. false othersise.
     */
    public boolean advanceDate(int daysToAdvance){
        boolean status = _erp.advanceDate(daysToAdvance);
        
        if (status){
            _fileChanged = true;
        }
        return status;
    }

    // --- setters ---------------------------------------------------

    /**
     * Registers a company in the system.
     * @param name name of the company.
     * @return the created company id.
     * @throws  InvalidCompanyNameException if given name is empty.
     */
    public int registerCompany(String name) 
                                throws InvalidCompanyNameException {
        /* already throws exception */
        int companyId = _erp.registerCompany(name);

        _fileChanged = true;
        return companyId;
    }

    /**
     * Returns a string representation of a company.
     * @param companyId id of the company to show.
     * @throws UnknownCompanyException if company doesnt exist.
     * @return the company representation.
     */
    public String showCompany(int companyId) throws UnknownCompanyException{
        return _erp.showCompany(companyId);
    }

    //todo RITA SHOW ALL COMPANIES
    public List<String> showAllCompanies(){
        return _erp.showAllCompanies();
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
    public boolean changeInventory (int companyId, int productId, 
                                    int quantityToChange, double newRPFactor)
                    throws UnknownCompanyException, UnknownProductException {
        boolean status =  _erp.changeInventory(companyId, productId, 
                                                quantityToChange, newRPFactor);
        
        if(status){
            _fileChanged = true;
        }
        return status;
    }

    //todo RITA SHOW COMPANY PRODUCTS
    public List<String> showCompanyProducts(int companyID) throws UnknownCompanyException {
        return _erp.showCompanyProducts(companyID);
    }
    
    //todo RITA SHOW COMPANY CLIENTS
    public List<String> showCompanyClients(int companyID) throws UnknownCompanyException {
        return _erp.showCompanyClients(companyID);
    }

    // --- products --------------------------------------------------

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
        
        /* already throws exception */
        int productId = _erp.registerProduct(type, name, basePrice, 
                                                category, baseShippingTime, 
                                                extraArgs);
        _fileChanged = true;
        return productId;
    }

    //todo RITA SHOW PRODUCT
    public String showProduct(int productID) throws UnknownProductException {
        return _erp.showProduct(productID);
    }

    //todo RITA SHOW ALL PRODUCTS
    public List<String> showAllProducts(){
        return _erp.showAllProducts();
    }

    //todo RITA SEARCH
    public List<String> search(String filter, String term) throws InvalidSearchFilterException {
        return _erp.search(filter, term);
    }

    // --- clients ---------------------------------------------------

    /**
     * Registers a new client in the system.
     * @param email client email 
     * @param name client name
     * @return true in case of success, false otherwise.
     */
    public boolean registerClient(String email, String name) {
        boolean status = _erp.registerClient(email, name);

        if(status){
            _fileChanged = true;
        }
        return status;
    }

    //todo RITA SHOW CLIENT
    public String showClient(String email) throws UnknownClientException {
        return _erp.showClient(email);
    }

    //todo RITA SHOW ALL CLIENTS
    public List<String> showAllClients(){
        return _erp.showAllClients();
    }

    //todo RITA SHOW CLIENT NOTIFICATIONS
    public String showClientNotifications(){
        return "";
    }

    // --- purchases -------------------------------------------------

    //todo RITA SHOW PURCHASE
    public String showPurchase(int purchaseID) throws UnknownPurchaseException {
        return "";
    }

    //todo RITA SHOW ALL PURCHASES
    public String showAllPurchases(){
        return "";
    }

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
        AddToBasketStatus msg = _erp.changeProductInBasket(basket, 
                                            productId, quantityToChange);
        
        if(msg == AddToBasketStatus.ADDED | msg == AddToBasketStatus.REMOVED){
            _fileChanged = true;
        }

        return msg;
    }

    /**
     * Returns a string representation of a basket.
     * @param basket 
     * @return the basket representation.
     */
    public String showBasket(Basket basket){
        return _erp.showBasket(basket);
    }

    //todo RITA SET ORDER SPLIT
    public void addOrderSplit(OrderSplit newOrderSplit){
        _erp.addOrderSplit(newOrderSplit);
    }

    //todo RITA SET TRANSPORT METHOD
    public void addTransportMethod(TransportMethod newTransport){
        _erp.addTransportMethod(newTransport);
    }

    //todo RITA ADD ADDITIONAL SERVICE
    public void addAdditionalService(AdditionalService newService){
        _erp.addAdditionalService(newService);
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
        Purchase purchase = _erp.checkout(basket);

        /** only mark change file if passed exception */
        _fileChanged = true;
        return purchase;
    }

    //todo RITA todos os lists keys em baixo
    public List<String> listProductTypeKeys(){
        return _erp.listProductTypeKeys();
    }

    public List<String> listCategoryKeys(){
        return _erp.listCategoryKeys();
    }

    public List<String> searchFilterKeys(){
        return _erp.searchFilterKeys();
    }

    public List<String> listOrderSplitKeys(){
        return _erp.listOrderSplitKeys();
    }

    public List<String> listTransportMethodsKeys(){
        return _erp.listTransportMethodsKeys();
    }

    public List<String> listAdditionalServicesKeys(){
        return _erp.listAdditionalServicesKeys();
    }

}
