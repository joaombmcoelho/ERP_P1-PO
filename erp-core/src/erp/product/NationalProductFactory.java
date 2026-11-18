package erp.product;

/**
 * Implements a factory for creating national products.
 **/
public class NationalProductFactory implements ProductFactory{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;
    
    /**
     * Creates a product according to the national type and returns it.
     * 
     * @param id
     * @param name
     * @param category
     * @param basePrice
     * @param baseShippingTime
     * @param extraArgs empty, the national product doenst have extraArgs.
     * @return the created product.
     */
    @Override
    public Product create(int id, String name, String category, int basePrice, 
                    int baseShippingTime, String[] extraArgs){
        
        Category productCategory = Category.valueOf(category);
        
        Product product = new NationalProduct(id, name, productCategory, 
                                                basePrice, baseShippingTime);
        
        return product;
    }

    /**
     * @return the name of the factory (type of product).
     */
    @Override
    public String getKey() {
        return "NACIONAL";
    }
}