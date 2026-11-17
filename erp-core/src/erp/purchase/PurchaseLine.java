package erp.purchase;

import java.io.Serializable;

import erp.product.Product;

/**
 * Class for each line in a purchase.
 */
public class PurchaseLine implements Serializable {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** Product in the line. */
    private Product _product;

    /** Quantity of the product in the line (purchased). */
    private int _quantity;

    /** Sale price locked in the time of the purchase */
    private final double _salePrice;  

    /**
     * Constructor
     * @param product
     * @param quantity
     * @param salePrice
     */
    public PurchaseLine(Product product, int quantity, double salePrice){
        _product = product;
        _quantity = quantity;
        _salePrice = salePrice;
    }

    // --- getters ---------------------------------------------------

    /**
     * @return product in this line.
     */
    public Product getProduct(){
        return _product;
    }

    /**
     * @return quantity of the product in this line.
     */
    public int getQuantity(){
        return _quantity;
    }

    /**
     * @return salePrice locked in the time of the purchase.
     */
    public double getSalePrice(){
        return _salePrice;
    }

    // --- setters ---------------------------------------------------

    /**
     * Change the product in the line.
     * Should never happen.
     * 
     * @param product
     */
    public void setProduct(Product product){
        _product = product;
    }

    /**
     * Change the quantity of the product in the line.
     * 
     * @param quantity
     */
    public void setQuantity(int quantity){
        _quantity = quantity;
    }

    // --- methods ---------------------------------------------------
    
    /**
     * Calculates the cost of the purchase line.
     * 
     * @return the cost of the purchase line.
     */
    public double calcTotalLineCost(){
        return (double) _quantity * _salePrice;
    }

    /**
    * Two purchase lines are considered equal when they have the 
    * same product, quantity and sale price.
    * 
    * @param o purchase line to compare.
    * @return true if the lines are equals, false otherwise.
    */
    @Override 
    public boolean equals (Object o){
        if (o instanceof PurchaseLine){
            PurchaseLine pl = (PurchaseLine) o;
            return (_product.equals(pl.getProduct()) && 
                    _quantity == pl.getQuantity() &&
                    _salePrice == pl.getSalePrice());
        }
        return false;
    }

    /**
    * String representation of a purchase line: 
    * ␣␣ProductId - name - quantity - salePrice - totalCost
    * 
    * @return a string representation of the line.
    */
    @Override 
    public String toString(){
        String string = "";

        string += ("  " + _product.getID() + " - " + _product.getName() 
                    + " - " + _quantity + " - " + String.format("%.2f", _salePrice)
                    + " - " + String.format("%.2f", calcTotalLineCost()));
        
        return string;
    }
}






