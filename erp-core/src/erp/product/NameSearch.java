package erp.product;

/**
 * Implements the name search by matching the product name with 
 * the given term.
 */
public class NameSearch implements Search{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;
    
    /**
     * Tells if a product has the name given in the term.
     * 
     * @param product
     * @param term name to compare.
     * @return true if so, false otherwise.
     */
    @Override 
    public boolean search(Product product, String term){
        
        String productName = product.getName();

        return term.equalsIgnoreCase(productName);
    }

    /**
     * @return name of the search chriterion.
     */
    @Override 
    public String getKey(){
        return "NOME";
    }

}