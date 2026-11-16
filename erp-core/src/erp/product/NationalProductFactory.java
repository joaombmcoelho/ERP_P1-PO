package erp.product;

public class NationalProductFactory implements ProductFactory{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override
    public Product create(int id, String name, String category, int basePrice, 
                    int baseShippingTime, String[] extraArgs){
        
        //! todo mudar categorias ou apanhar estas escecoes
        Category productCategory = Category.valueOf(category);
        
        Product product = new NationalProduct(id, name, productCategory, 
                                                basePrice, baseShippingTime);
        
        return product;
    }

    @Override
    public String getKey() {
        return "NACIONAL";
    }
}
