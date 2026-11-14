package erp.product;

import java.io.Serializable;
import erp.exceptions.InvalidProductException;

public interface ProductFactory extends Serializable{

    @java.io.Serial
    public static final long serialVersionUID = 202608261200L;  //!ver public

    public Product create(int id, String name, String category, int basePrice, 
                            int baseShippingTime, String[] extraArgs) throws InvalidProductException;

    public String getKey();
}
