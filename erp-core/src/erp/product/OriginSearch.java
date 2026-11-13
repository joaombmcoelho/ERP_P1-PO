package erp.product;

public class OriginSearch implements Search{

    @Override 
    public boolean search(Product product, String term){
        
        if (!(product instanceof ImportedProduct)){
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