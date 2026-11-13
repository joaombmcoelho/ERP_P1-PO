package erp.product;

public class NameSearch implements Search{

    @Override 
    public boolean search(Product product, String term){
        
        String productName = product.getName();

        return term.equalsIgnoreCase(productName);
    }

    @Override 
    public String getKey(){
        return "NOME";
    }
}