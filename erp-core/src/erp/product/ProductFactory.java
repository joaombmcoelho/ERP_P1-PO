package erp.product;

import java.io.Serializable;
import erp.exceptions.InvalidProductFieldsException;

/**
 * Defines a factory interface for creating a product.
 * Work from raw input arguments, extensible for new types of products
 * given the extra Args array that work as a buffer for the arguments
 * that were not expected in the regular type of a product.
 */
public interface ProductFactory extends Serializable{

    /**
     * Creates a product according to the factory type and returns it.
     * 
     * @param id
     * @param name
     * @param category
     * @param basePrice
     * @param baseShippingTime
     * @param extraArgs array of extra fields the type of product can need.
     * @return the created product.
     * 
     * @throws InvalidProductFieldsException if extraArgs are invalid.
     */
    public Product create(int id, String name, String category, int basePrice, 
                            int baseShippingTime, String[] extraArgs) 
                            throws InvalidProductFieldsException;
    /**
     * @return the name of the factory (type of product).
     */
    public String getKey();
}
