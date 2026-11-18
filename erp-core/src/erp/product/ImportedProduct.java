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

    /**
     * Tells if a product is imported from the given country.
     * 
     * @param country country to compare
     * @return true if so, false otherwise.
     */
    public boolean isImportedFrom(String country){
        return this.getCountry().equalsIgnoreCase(country);
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

    // --- methods ---------------------------------------------------

    /**
     * Calculates the product reccomended price: 
     * (basePrice*(100+TAX))/100.
     * 
     * @return the calculated recommended price.
     */
    @Override
    public double getRecommendedPrice(){
        return (getBasePrice() * (100 + _TAX))/100;  
    }

    /**
     * @return type of the product.
     */
    @Override 
    public String getType(){
        return "IMPORTADO";
    }

    /**
     * Concatenates the product's country and TAX  into a String.
     * Imported product Additional info: - country - tax;
     * 
     * @return product additional information.
     */
    @Override
    public String getAdditionalInfo(){
        return " - " + _country + " - " +  _TAX;
    }
}