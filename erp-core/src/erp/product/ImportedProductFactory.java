package erp.product;

import erp.exceptions.InvalidProductException;

public class ImportedProductFactory implements ProductFactory{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override
    public Product create(int id, String name, String category, int basePrice, 
                    int baseShippingTime, String[] extraArgs) throws InvalidProductException{
        
        /** get the imported products extra args */
        String country = extraArgs[0];
        double tax = Double.parseDouble(extraArgs[1]);

        /** problem if country is empty */
        if (country.trim().isEmpty()){
            throw new InvalidProductException();
        }

        /** problem if tax not positive */
        if (tax <= 0){
            throw new InvalidProductException();
        }

        Category productCategory = Category.valueOf(category);

        Product product = new ImportedProduct(id, name, productCategory, basePrice, baseShippingTime, country, tax);
        
        return product;
    }

    @Override
    public String getKey() {
        return "IMPORTED";
    }
}