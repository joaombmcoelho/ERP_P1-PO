package erp.purchase;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import erp.product.Product;


public class Order implements Serializable{
    
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private int _purchaseID;
    private int _purchaseDate;
    private List<PurchaseLine> _purchaseLines;
    private List<AdditionalService> _additionalServices;
    private TransportMethod _transportMethod;

    public Order(int purchaseID, int purchaseDate){
        _purchaseID = purchaseID;
        _purchaseDate = purchaseDate;
        _purchaseLines = new ArrayList<>();
        _additionalServices = new ArrayList<>();
        _transportMethod = null;
    }

    public Order(int purchaseID, int purchaseDate, List<PurchaseLine> purchaseLines, 
                List<AdditionalService> additionalServices, TransportMethod transportMethod ){
        _purchaseID = purchaseID;
        _purchaseDate = purchaseDate;
        _purchaseLines = purchaseLines;
        _additionalServices = additionalServices;
        _transportMethod = transportMethod;
    }

    /** GETTERS */

    public int getID(){
        return _purchaseID;
    }

    public int getPurchaseDate(){
        return _purchaseDate;
    }

    public List <PurchaseLine> getPurchaseLines(){
        return Collections.unmodifiableList(_purchaseLines);
    }

    public List <AdditionalService> getAdditionalServices(){
        return Collections.unmodifiableList(_additionalServices);
    }

    public TransportMethod getTransportMethod(){
        return _transportMethod;
    }

    /** SETTERS */

    public void setID(int newID){
        _purchaseID = newID;
    }

    public void setPurchaseDate(int newDate){
        _purchaseDate = newDate;
    }

    public void addPurchaseLine(PurchaseLine newLine){    
        _purchaseLines.add(newLine);
    }

    public void addAdditionalService(AdditionalService newService){   
        _additionalServices.add(newService);
    }

    public void setTransportMethod(TransportMethod newTransport){
        _transportMethod = newTransport;
    }

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

    @Override
    public String toString(){
        return _purchaseID + " - " + doTransportDescription() + " - " + calcDeliveryDate() + " - " +
               String.format("%.2f",calcProcessingCost())  + " -" + getProductsIdString();
    }

    /** Calculates the total value of purchase lines in this order */
    public double calcItemsCost(){
        double itemsCost = 0;

        for (PurchaseLine pL: _purchaseLines){
            itemsCost += pL.calcTotalLineCost();
        }
        return  itemsCost;
    }

    /** If the products in the order have a value > 50, processing cost = 0. Otherwise, equals 5. */
    public double calcProcessingCost(){

        double itemsCost = calcItemsCost();

        if (itemsCost() > 50){
            return 0.00;
        }
        else{
            return 5.00;
        }
    }

    /** Calculates processingCost + all additional services cost + transport shippingFee */
    public double calcOrderCost(){
        double orderCost = calcProcessingCost();

        for (AdditionalService aS: _additionalServices){
            orderCost += aS.getAdditionalCost();
        }

        orderCost += _transportMethod.getShippingFee();

        return orderCost;
    }
    
    public double calcTotalCost(){
        return calcOrderCost() + calcItemsCost();
    }

    /** Delivery date = _purchaseDate + biggest base shipping time + transport shipping + addservices time */
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
        
        return _purchaseDate + bigBaseShipping + _transportMethod.getBaseShippingTime() + aSCost;
    }

    public List<Integer> getProductsIdList(){
        List<Integer> productsID = new ArrayList<>();

        for (PurchaseLine pL: _purchaseLines){
           productsID.add(pL.getProduct().getID());
        }

        Collections.sort(productsID);
        return productsID;
    }

    public String getProductsIdString(){
        String productsID = "";

        //! todo-> traço ou nao entre ids
        for (Integer pID: getProductsIdList()){
            productsID += "  " + pID;
        }

        return productsID;
    }

    public String doTransportDescription(){
        List<AdditionalService> addS = _additionalServices.reversed();
        String addS = "";

        for (AdditionalService aS: _additionalServices){
            addS += "+" + aS.getKey();
        }
        return _transportMethod.getKey() + addS;
    }
    
    //! acabar -> erp
    public abstract String getDetailedString();
}