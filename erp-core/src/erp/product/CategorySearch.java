package erp.product;

public class CategorySearch implements Search{

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