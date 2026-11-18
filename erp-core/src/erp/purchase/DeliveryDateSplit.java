package erp.purchase;

import java.util.ArrayList;
import java.util.List;

import erp.product.Product;

/**
 * Implements the Delivery date order split strategy that groups
 * the purchase lines into orders based on their products shipping time.
 */
public class DeliveryDateSplit implements OrderSplit {
    
    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * Calculates the mean of the base shipping time of all products
     * in the purchase line
     * @param purchaseLines list of purchase lines.
     * @return mean of the purchase products base shipping time.
     */
    public double calcMeanProductShippingTime(List <PurchaseLine> listPL){

        int shippingTimeSum = 0;
        int numProducts = 0;

        for(PurchaseLine pL: listPL){
            Product product = pL.getProduct();
            shippingTimeSum += product.getBaseShippingTime();
            numProducts ++;
        }

        return (double) shippingTimeSum/numProducts;
    }

    /**
     * Splits the purchase lines according to delivery date split strategy.
     * 
     * Basically, GROUPS all the products with base shipping time <= mean
     * of all products and creates a NEW order for each product with base
     * shipping time > mean.
     * 
     * @param purchaseLines list of purchase lines.
     * @return list of orders.
     */
    @Override 
    public List<List<PurchaseLine>> split(List <PurchaseLine> listPL){
        
        List<List<PurchaseLine>> splitOrders = new ArrayList<>();

        /* Initialize the order of the group <= mean */
        List<PurchaseLine> firstOrder = new ArrayList<>();
        double mean = calcMeanProductShippingTime(listPL);

        for(PurchaseLine purchaseLine: listPL){
            Product product = purchaseLine.getProduct();
            double productShippingTime = product.getBaseShippingTime();

            /* GROUP ALL ORDERS WITH SHIPPING <= MEAN */
            if(productShippingTime <= mean){
                firstOrder.add(purchaseLine);
            }
            /* CREATE A NEW ORDER FOR EACH LINE WITH SHIPPING > MEAN */
            else{
                List<PurchaseLine> order = new ArrayList<>();
                order.add(purchaseLine);
                splitOrders.add(order);
            }
        }

        /* probabbly never empty */
        if (!firstOrder.isEmpty()) {
            splitOrders.add(0, firstOrder); /* put the fast order first */
        }

        return splitOrders;
    }

    /**
     * @return name of the order split strategy.
     */
    @Override 
    public String getKey(){
        return "PRAZO";
    }
}