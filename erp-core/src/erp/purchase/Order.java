package erp.purchase;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import erp.product.Product;

/**
 * Class for orders registered in ERP.
 */
public class Order implements Serializable{
    
    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** purchase id */ 
    private int _purchaseID;

    /** purchase date */
    private int _purchaseDate;

    /** all purchase lines of the purchase */
    private List<PurchaseLine> _purchaseLines;

    /** all additional services of the purchase  */
    private List<AdditionalService> _additionalServices;

    /** purchase transport method */
    private TransportMethod _transportMethod;

    /**
     * Constructor.
     * 
     * @param id purchase id.
     * @param name purchase date.
     */
    public Order(int purchaseID, int purchaseDate){
        _purchaseID = purchaseID;
        _purchaseDate = purchaseDate;
        _purchaseLines = new ArrayList<>();
        _additionalServices = new ArrayList<>();
        _transportMethod = null;
    }

    /**
     * Constructor.
     * 
     * @param id purchase id.
     * @param name purchase date.
     * @param purchaseLines  purchase lines.
     * @param additionalServices purchase additional services.
     * @param transportMethod purchase transport method.
     */
    public Order(int purchaseID, int purchaseDate, List<PurchaseLine> purchaseLines, 
                List<AdditionalService> additionalServices, TransportMethod transportMethod ){
        _purchaseID = purchaseID;
        _purchaseDate = purchaseDate;
        _purchaseLines = purchaseLines;
        _additionalServices = additionalServices;
        _transportMethod = transportMethod;
    }

    // --- getters --------------------------------------------------

    /**
     * @return purchase id.
     */
    public int getID(){
        return _purchaseID;
    }

    /** 
     * @return purchase date .
     */
    public int getPurchaseDate(){
        return _purchaseDate;
    }

    /**
     * @return a copy of the purchase lines list.
     */
    public List <PurchaseLine> getPurchaseLines(){
        return Collections.unmodifiableList(_purchaseLines);
    }

    /**
     * @return a copy of the additional services list.
     */
    public List <AdditionalService> getAdditionalServices(){
        return Collections.unmodifiableList(_additionalServices);
    }

    /**
     * @return purchase transport method.
     */
    public TransportMethod getTransportMethod(){
        return _transportMethod;
    }

    // --- setters --------------------------------------------------

    /**
     * Changes a purchase's id.
     * 
     * @param newID
     */
    public void setID(int newID){
        _purchaseID = newID;
    }

    /**
     * Changes a purchase's date.
     * 
     * @param newDate
     */
    public void setPurchaseDate(int newDate){
        _purchaseDate = newDate;
    }

    /**
     * Adds a purchase line to a purchase.
     * 
     * @param newLine
     */
    public void addPurchaseLine(PurchaseLine newLine){    
        _purchaseLines.add(newLine);
    }

    /**
     * Adds an additional service to a purchase.
     * 
     * @param newService
     */
    public void addAdditionalService(AdditionalService newService){   
        _additionalServices.add(newService);
    }

    /**
     * Changes a purchase's tranport method.
     */
    public void setTransportMethod(TransportMethod newTransport){
        _transportMethod = newTransport;
    }

    // --- methods --------------------------------------------------
    
    /**
    * Two purchases are considered equal when they have the same id,
    * date, purchase lines, additional services and transport
    * methods.
    * 
    * @param o purchase to compare
    * @return true if purchase are equals, false otherwise.
    */
    @Override
    public boolean equals(Object o){
        if(o instanceof Order){
            Order order = (Order) o;
            return (_purchaseID == order.getID() &&
                    _purchaseDate == order.getPurchaseDate() &&
                    _purchaseLines.equals(order.getPurchaseLines()) &&
                    _additionalServices.equals(order.getAdditionalServices()) &&
                    _transportMethod.equals(order.getTransportMethod()));
        }
        return false;
    }

    /**
    * String representation of a purchase: 
    * purchaseID - tranport method and additional services - delivery
    * date - processing cost - list of products id in the purchase.
    * 
    * @return a string representation of the purchase.
    */
    @Override
    public String toString(){
        return _purchaseID + " - " + doTransportDescription() + " - " + calcDeliveryDate() + " - " +
                String.format("%.2f",calcProcessingCost())  + " -" + getProductsIdString();
    }

    /** 
     * Calculates the total value of purchase lines in this order .
     * 
     * @return sum of the cost of the items in a purchase.
     */
    public double calcItemsCost(){
        double itemsCost = 0;

        for (PurchaseLine pL: _purchaseLines){
            itemsCost += pL.calcTotalLineCost();
        }
        return  itemsCost;
    }

    /**
     * Calculates the purchase processing cost. If the products in
     * the order have a value > 50, processing cost = 0. Otherwise,
     * equals 5. 
     * 
     * @return purchase processing cost.
     */
    public double calcProcessingCost(){

        double itemsCost = calcItemsCost();

        if (itemsCost > 50){
            return 0.00;
        }
        else{
            return 5.00;
        }
    }

    /**
     * Calculates an order cost. Order cost = processingCost +
     * all additional services cost + transport shippingFee.
     * 
     * @return order cost.
     */
    public double calcOrderCost(){
        double orderCost = calcProcessingCost();

        for (AdditionalService aS: _additionalServices){
            orderCost += aS.getAdditionalCost();
        }

        orderCost += _transportMethod.getShippingFee();

        return orderCost;
    }
    
    /**
     * Calculates the total cost of a purchase.
     * Total cost = order cost + items cost.
     * 
     * @return total cost.
     */
    public double calcTotalCost(){
        return calcOrderCost() + calcItemsCost();
    }

    /** 
     * Calculates a purchase delivery date.
     * Delivery date = _purchaseDate + biggest base shipping time +
     * transport shipping + addservices time .
     * 
     * @return purchase delivery date.
     */
    public int calcDeliveryDate(){
        int aSCost = 0;
        int bigBaseShipping = 0;

        for (AdditionalService aS: _additionalServices){
            aSCost += aS.getAdditionalTime();
        }

        for (PurchaseLine pL: _purchaseLines){
            if (pL.getProduct().getBaseShippingTime() > bigBaseShipping){
                bigBaseShipping = pL.getProduct().getBaseShippingTime();
            }
        }
        
        return _purchaseDate + bigBaseShipping + _transportMethod.getShippingTime() + aSCost;
    }

    /**
     * Creates a list of the ids of the products in a purchase.
     * 
     * @return a sorted list of the products' ids.
     */
    public List<Integer> getProductsIdList(){
        List<Integer> productsID = new ArrayList<>();

        for (PurchaseLine pL: _purchaseLines){
            productsID.add(pL.getProduct().getID());
        }

        Collections.sort(productsID);
        return productsID;
    }

    /** 
     * Transforms the product ids list into a string.
     * 
     * @return string of products' ids.
     */
    public String getProductsIdString(){
        String productsID = "";

        for (Integer pID: getProductsIdList()){
            productsID += "  " + pID;
        }

        return productsID;
    }

    /** 
     * Creates a purchase transport description: transport method + 
     * the key of all additional services in the purchase.
     * 
     * @return string of the transport description.
     */
    public String doTransportDescription(){
        List<AdditionalService> addSReversed = _additionalServices.reversed();
        String addS = "";

        for (AdditionalService aS: addSReversed){
            addS += "+" + aS.getKey();
        }
        return _transportMethod.getKey() + addS;
    }
    
    //todo not complete 
    public String getDetailedString(){
        return "";
    }
}