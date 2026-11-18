package erp.company;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;

import erp.basket.Basket;
import erp.client.Client;
import erp.company.ProductInventory;
import erp.product.Product;
import erp.purchase.Purchase;

/**
 * Class for companies registered in ERP.
 */
public class Company implements Serializable{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** company id */
    private int _id;

    /** company name */
    private String _name;

    /** company inventory */
    private Map <Product, ProductInventory> _inventory;

    /** all purchases clients did with this company */
    private Map <Integer, Purchase> _purchases;
    
    /** all clients that did business with this company */
    private List <Client> _clients;

    /**
     * Constructor.
     * 
     * @param id company id.
     * @param name company name.
     */
    public Company(int id, String name){
        _id = id;
        _name = name;
        _inventory = new TreeMap<>();
        _purchases = new TreeMap<>();
        _clients = new ArrayList<>();
    }

    // --- getters ---------------------------------------------------

    /** company-related getters*/

    /**
     * @return company id.
     */
    public int getId(){
        return _id;
    }

    /**
     * @return company name.
     */
    public String getName(){
        return _name;
    }

    /**
     * @return a copy of the inventory map.
     */
    public Map <Product, ProductInventory> getAllInventory(){
        return Collections.unmodifiableMap(_inventory);
    }

    /**
     * @param product
     * @return the inventory of this product.
     */
    public ProductInventory getInventory(Product product){
        return _inventory.get(product);
    }

    /**
     * @return a copy of the purchases map.
     */
    public Map <Integer, Purchase> getAllPurchases(){
        return Collections.unmodifiableMap(_purchases);
    }
    
    /**
     * @param purchaseId
     * @return the purchase with given id.
     */
    public Purchase getPurchase(int purchaseId){
        return _purchases.get(purchaseId);
    }
    
    /**
     * @return a copy of the clients map.
     */
    public List <Client> getAllClients(){
        return Collections.unmodifiableList(_clients);
    }

    /** inventory-related getters */

    /**
     * @param product
     * @return number of units available of this product.
     */
    public int getProductUnitsAvailable(Product product){
        ProductInventory productInventory = getInventory(product);

        return productInventory.getUnitsAvailable();
    }

    /**
     * @param product
     * @return recommended price factor of this product.
     */
    public double getProductRPFactor(Product product){
        ProductInventory productInventory = getInventory(product);
        
        return productInventory.getRPFactor();
    }

    /**
     * @param product
     * @return sell Price of this product.
     */
    public double getProductSellPrice(Product product){
        ProductInventory productInventory = getInventory(product);
        
        return productInventory.calcSellPrice();
    }

    // --- setters ---------------------------------------------------

    /** company-related setters */

    /**
     * Adds a client to a company.
     * 
     * @param newClient client to add.
     */
    public void addClient(Client newClient){    
        _clients.add(newClient);
    }

    /**
     * Adds a purchase to a company.
     * 
     * @param newPurchase purchase to add.
     */
    public void addPurchase(Purchase newPurchase){ 
        _purchases.put(newPurchase.getID(), newPurchase);
    }

    /** inventory-related setters */

    /**
     * Adds (or removes) a certain ammount of units (stock) to a product.
     * This function is used internally, and in basket, to avoid 
     * changing the RPFactor through the change inventory function.
     * 
     * @param product
     * @param quantityToAdd
     */
    public void addUnits(Product product, int quantityToAdd){
        ProductInventory productInventory = getInventory(product);

        productInventory.addUnits(quantityToAdd);
        /** update product counter of global stock */
        product.addToTotalUnits(quantityToAdd);
    }

    /**
     * Changes the Recommended Price factor of a product.
     * 
     * @param product
     * @param newFactor
     */
    private void changeRPFactor(Product product, double newFactor){
        ProductInventory productInventory = getInventory(product);

        productInventory.setRPFactor(newFactor);
    }
    
    // --- methods ---------------------------------------------------
    
    /**
     * Tells if the company provides a product.
     * 
     * @param product
     * @return true if so, false otherwise.
     */
    public boolean providesProduct(Product product){
        return _inventory.containsKey(product);
    }

    /**
     * Tells if the company has stock of a product.
     * 
     * @param product
     * @return true if so, false otherwise.
     */
    public boolean hasStock(Product product){
        ProductInventory productInventory = getInventory(product);

        if (productInventory.getUnitsAvailable() > 0){
            return true;
        }
        return false;
    }

    /**
     * Add, decrease or remove inventory of a product.
     * @param product product to change inventory.
     * @param quantityToAdd quantity to add or decrease. 
     * @param newRPFactor recommended price factor to change.
     * @return InventoryStatus - message:
     * ERROR, in case of error.
     * INVALID_RP_FACTOR, if recommended price factor is not valid.
     * ERROR_ADD_0_UNITS, company cant provide a new product with zero units.
     * NOT_ENOUGH_TO_REMOVE, if company wants to remove more units that
     * what it has.
     */
    public InventoryStatus changeInventory(Product product, int quantityToAdd, 
                                                double newRPFactor) {

        /* flag that tells if company is adding a new product
         * or just updating its stock and/or RP factor.
        */
        boolean newProduct = true;

        /* check if user wants to add a entire new product or just 
         * change inventory */
        if (providesProduct(product)){
            newProduct = false;
        }

        /* problem if RP factor not positive */
        if(newRPFactor <= 0){
            return InventoryStatus.INVALID_RP_FACTOR;
        }

        /** Case (1) - Add a new product */
        if(newProduct){
            /* problem if quantityToAdd not positive
             * because if its a new product quantity has to be >= 1. 
             */
            if (quantityToAdd <= 0){
                return InventoryStatus.ERROR_ADD_0_UNITS;
            }
            /* Change is valid, put the product in the inventory */
            _inventory.put(product, new ProductInventory(product, 
                                        quantityToAdd, newRPFactor));
            /* Update product counter of global stock */
            product.addToTotalUnits(quantityToAdd);
            return InventoryStatus.SUCCESS; /** SUCCESS! */
        }
        /* Case (2) - Update existing product */
        else {
            int currentInventory = getInventory(product).getUnitsAvailable();
            int resultingQuantity = currentInventory + quantityToAdd; 

            /* problem if user wants to remove more products than 
             * the stock the companiy has.
             */
            if(resultingQuantity < 0){
                return InventoryStatus.NOT_ENOUGH_TO_REMOVE;
            }

            addUnits(product, quantityToAdd);
            changeRPFactor(product, newRPFactor);
            return InventoryStatus.SUCCESS; /** SUCCESS! */
        }
    }

    /**
    * Two companies are considered equal when they have the same id.
    * 
    * @param o company to compare
    * @return true if companies are equals, false otherwise.
    */
    @Override 
    public boolean equals (Object o){
        if (o instanceof Company){
            Company c = (Company) o;
            return (_id == c.getId());
        }
        return false;
    }

    /**
    * String representation of a company: 
    * companyId - companyName
    * 
    * @return a string representation of the company.
    */
    @Override 
    public String toString(){
        return _id + " - " + _name;
    }

}
