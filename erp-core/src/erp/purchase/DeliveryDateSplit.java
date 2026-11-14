package erp.purchase;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDateSplit implements OrderSplit {
    
    public double calcMean
    @Override 
    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines){
        
        List<List<PurchaseLine>> splitOrders = new ArrayList<>();


        splitOrders.add(purchaseLines);

        return splitOrders;
    }

    @Override 
    public String getKey(){
        return "PRAZO";
    }
}