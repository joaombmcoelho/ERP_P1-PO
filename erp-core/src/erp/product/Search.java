package erp.product;

import java.io.Serializable;

import erp.product.Product;

/**
 * Defines a search method by matching the product with the given
 * search term.
 */
public interface Search extends Serializable{

    /**
     * Tells if a product is compatible with the search term.
     * 
     * @param product product to match.
     * @param term term to match.
     * @return true if so, false otherwise.
     */
    public boolean search(Product product, String term);

    /**
     * @return name of the search chriterion.
     */
    public String getKey();
}