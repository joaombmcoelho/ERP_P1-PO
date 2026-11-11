package erp.company;

import java.io.Serializable;

import erp.product.Product;

public class ProductInventory {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private int _unitsAvailable;
    private double _rpFactor;
    private Product _product;

    public ProductInventory(Product product, int units, double rp){
        _product = product;
        _unitsAvailable = units;
        _rpFactor = rp;
    }

    /**  GETTERS */

    public int getUnitsAvailable(){
        return _unitsAvailable;
    }

    public double getRPFactor(){
        return _rpFactor;
    }

    public Product getProduct(){
        return _product;
    }

    /** SETTERS */

    public void addUnits(int quantity){
        _unitsAvailable += quantity;
    }

    public void setRPFactor(double newRPFactor){
        _rpFactor = newRPFactor;
    }

    public void setProduct(Product newProduct){
        _product = newProduct;
    }

   @Override
   public boolean equals(Object o){
        if (o instanceof ProductInventory){
            ProductInventory pi = (ProductInventory) o;
                return(_unitsAvailable.equals(pi.getUnitsAvailable()) &&
                       _rpFactor.equals(pi.getRPFactor()) &&
                       _product.equals(pi.getProduct()));
        }
        return false;
   }

   @Override
   public String toString(){
        return _product.getID()+ " - " + _unitsAvailable + " - " + _rpFactor + " - " + calcSellPrice();
   }

   public double calcSellPrice(){
        return _product.getRecommendedPrice()*_rpFactor;
   }
}