package erp.purchase;

import java.io.Serializable;
// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Collections;
// import java.util.Map;

import erp.product.Product;


public class PurchaseLine implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private Product _product;
    private int _quantity;
    private final double _salePrice;    // Sale Price doesn't change mid-purchase


    public PurchaseLine(Product product, int quantity, double salePrice){
        _product = product;
        _quantity = quantity;
        _salePrice = salePrice;
    }

    /** GETTERS */

    public Product getProduct(){
        return _product;
    }

    public int getQuantity(){
        return _quantity;
    }

    public double getSalePrice(){
        return _salePrice;
    }

    /** SETTERS */

    public void setProduct(Product product){
        _product = product;
    }

    public void setQuantity(int quantity){
        _quantity = quantity;
    }

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

    @Override 
    public String toString(){
        String string = "";

        string += ("  " + _product.getID() + " - " + _product.getName() + " - " + _quantity + " - "
                        + String.format("%.2f", _salePrice) + " - " + String.format("%.2f", calcTotalLineCost()));
        
        return string;
    }

    public double calcTotalLineCost(){
        return (double) _quantity * _salePrice;
    }
}




