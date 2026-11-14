package erp.purchase;

import java.util.ArrayList;
import java.util.List;

import erp.product.Product;

public class DeliveryDateSplit implements OrderSplit {
    
    public double calcMeanProductShippingTime(List <PurchaseLine> purchaseLines){

        int shippingTimeSum = 0;
        int numProducts = 0;
        for(PurchaseLine purchaseLine: purchaseLines){
            Product product = purchaseLine.getProduct();
            shippingTimeSum += product.getBaseShippingTime();
            numProducts ++;
        }

        return (double) shippingTimeSum/numProducts;
    }

    @Override 
    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines){
        
        List<List<PurchaseLine>> splitOrders = new ArrayList<>();

        List<PurchaseLine> firstOrder = new ArrayList<>();
        double mean = calcMeanProductShippingTime(purchaseLines);

        for(PurchaseLine purchaseLine: purchaseLines){
            Product product = purchaseLine.getProduct();
            double productShippingTime = product.getBaseShippingTime();

            /** GROUP ALL ORDERS WITH SHIPPING <= MEAN */
            if(productShippingTime <= mean){
                firstOrder.add(purchaseLine);
            }
            /** CREATE A NEW ORDER FOR EACH LINE WITH SHIPPING > MEAN */
            else{
                List<PurchaseLine> order = new ArrayList<>();
                order.add(purchaseLine);
                splitOrders.add(order);
            }
        }

        /** probabbly never empty */
        if (!firstOrder.isEmpty()) {
            splitOrders.add(0, firstOrder); /** put the fast order first */
        }

        return splitOrders;
    }

    @Override 
    public String getKey(){
        return "PRAZO";
    }
}