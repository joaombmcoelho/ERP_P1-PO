package erp.product;

/**
 * Implements the category search by matching the product category with 
 * the given category.
 */
public class CategorySearch implements Search{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

     /**
     * Tells if a product has the category given in the term.
     * 
     * @param product product to match.
     * @param term category to match.
     * @return true if so, false otherwise.
     */
    @Override 
    public boolean search(Product product, String term){
        
        Category productCategory = product.getCategory();

        return term.equalsIgnoreCase(productCategory.name());
    }

    /**
     * @return name of the search chriterion.
     */
    @Override 
    public String getKey(){ 
        return "CATEGORIA";
    }
}