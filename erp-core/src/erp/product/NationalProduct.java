package erp.product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import erp.basket.Basket;
import erp.client.Client;

import java.util.List;

// import erp.basket.Basket;
// import erp.client.Client;
// import erp.company.ProductInventory;
// import erp.product.Product;
// import erp.purchase.Purchase;



public class NationalProduct extends Product {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    public NationalProduct(int id, String name,Category category, int basePrice, int baseShippingTime){
        super(id, name, category, basePrice, baseShippingTime);
    }

    //! ver padroes de desenho aqui
    @Override
    public double getRecommendedPrice(){

        /* 100 <= totalUnits <= 10000 */
        double recommendedPrice = getBasePrice();

        if (getTotalUnitsAvailable() > 10000){
            recommendedPrice = (0.90 * getBasePrice());
        }
        else if(getTotalUnitsAvailable() < 100){
            recommendedPrice = (1.30 * getBasePrice());
        }

        return recommendedPrice;
        // todo ver se é para aopresentar 58.5 ou 58.50 onde produtos nacionais enunciado
    }

    @Override 
    public String getType(){
        return "NACIONAL";
    }

    @Override //todo see if it makes sense
    public String getAdditionalInfo(){
        return "";
    }

    //todo put in the main class and see equals
    @Override 
    public String toString(){
        return _id + " - " + getType() + " - "+ _name + " - " + _category + " - " + String.format("%.2f",(double)_basePrice) + " - " +
         String.format("%.2f", getRecommendedPrice()) + " - " + _baseShippingTime + " - " + _totalUnitsAvailable;
    }
}   