package erp.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import erp.basket.Basket;
import erp.client.Client;
import erp.company.ProductInventory;
import erp.product.Product;
import erp.purchase.Purchase;

// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;


public class Company implements Serializable{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private int _id;
    private String _name;
    private Map <Product, ProductInventory> _inventory;
    private Map <Integer, Purchase> _purchases; //map of purchases with purchase id
    private List <Client> _clients;

    public Company(int id, String name){
        _id = id;
        _name = name;
        _inventory = new HashMap<>();
        _purchases = new HashMap<>();
        _clients = new ArrayList<>();
    }

    /** GETTERS */

    /* company-related */

    public int getId(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public Map <Product, ProductInventory> getAllInventory(){
        return Collections.unmodifiableMap(_inventory);
    }

    public ProductInventory getInventory(Product product){
        return _inventory.get(product);
    }

    public Map <Integer, Purchase> getAllPurchases(){
        return Collections.unmodifiableMap(_purchases);
    }

    public Purchase getPurchase(int purchase_id){
        return _purchases.get(purchase_id);
    }

    public List <Client> getAllClients(){
        return Collections.unmodifiableList(_clients);
    }

    /* inventory-related */

    public int getProductUnitsAvailable(Product product){
        ProductInventory productInventory = getInventory(product);

        return productInventory.getUnitsAvailable();
    }

    public double getProductRPFactor(Product product){
        ProductInventory productInventory = getInventory(product);
        
        return productInventory.getRPFactor();
    }

    public double getProductSellPrice(Product product){
        ProductInventory productInventory = getInventory(product);
        
        return productInventory.calcSellPrice();
    }

    /** SETTERS */

    /* company-related */

    public void addClient(Client newClient){    //todo see if we need status
        _clients.add(newClient);
    }

    public void addPurchase(Purchase newPurchase){ //todo see if we need status
        _purchases.put(newPurchase.getID(), newPurchase);
    }

    /* inventory-related */

    public void addUnits(Product product, int quantityToAdd){
        ProductInventory productInventory = getInventory(product);

        productInventory.addUnits(quantityToAdd);
        product.addToTotalUnits(quantityToAdd);
    }

    private void changeRPFactor(Product product, double newFactor){
        ProductInventory productInventory = getInventory(product);

        productInventory.setRPFactor(newFactor);
    }

    @Override 
    public boolean equals (Object o){
        if (o instanceof Company){
            Company c = (Company) o;
            return (_id == c.getId() && 
                    _name.equals(c.getName()) &&
                    _inventory.equals(c.getAllInventory()) &&
                    _purchases.equals(c.getAllPurchases()) &&
                    _clients.equals(c.getAllClients()));
        }
        return false;
    }

    @Override 
    public String toString(){
        return _id + " - " + _name;
    }

    public boolean offersProduct(Product product){
        return _inventory.containsKey(product);
    }

    public boolean hasStock(Product product){
        ProductInventory productInventory = getInventory(product);

        if (productInventory.getUnitsAvailable() > 0){
            return true;
        }
        return false;
    }

    public InventoryStatus changeInventory(Product product, int quantityToAdd, double newRPFactor){

        boolean newProduct = true;
        // check if user wants to add a entire new product or just change inventory
        if (offersProduct(product)){
            newProduct = false;
        }

        // problem if RP factor <= 0
        if(newRPFactor <= 0){
            return InventoryStatus.INVALID_RP_FACTOR;
        }

        // add a new product
        if(newProduct){
            // problem if quantity To Add not positive
            if (quantityToAdd <= 0){
                return InventoryStatus.ERROR_ADD_0_UNITS;
            }
            _inventory.put(product, new ProductInventory(product, quantityToAdd, newRPFactor));
            product.addToTotalUnits(quantityToAdd);
            return InventoryStatus.SUCCESS;
        }

        int resultingQuantity = getInventory(product).getUnitsAvailable() + quantityToAdd;

        // problem if user wants to remove more products than inventory
        if(resultingQuantity < 0){
            return InventoryStatus.NOT_ENOUGH_TO_REMOVE;
        }

        addUnits(product, quantityToAdd);
        changeRPFactor(product, newRPFactor);
        return InventoryStatus.SUCCESS;
    }
}
