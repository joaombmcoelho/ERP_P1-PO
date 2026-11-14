package erp.product;

import java.io.Serializable;

                                             /** so it sorts maps by product id */
public abstract class Product implements Serializable, Comparable<Product>{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    protected int _id;
    protected String _name;
    protected Category _category;
    protected int _basePrice;
    protected int _baseShippingTime;
    protected int _totalUnitsAvailable;

    public Product(int id, String name,Category c, int basePrice, int baseShipping){
        _id = id;
        _name = name;
        _category = c;     /** NOT SURE VERIFICAR DEPOIS */
        _basePrice = basePrice;
        _baseShippingTime = baseShipping;
        _totalUnitsAvailable = 0;
    }

    /** GETTERS */

    public int getID(){
        return _id;
    }

    public String getName(){
        return _name;
    }

    public Category getCategory(){
        return _category;
    }

    public int getBasePrice(){
        return _basePrice;
    }

    public int getBaseShippingTime(){
        return _baseShippingTime;
    }

    public int getTotalUnitsAvailable(){
        return _totalUnitsAvailable;
    }


    /** SETTERS */

    public void setID(int newID){
        _id = newID;
    }

    public void setName(String newName){
        _name = newName;
    }

    public void setCategory(Category newCategory){
        _category = newCategory;
    }

    public void setBasePrice(int newBasePrice){
        _basePrice = newBasePrice;
    }

    public void setBaseShippingTime(int newBaseShipping){
        _baseShippingTime = newBaseShipping;
    }

    public void addToTotalUnits(int unitsToAdd){
        _totalUnitsAvailable += unitsToAdd;
    }

    @Override
    public int compareTo(Product otherProduct) {
        return Integer.compare(_id, otherProduct.getID());
    }

    public abstract double getRecommendedPrice();
    public abstract String getType();
    public abstract String getAdditionalInfo();

    //!todo por aqui as equals e tostring . disse te mal :-(
}
