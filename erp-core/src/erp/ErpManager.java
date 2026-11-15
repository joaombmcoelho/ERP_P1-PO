package erp;

import erp.exceptions.*;

//my imports:
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import erp.purchase.Purchase;
//import this to use
import erp.company.Company;
import erp.client.Client;
import erp.product.Product;
import erp.basket.AddToBasketStatus;
import erp.basket.Basket;
//FIXME maybe import classes

/**
 * The façade of the ERP core: the single entry point through which the
 * application interacts with the domain.
 *
 * <p><b>Façade</b> pattern.</p>
 */
public class ErpManager {

    private boolean _fileChanged = false;

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
        _fileChanged = false;
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

        _fileChanged = false;
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

        _fileChanged = false;
    }

    public void importFile(String filename) throws ImportFileException {
        try {
            if (filename != null && !filename.isEmpty())
                _erp.importFile(filename);
        } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
            throw new ImportFileException(filename, e);
        }

        _fileChanged = false;
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

    public boolean advanceDate(int daysToAdvance){
        _erp.advanceDate(daysToAdvance);
        _fileChanged = true;
        //! ver post it
        return true;    //! -< mudar
    }

    public boolean hasUnsavedChanges(){
        return _fileChanged;
    }

    /******************** COMPANIES MENU *********************/

    public boolean registerCompany(String name){
        boolean status = _erp.registerCompany(name);
        
        if(status){
            _fileChanged = true;
        }
        return status;
    }

    public String showCompany(int companyId) throws UnknownCompanyException{
        return _erp.showCompany(companyId);
    }

    //todo RITA SHOW ALL COMPANIES

    public boolean changeInventory (int companyId, int productId, 
                                    int quantityToChange, double newRPFactor)
                                    throws UnknownCompanyException, UnknownProductException{
        boolean status =  _erp.changeInventory(companyId, productId, quantityToChange, newRPFactor);
        
        if(status){
            _fileChanged = true;
        }
        return status;
    }

    //todo RITA SHOW COMPANY PRODUCTS
    
    //todo RITA SHOW COMPANY CLIENTS

    /******************** PRODUCTS MENU *********************/

    public boolean registerProduct(String type, String name, int basePrice, String category,
                                    int baseShippingTime, String[] extraArgs){
        boolean status = _erp.registerProduct(type, name, basePrice, category, baseShippingTime, extraArgs);

        if(status){
            _fileChanged = true;
        }
        return status;
    }

    //todo RITA SHOW PRODUCT

    //todo RITA SHOW ALL PRODUCTS

    //todo RITA SEARCH

    /******************** CLIENTS MENU *********************/

    public boolean registerClient(String email, String name){
        boolean status = _erp.registerClient(email, name);

        if(status){
            _fileChanged = true;
        }
        return status;
    }

    //todo RITA SHOW CLIENT

    //todo RITA SHOW ALL CLIENTS

    //todo RITA SHOW CLIENT NOTIFICATIONS

    /******************** PURCHASE MENU *********************/

    //todo RITA SHOW PURCHASE

    //todo RITA SHOW ALL PURCHASES

    /******************** BASKET MENU *********************/

    //! passar esta logica apra o erp e throwar excecao e ver file changed aqui dps
    public AddToBasketStatus changeProductInBasket(Basket basket, int productId, int quantityToChange)
                                                    throws UnknownProductException{
        AddToBasketStatus message = _erp.changeProductInBasket(basket, productId, quantityToChange);
        
        if(message == AddToBasketStatus.ADDED | message == AddToBasketStatus.REMOVED){
            _fileChanged = true;
        }

        return message;
    }

    public String showBasket(Basket basket){
        return _erp.showBasket(basket);
    }

    //todo RITA SET ORDER SPLIT

    //todo RITA SET TRANSPORT METHOD

    //todo RITA ADD ADDITIONAL SERVICE

    /** finalizar compra do enunciado */
    public Purchase checkout(Basket basket) throws InsufficientInventoryException {
        Purchase purchase = _erp.checkout(basket);

        /** only mark change file if passed exception */
        _fileChanged = true;
        return purchase;
    }

    //todo RITA todos os lists keys em baixo


}
