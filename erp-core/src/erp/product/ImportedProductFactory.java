package erp.product;

import erp.exceptions.InvalidProductFieldsException;

/**
 * Implements a factory for creating imported products.
 **/
public class ImportedProductFactory implements ProductFactory{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * Creates a product according to the imported type and returns it.
     * Checks if the extra args are valid.
     * 
     * @param id
     * @param name
     * @param category
     * @param basePrice
     * @param baseShippingTime
     * @param extraArgs country of origin and tax.
     * @return the created product.
     * @throws InvalidProductFieldsException if extraArgs are invalid.
     */
    @Override
    public Product create(int id, String name, String category, int basePrice, 
                            int baseShippingTime, String[] extraArgs) 
                            throws InvalidProductFieldsException {
        
        /* Get the imported products extra args */
        String country = extraArgs[0];
        double tax = Double.parseDouble(extraArgs[1]);

        /* Problem if country is empty */
        if (country.trim().isEmpty()){
            throw new InvalidProductFieldsException();
        }

        /* Problem if tax not positive */
        if (tax <= 0){
            throw new InvalidProductFieldsException();
        }

        Category productCategory = Category.valueOf(category);

        Product product = new ImportedProduct(id, name, productCategory, 
                                                basePrice, baseShippingTime, 
                                                country, tax);
        return product;
    }

    /**
     * @return the name of the factory (type of product).
     */
    @Override
    public String getKey() {
        return "IMPORTADO";
    }
}