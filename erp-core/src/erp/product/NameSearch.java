package erp.product;

public class NameSearch implements Search{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

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