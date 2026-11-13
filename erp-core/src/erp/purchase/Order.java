package erp.purchase;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


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
        return _purchaseID + " - " + doTransportDescription() + calcDeliveryDate() + " - " +
               calcProcessingCost() + " - " /** + lista ids prods */;
    }

    public double calcProcessingCost(){
        int acc = 0;

        for (AdditionalService aS: _additionalServices){
            acc += aS.getAdditionalCost();
        }
        return acc + _transportMethod.getShippingFee();
    }

    public int calcDeliveryDate(){
        int acc = 0;

        for (AdditionalService aS: _additionalServices){
            acc += aS.getAdditionalTime();
        }
        return acc + _transportMethod.getShippingTime();
    }

    public String doTransportDescription(){
        String addS = "";

        for (AdditionalService aS: _additionalServices){
            addS += aS.getKey() + " - ";
        }
        return _transportMethod.getKey() + " - " + addS;
    }
    
}