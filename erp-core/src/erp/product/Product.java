package erp.product;

import java.io.Serializable;

/**
 * Class for products registered in ERP.
 */
public abstract class Product implements Serializable, Comparable<Product>{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    //! see if its private
    /** Product id */
    protected int _id;  

    /** Product name */
    protected String _name;

    /** Product category */
    protected Category _category;

    /** Product base price */
    protected int _basePrice;

    /** Product base delay */
    protected int _baseShippingTime;

    /** Product total unites available */
    protected int _totalUnitsAvailable;

    /**
     * Constructor.
     * 
     * @param id 
     * @param name 
     * @param c product category.
     * @param basePrice
     * @param baseShipping baseDelay.
     */
    public Product(int id, String name,Category c, int basePrice, int baseShipping){
        _id = id;
        _name = name;
        _category = c;     /** NOT SURE VERIFICAR DEPOIS */
        _basePrice = basePrice;
        _baseShippingTime = baseShipping;
        _totalUnitsAvailable = 0;
    }

    // --- getters --------------------------------------------------

    /**
     * @return product id.
     */
    public int getID(){
        return _id;
    }

    /**
     * @return product name.
     */
    public String getName(){
        return _name;
    }

    /**
     * @return product category.
     */
    public Category getCategory(){
        return _category;
    }

    /**
     * @return product base price.
     */
    public int getBasePrice(){
        return _basePrice;
    }

    /**
     * @return product base delay.
     */
    public int getBaseShippingTime(){
        return _baseShippingTime;
    }

    /**
     * @return product total units available.
     */
    public int getTotalUnitsAvailable(){
        return _totalUnitsAvailable;
    }


    // --- setters --------------------------------------------------

    /**
     * Changes a product's id.
     * 
     * @param newID
     */
    public void setID(int newID){
        _id = newID;
    }

    /**
     * Changes a product's name.
     * 
     * @param newName
     */
    public void setName(String newName){
        _name = newName;
    }

    /**
     * Changes a product's category.
     * 
     * @param newCategory
     */
    public void setCategory(Category newCategory){
        _category = newCategory;
    }

    /**
     * Changes a product's base price.
     * 
     * @param newBasePrice
     */
    public void setBasePrice(int newBasePrice){
        _basePrice = newBasePrice;
    }

    /**
     * Changes a product's base delay.
     * 
     * @param newBaseShipping
     */
    public void setBaseShippingTime(int newBaseShipping){
        _baseShippingTime = newBaseShipping;
    }

    /**
     * Changes a product's total units available:
     * if unitsToAdd > 0, adds;
     * if unitsToAdd < 0, removes.
     * 
     * @param unitsToAdd
     */
    public void addToTotalUnits(int unitsToAdd){
        _totalUnitsAvailable += unitsToAdd;
    }

    /** Compares products by id.
     * 
     * @param otherProduct
     * @return 0 if the ids are the same and a negative (or positive)
     * integer if the id is smaller (or greater).
     */
    @Override
    public int compareTo(Product otherProduct) {
        return Integer.compare(_id, otherProduct.getID());
    }

    /**
     * Calculates the product's recommended price.
     * 
     * @return recommended price
     */
    public abstract double getRecommendedPrice();

    /**
     * @return type of the product
     */
    public abstract String getType();

    /**
     * @return product additional information
     */
    public abstract String getAdditionalInfo();
}
