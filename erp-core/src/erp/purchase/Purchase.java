package erp.purchase;

import java.io.Serializable;

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import erp.client.Client;
import erp.purchase.OrderSplit;
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.purchase.AdditionalService;
import erp.product.Product;
import erp.purchase.PurchaseLine;
import erp.purchase.Purchase;


public class Purchase implements Serializable{
    
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private int _id;
    private Client _client;
    private Company _company;
    private int _purchaseDate;
    private List<PurchaseLine> _purchaseLines;
    private List<Order> _orders;

    //! todo 
    public Purchase(Client client, Company company, List<PurchaseLine> purchaseLines){
        _client = client;
        _company = company;
        _purchaseLines = purchaseLines;
        _orders = new ArrayList<>();
    }

    public Purchase(int id, int purchaseDate,Client client, Company company, List<PurchaseLine> purchaseLines){
        _id = id;
        _purchaseDate = purchaseDate;
        _client = client;
        _company = company;
        _purchaseLines = purchaseLines;
        _orders = new ArrayList<>();
        
    }

    /** GETTERS */

    public int getID(){
        return _id;
    }

    public int getPurchaseDate(){
        return _purchaseDate;
    }

    public Client getClient(){
        return _client;
    }

    public Company getCompany(){
        return _company;
    }

    public List <PurchaseLine> getPurchaseLines(){
        return Collections.unmodifiableList(_purchaseLines);
    }

    public List <Order> getAllOrders(){
        return Collections.unmodifiableList(_orders);
    }

    /** SETTERS */
    // Check if are all needed

    public void setID(int newID){
        _id = newID;
    }

    public void setPurchaseDate(int newDate){
        _purchaseDate = newDate;
    }

    public void setClient(Client newClient){
        _client = newClient;
    }

    public void setCompany(Company newCompany){
        _company = newCompany;
    }

    public void addPurchaseLine(PurchaseLine newLine){    
        _purchaseLines.add(newLine);
    }

    public void addOrder(Order newOrder){   
        _orders.add(newOrder);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Purchase){
            Purchase p = (Purchase) o;
            return (_id == p.getID() &&
                    _purchaseDate == p.getPurchaseDate() &&
                    _purchaseLines.equals(p.getPurchaseLines()));
        }
        return false;
    }

    @Override
    public String toString(){
        return _id + " - " + _client.getEmail() + " - " + _company.getId() + " - " +
               String.format("%.2f", calcItemsCost()) + " - " + 
               String.format("%.2f", calcOrdersCost()) + " - " + 
               String.format("%.2f", calcTotalCost());
    }

   public double calcItemsCost(){
        double itemsCost = 0;

        for (Order ord: _orders){
            itemsCost += ord.calcItemsCost();
        }

        return itemsCost;
   }

   public double calcOrdersCost(){
        double ordersCost = 0;

        for (Order ord: _orders){
            ordersCost += ord.calcOrderCost();
        }

        return ordersCost;
   }

   public double calcTotalCost(){
        double totalCost = 0;

        for (Order ord: _orders){
            totalCost += ord.calcTotalCost();
        }

        return totalCost;
   }

    //! acabar -> erp
   public List<Orders> getSortedOrders(){
        List<Order> sortedOrders = _orders;

        return sortedOrders;
   }

   public abstract String toDetailedString();
}