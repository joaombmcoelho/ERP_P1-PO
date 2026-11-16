package erp.product;

public class CategorySearch implements Search{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override 
    public boolean search(Product product, String term){
        
        Category productCategory = product.getCategory();

        return term.equalsIgnoreCase(productCategory.name());
    }

    @Override 
    public String getKey(){ 
        return "CATEGORIA";
    }
}