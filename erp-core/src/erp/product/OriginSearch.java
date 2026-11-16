package erp.product;

public class OriginSearch implements Search{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override 
    public boolean search(Product product, String term){
        
        if (product.getType() == "NACIONAL"){
            return false;
        }
        /* cast the product to imported product */
        ImportedProduct importedProduct = (ImportedProduct) product;

        String productOrigin = importedProduct.getCountry();

        return term.equalsIgnoreCase(productOrigin);
    }

    @Override 
    public String getKey(){
        return "ORIGEM";
    }
}