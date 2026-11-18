package erp.product;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import erp.basket.Basket;
import erp.client.Client;

/**
 * Class for national products registered in the ERP system.
 * Calculates the recommended price through the global stock of the 
 * product in the whole system.
 */
public class NationalProduct extends Product {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * Constructor.
     * @param id
     * @param name
     * @param category
     * @param basePrice
     * @param baseShippingTime base delay.
     */
    public NationalProduct(int id, String name,Category category, 
                            int basePrice, int baseShippingTime) {
        super(id, name, category, basePrice, baseShippingTime);
    }

    /**
     * Calculates the product recommended price according with:
     * (1) If the global stock is > 10 000: price = 90% base price.
     * (2) If the global stock is < 100: price = 130% base price.
     * (3) Otherwise: recommended price = base price.
     * 
     * @return the calculated recommended price.
     */
    @Override
    public double getRecommendedPrice(){

        double recommendedPrice;

        /* (1) TotalUnits > 10 000 */
        if (getTotalUnitsAvailable() > 10000) {
            recommendedPrice = (0.90 * getBasePrice());
        }
        /* (2) TotalUnits < 100 */
        else if(getTotalUnitsAvailable() < 100) {
            recommendedPrice = (1.30 * getBasePrice());
        }
        /* (3) 100 <= totalUnits <= 10000 */
        else {
            recommendedPrice = getBasePrice();
        }

        return recommendedPrice;
    }

    /**
     * @return type of the product.
     */
    @Override 
    public String getType(){
        return "NACIONAL";
    }

    /**
     * @return the additional info (national products dont have that).
     */
    @Override
    public String getAdditionalInfo(){
        return "";
    }

}   