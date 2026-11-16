package erp.purchase;

import java.util.ArrayList;
import java.util.List;

public class SingleSplit implements OrderSplit {
    
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

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
