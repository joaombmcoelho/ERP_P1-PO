package erp;

import erp.exceptions.*;

//my imports:
import java.io.IOException;
import java.io.Serializable;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import erp.purchase.Purchase;
//import this to use
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.company.InventoryStatus;
import erp.client.Client;
import erp.product.Product;
import erp.product.ProductFactory;
import erp.basket.AddToBasketStatus;
import erp.basket.Basket;
import erp.basket.CheckoutStatus;
import erp.product.Category;
import erp.product.NationalProductFactory;
import erp.product.ImportedProductFactory;

//FIXME maybe import classes

/**
 * The façade of the ERP core: the single entry point through which the
 * application interacts with the domain.
 *
 * <p><b>Façade</b> pattern.</p>
 */
public class ErpManager {

    //! ver como funciona flags de alteracoes dirty/changed no manager

    /** The object doing all the actual work. */
    private Erp _erp = new Erp();
    private String _filename = "";  /** added for save */

    //!FIXME maybe define constructors
    public ErpManager(){
        _erp = new Erp();
        _filename = "";  /** added for save */
    }

    // --- persistence -------------------------------------------------------

    public void save() throws MissingFileAssociationException, IOException {

        if (_filename == null || _filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        /** using try with resources */
        try (ObjectOutputStream oos =   new ObjectOutputStream(
                                        new BufferedOutputStream(
                                        new FileOutputStream(_filename)))){
            oos.writeObject(_erp);
        }
    }

    public void saveAs(String filename) throws MissingFileAssociationException, IOException {
        
        if (filename == null || filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        _filename = filename;
        
        try{
            save();
        } catch (MissingFileAssociationException e){
            e.printStackTrace();
        }
    }

    public void load(String filename) throws UnavailableFileException {
        
        if (filename == null || filename.isEmpty()){
            throw new UnavailableFileException(filename);
        }

        /** using try with resources */
        try (ObjectInputStream ois =    new ObjectInputStream(
                                        new BufferedInputStream(
                                        new FileInputStream(filename)))){
            /** attatch the new filename and erp to the manager */
            _erp = (Erp) ois.readObject();
            _filename = filename;
        }
        catch(IOException | ClassNotFoundException e){
            throw new UnavailableFileException(filename);
        }

    }

    public void importFile(String filename) throws ImportFileException {
        try {
            if (filename != null && !filename.isEmpty())
                _erp.importFile(filename);
        } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
            throw new ImportFileException(filename, e);
        }
    }

    //FIXME implement other methods

    /******************** GETTERS *********************/

    public int getCurrentDate(){
        return _erp.getCurrentDate();
    }

    public Company getCompany(int companyId) throws UnknownCompanyException{
        return _erp.getCompany(companyId);
    }

    public Product getProduct(int productId) throws UnknownProductException{
        return _erp.getProduct(productId);
    }

    public Client getClient(String email) throws UnknownClientException{
        return _erp.getClient(email);
    }

    public Purchase getPurchase(int purchaseId) throws UnknownPurchaseException{
        return _erp.getPurchase(purchaseId);
    }

    public Basket getBasket(String email, int companyId)
                        throws UnknownClientException, UnknownCompanyException{
        return _erp.getBasket(email, companyId);
    }

    public void advanceDate(int daysToAdvance){
        _erp.advanceDate(daysToAdvance);
    }

    /******************** COMPANIES MENU *********************/

    public boolean registerCompany(String name){
        return _erp.registerCompany(name);
    }

    public String showCompany(int companyId) throws UnknownCompanyException{
        return _erp.showCompany(companyId);
    }

    //todo RITA SHOW ALL COMPANIES

    public boolean changeInventory (int company_id, int product_id, 
                                    int quantityToChange, double newRPFactor)
                                    throws UnknownCompanyException, UnknownProductException{
        return _erp.changeInventory(company_id, product_id, quantityToChange, newRPFactor);
    }

    //todo RITA SHOW COMPANY PRODUCTS
    
    //todo RITA SHOW COMPANY CLIENTS

    /******************** PRODUCTS MENU *********************/

    public boolean registerProduct(String type, String name, int basePrice, String category,
                                    int baseShippingTime, String[] extraArgs){
        return _erp.registerProduct(type, name, basePrice, category, baseShippingTime, extraArgs);
    }

    //todo RITA SHOW PRODUCT

    //todo RITA SHOW ALL PRODUCTS

    //todo RITA SEARCH

    /******************** CLIENTS MENU *********************/

    public boolean registerClient(String email, String name){
        return _erp.registerClient(email, name);
    }

    //todo RITA SHOW CLIENT

    //todo RITA SHOW ALL CLIENTS

    //todo RITA SHOW CLIENT NOTIFICATIONS

    /******************** PURCHASE MENU *********************/

    //todo RITA SHOW PURCHASE

    //todo RITA SHOW ALL PURCHASES

    /******************** BASKET MENU *********************/

    public AddToBasketStatus changeProductInBasket(Basket basket, int productId, int quantityToChange)
                                                    throws UnknownProductException{
        return _erp.changeProductInBasket(basket, productId, quantityToChange);
    }

    public String showBasket(Basket basket){
        return _erp.showBasket(basket);
    }

    //todo RITA SET ORDER SPLIT

    //todo RITA SET TRANSPORT METHOD

    //todo RITA ADD ADDITIONAL SERVICE

    /** finalizar compra do enunciado */
    public Purchase checkout(Basket basket) throws InsufficientInventoryException{
        return _erp.checkout(basket);
    }

    //todo RITA todos os lists keys em baixo


}
