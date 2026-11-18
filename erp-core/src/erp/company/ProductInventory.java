package erp.company;

import java.io.Serializable;

import erp.product.Product;

/**
 * Class for the  products inventory of a company registered in ERP.
 */
public class ProductInventory implements Serializable {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** units available of the product */
    private int _unitsAvailable;

    /** multiplicative factor of a product in a company */
    private double _rpFactor;

    /** product */
    private Product _product;

    /**
     * Constructor.
     * 
     * @param product
     * @param units product units.
     * @param rp multiplicative factor.
     */
    public ProductInventory(Product product, int units, double rp){
        _product = product;
        _unitsAvailable = units;
        _rpFactor = rp;
    }

    // --- getters ---------------------------------------------------

    /**
     * @return units available of a product.
     */
    public int getUnitsAvailable(){
        return _unitsAvailable;
    }

    /**
     * @return multiplicative factor of a product in a company.
     */
    public double getRPFactor(){
        return _rpFactor;
    }

    /**
     * @return product.
     */
    public Product getProduct(){
        return _product;
    }

    // --- setters ---------------------------------------------------

    /**
     * Changes the units available of a product.
     * 
     * @param units units to change.
     */
    public void setUnitsAvailable(int units){
        _unitsAvailable = units;
    }

    /**
     * Changes the rp factor of a product in a company.
     * 
     * @param newRPFactor
     */
    public void setRPFactor(double newRPFactor){
        _rpFactor = newRPFactor;
    }

    /**
     * Changes a product.
     * 
     * @param newProduct.
     */
    public void setProduct(Product newProduct){
        _product = newProduct;
    }

    /**
     * Two products in the inventory of a company are considered
     * equal when they have the same units available, the same 
     * rp factor and are the same product.
     *
     * @param o product inventory to compare
     * @return true if product inventory are equals, false otherwise.
     */
    @Override
    public boolean equals(Object o){
        if (o instanceof ProductInventory){
            ProductInventory pi = (ProductInventory) o;
                return(_unitsAvailable == pi.getUnitsAvailable() &&
                        _rpFactor ==pi.getRPFactor() &&
                        _product.equals(pi.getProduct()));
        }
        return false;
    }

    /**
    * String representation of a product inventory:
    * productId - units available - rp factor - sell price.
    * 
    * @return a string representation of the product inventory.
    */
    @Override
    public String toString(){
        return _product.getID()+ " - " + _unitsAvailable + " - " 
               + _rpFactor + " - "
               + String.format("%.2f", calcSellPrice());
    }

    // --- methods --------------------------------------------------

    /**
     * Adds units of a product in product inventory.
     * 
     * @param quantity units to add.
     */
    public void addUnits(int quantity){
        _unitsAvailable += quantity;
    }

    /** 
     * Calculates the rounded value of sell price of the product in
     * the company.
     * 
     * @return product sell price in the company.
     */
    public double calcSellPrice(){

        /* sell price is rounded to two decimal places */
        return Math.round(_product.getRecommendedPrice()*_rpFactor * 100)/100.0;  
    }

}