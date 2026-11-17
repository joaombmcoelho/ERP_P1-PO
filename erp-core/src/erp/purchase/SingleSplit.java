package erp.purchase;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Single order split strategy gruping all purchase 
 * lines into one order.
 */
public class SingleSplit implements OrderSplit {
    
    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * Splits the purchase lines according to the single split strategy.
     * Basically, groups all the purchase lines into one order.
     * 
     * @param purchaseLines list of purchase lines.
     * @return list of orders (only 1).
     */
    @Override 
    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines){
        
        List<List<PurchaseLine>> splitOrders = new ArrayList<>();
        splitOrders.add(purchaseLines);

        return splitOrders;
    }

    /**
     * @return name of the order split strategy.
     */
    @Override 
    public String getKey(){
        return "UNICA";
    }
}
