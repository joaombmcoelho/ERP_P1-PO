package erp.product;


/**
 * Implements the origin search by matching the product origin with 
 * the given country. Only accepts imported products.
 */
public class OriginSearch implements Search{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * Tells if a product has the origin given in the term.
     * 
     * @param product
     * @param term country to compare.
     * @return true if so, false otherwise or if the type is not imported.
     */
    @Override 
    public boolean search(Product product, String term){
        
        /** Only works with imported products */
        if (product.getType() != "IMPORTADO"){
            return false;
        }

        /* Cast the product to imported product */
        ImportedProduct importedProduct = (ImportedProduct) product;

        String productOrigin = importedProduct.getCountry();

        return term.equalsIgnoreCase(productOrigin);
    }

    /**
     * @return name of the search chriterion.
     */
    @Override 
    public String getKey(){
        return "ORIGEM";
    }

}