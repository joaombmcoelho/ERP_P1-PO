package erp.product;

/**
 * Class for imported products registered in the ERP system.
 */
public class ImportedProduct extends Product{
    
    /** Serial number for serialization.  */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** Imported product country. */
    private String _country;

    /** Imported product TAX. */
    private double _TAX;

    /**
     * Constructor.
     * 
     * @param id 
     * @param name 
     * @param c product category.
     * @param basePrice
     * @param baseShipping baseDelay.
     * @param country product country.
     * @param tax product TAX.
     */
    public ImportedProduct(int id, String name,Category c,
                           int basePrice, int baseShipping,
                           String country, double tax){
        super(id, name, c, basePrice, baseShipping);
        _country = country;
        _TAX = tax;
    }

    // --- getters --------------------------------------------------

    /**
     * @return imported product country.
     */
    public String getCountry(){
        return _country;
    }

    /**
     * @return imported product TAX.
     */
    public double getTAX(){
        return _TAX;
    }

    // --- setters --------------------------------------------------

    /**
     * Changes the country of an imported product.
     * 
     * @param newCountry
     */
    public void setCountry(String newCountry){
        _country = newCountry;
    }

    /**
     * Changes the TAX of an imported product.
     * 
     * @param newTAX
     */
    public void setTAX(double newTAX){
        _TAX = newTAX;
    }

    // --- methods --------------------------------------------------

    /**
    * Two imported products are considered equal when they have the 
    * same id, name, category, basePrice, baseShippingTime, country 
    * and same TAX.
    * 
    * @param o imported product to compare
    * @return true if products are equals, false otherwise.
    */
    @Override
    public boolean equals(Object o){
        if (o instanceof ImportedProduct){
            ImportedProduct p = (ImportedProduct) o;
            return (_id == p.getID() &&
                    _name.equals(p.getName()) &&
                    _category.equals(p.getCategory()) &&
                    _basePrice == p.getBasePrice() &&
                    _baseShippingTime == p.getBaseShippingTime() &&
                    _country.equals(p.getCountry()) &&
                    _TAX == p.getTAX() &&
                    getType().equals("IMPORTADO"));
        }
        return false;
    }

    /**
    * String representation of an imported product: 
    * id - type - name - category - basePrice - baseDelay - totalUnits
    * - additional info (country and TAX).
    * 
    * @return a string representation of the imported product.
    */
    @Override 
    public String toString(){
        return _id + " - " + getType() + " - "+ _name + " - " +
        _category + " - " + String.format("%.2f",(double)_basePrice) 
        + " - " + String.format("%.2f", getRecommendedPrice()) + " - "
        + _baseShippingTime + " - " + _totalUnitsAvailable + " - " ~
        + getAdditionalInfo() ;
    }

    /**
     * Calculates the product reccomended price: 
     * (basePrice*(100+TAX))/100.
     * 
     * @return the calculated recommended price.
     */
    @Override
    public double getRecommendedPrice(){
        return (_basePrice * (100 + _TAX))/100;  
    }

    /**
     * @return type of the product.
     */
    @Override 
    public String getType(){
        return "IMPORTADO";
    }

    /**
     * Concatenates the product's country and TAX  into a String .
     * 
     * @return product additional information.
     */
    @Override
    public String getAdditionalInfo(){
        return _country + " - " +  _TAX;
    }
}