package erp.purchase;

import java.io.Serializable;
import java.util.List;

/**
 * Defines a order split strategy dividing a list of purchase lines into
 * a list of lists of purchase lines (list of orders).
 */
public interface OrderSplit extends Serializable{

    /**
     * Splits the purchase lines according to the order split strategy.
     * @param purchaseLines list of purchase lines.
     * @return list of orders.
     */
    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines);

    /**
     * @return name of the order split strategy.
     */
    public String getKey();

}