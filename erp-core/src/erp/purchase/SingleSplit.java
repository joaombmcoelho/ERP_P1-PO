package erp.purchase;

import java.util.ArrayList;
import java.util.List;

public class SingleSplit implements OrderSplit {
    
    @Override 
    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines){
        
        List<List<PurchaseLine>> splitOrders = new ArrayList<>();
        splitOrders.add(purchaseLines);

        return splitOrders;
    }

    @Override 
    public String getKey(){
        return "UNICA";
    }
}
