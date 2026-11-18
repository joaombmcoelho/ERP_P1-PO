package erp.basket;

import java.io.Serializable;

import java.util.Map;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;

import erp.client.Client;
import erp.purchase.OrderSplit;
import erp.purchase.SingleSplit;
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.purchase.AdditionalService;
import erp.purchase.Order;
import erp.product.Product;
import erp.purchase.PurchaseLine;
import erp.purchase.Purchase;

/**
 * A client's shopping basket at one company: what it intends to buy, and how it
 * wants the purchase shipped.
 *
 * The basket menu is opened over a basket, so this class is the
 * receiver of every command in erp.app.basket.
 */
public class Basket implements Serializable {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** Client of this basket */
    private Client _client;

    /** Company of this basket */
    private Company _company;

    /** Split strategy associated with this basket */
    private OrderSplit _orderSplit;

    /** Modality associated with this basket*/
    private TransportMethod _transportMethod;

    /** All the addiitonal services associated with this basket*/
    private List<AdditionalService> _additionalServices;
    
    /** Basket lines each: <Product, QuantityToBuy> */
    private Map<Product, Integer> _basketLines; 

    /**
     * Constructor.
     * @param client
     * @param company
     * @param orderSplit
     * @param defaultSplit
     * @param transportMethod
     * @param additionalServices
     */
    public Basket (Client client, Company company, OrderSplit orderSplit, 
                    TransportMethod transportMethod, 
                    List<AdditionalService> additionalServices) {                   
        
        _client = client;
        _company = company;
        _orderSplit = orderSplit;
        _transportMethod = transportMethod;
        _additionalServices = additionalServices;
        _basketLines = new TreeMap<>(); /* sorts automatically by product id */
    }

    /**
     * Constructor.
     * @param client
     * @param company
     */
    public Basket (Client client, Company company){                   
        
        _client = client;
        _company = company;
        _orderSplit = new SingleSplit(); /* the default split */
        _transportMethod = null; /* a basket starts with no modality */
        _additionalServices = new ArrayList<>();
        _basketLines = new TreeMap<>();
    }

    // --- getters ---------------------------------------------------

    /**
     * @return Client associated with this basket.
     */
    public Client getClient(){
        return _client;
    }

    /**
     * @return Company associated with this basket.
     */
    public Company getCompany(){
        return _company;
    }

    /**
     * @return Split strategy associated with this basket.
     */
    public OrderSplit getOrderSplit(){
        return _orderSplit;
    }

    /**
     * @return Modality associated with this basket.
     */
    public TransportMethod getTransportMethod(){
        return _transportMethod;
    }

    /**
     * @return Copy of the list of Addiitonal Services this basket has.
     */
    public List<AdditionalService> getAdditionalServices(){
        return Collections.unmodifiableList(_additionalServices);
    }

    /**
     * @return Copy of all the basket lines.
     */
    public Map<Product, Integer> getAllBasketLines() {
        return Collections.unmodifiableMap(_basketLines);
    }

    /**
     * Gets the transport description of a basket that consists in:
     * TRANSPORT_METHOD+ADDITIONAL_SERVICES.
     * 
     * @return the string that represents the transport description.
     */
    public String getTransportDescription(){

        TransportMethod tm = getTransportMethod();

        /* (1) Build the transport method part of the description */
        String tmString = "";
        if (tm == null){
            /* if there is no transport method set yet, print "SEM TRANSPORTE" */
            tmString = "SEM TRANSPORTE";
            return tmString;
        }
        else{
            tmString = tm.getKey();
        }

        /* (2) build additional services part of the description */

        /* reverse the additional services from last to first in a copy*/
        //todo check if it needs to be sorted like in the orders.
        List<AdditionalService> addServicesSorted = _additionalServices.reversed();
        
        String addServicesString = "";
        for (AdditionalService aS: addServicesSorted){
            addServicesString += "+" + aS.getKey();
        }
        return tmString + addServicesString;
    }

    /**
     * Get the total cost of a line in the basket.
     * 
     * @param product
     * @param quantity
     * @return the total cost, calculated with sell price.
     */
    public double getTotalLineCost(Product product, int quantity){
        return (_company.getProductSellPrice(product) * quantity);
    }

    /**
     * Get the total cost of all the lines in the basket.
     * @return the total cost, calculated with sell price.
     */
    public double getAllLinesTotalCost(){

        double totalCost = 0;
        
        for(Map.Entry<Product, Integer> entry : _basketLines.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            totalCost += getTotalLineCost(product, quantity);
        }
        return totalCost;
    }

    // --- setters ---------------------------------------------------

    /**
     * Set or change the order split strategy of the basket.
     * 
     * @param orderSplit split strategy.
     */
    public void setOrderSplit(OrderSplit orderSplit){
        _orderSplit = orderSplit;
    }

    /**
     * Set or change the modality of the basket.
     * 
     * @param transportMethod modality.
     */
    public void setTransportMethod(TransportMethod transportMethod){
        _transportMethod = transportMethod;
    }

    /**
     * Add an additional service to the basket.
     * 
     * @param additionalService
     */
    public void addAdditionalService(AdditionalService additionalService){
        _additionalServices.add(additionalService);
    }

    // --- basket methods --------------------------------------------

    /**
     * Tells if a basket is empty or not.
     * 
     * @return true if so, false otherwise.
     */
    public boolean isEmpty(){
        return _basketLines.isEmpty();
    }
    
    /**
     * Tells if a basket has a set transport method already.
     * 
     * @return true if so, false otherwise.
     */
    public boolean hasTransportMethod(){
        return _transportMethod != null;
    }

    /**
     * Clears a basket and all its fields come back to default.
     */
    public void clearBasket(){
        _basketLines.clear();
        _transportMethod = null;
        _additionalServices.clear();
        _orderSplit = new SingleSplit(); /*order split goes back to default */
    }

    /**
     * Change the quantity of a product in basket, or remove it.
     * @param product
     * @param quantity
     * @return AddToBasketStatus - What the change did:
     * ADDED, if it resulted in a new product or quanity changed.
     * REMOVED, if the product was removed from the basket.
     * NOT_IN_BASKET, if the product to change is not in basket.
     * NOT_ENOUGH, if company doenst provide the quantity user wants to add.
     * NOT_SUPPLIED, if company doenst provide that product.
     */
    public AddToBasketStatus addToBasket(Product product, int quantity){

        Integer currentQuantity = _basketLines.get(product);

        /* Check if user is adding a new product */
        if(currentQuantity == null){
            currentQuantity = 0;
        }

        /* Problem if user wants to add a product the company doesnt provide */
        if (!(_company.providesProduct(product))){
            return AddToBasketStatus.NOT_SUPPLIED;
        }

        /* Problem if user wants to remove a product that's not in basket */
        if(quantity <= 0 && currentQuantity == 0){
            return AddToBasketStatus.NOT_IN_BASKET;
        }

        /* the resulting quantity after adding or removing */
        int finalQuantity = currentQuantity + quantity;

        /* Problem if user wants to add more quantity than inventory */
        if (quantity > 0){
            if (finalQuantity > _company.getProductUnitsAvailable(product)){
                    return AddToBasketStatus.NOT_ENOUGH;
            }
        }

        /* Passed all verifications, do the work */
        
        /* Case (1) - Remove a product completely */
        if (finalQuantity <= 0 && currentQuantity > 0){
            _basketLines.remove(product);
            return AddToBasketStatus.REMOVED;
        }

        /* Case (2) -  Add a new Product to the Basket 
         * or increase/decrease its quantity. */
        _basketLines.put(product, finalQuantity);

        return AddToBasketStatus.ADDED;

    }

    /**
     * Tells if a basket is ready to checkout according to the restrictions.
     * 
     * @return CheckoutStatus messages:
     * CANT_CHECKOUT_EMPTY_BASKET, if basket is empty
     * NO_TRANSPORT_METHOD, a set modality is mandatory.
     * NOT_ENOUGH, if in the moment of the checkout the company had 
     * already sold needed inventory.
     * SUCCESS, the checkout turned into a puchase.
     */
    public CheckoutStatus canCheckout(){

        /* Problem if user wants to checkout empty basket */
        if (isEmpty()){
            return CheckoutStatus.CANT_CHECKOUT_EMPTY_BASKET;
        }

        /* Problem if user wants to checkout a basket without a set 
         * transport method */
        else if (hasTransportMethod() == false){
            return CheckoutStatus.NO_TRANSPORT_METHOD;
        }

        /* Problem if the company doesnt have, at the moment, all the 
         * required quantities of all products. */
        for(Map.Entry<Product, Integer> entry : _basketLines.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            if (_company.getProductUnitsAvailable(product) < quantity){
                return CheckoutStatus.NOT_ENOUGH;
            }
        }

        /* Passed all verifications */
        return CheckoutStatus.SUCCESS;
    }

    /**
     * Checkout a basket which results in a purchase split in orders.
     * 
     * @param purchaseId id of the purchase to finish.
     * @param purchaseDate date of the checkout, and so the purchase.
     * @return result purchase.
     */
    public Purchase checkout(int purchaseId, int purchaseDate) { 

        List <PurchaseLine> purchaseLines = new ArrayList<>();

        /* (1) Build the purchase lines, from the basket lines */
        for(Map.Entry<Product, Integer> entry : _basketLines.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            /* lock the sale price in the checkout */
            double salePrice = _company.getProductSellPrice(product);

            /* create the new purchase line */
            PurchaseLine line = new PurchaseLine(product, quantity, salePrice);
            purchaseLines.add(line);
        }

        /* (2) Create the purchase with the purchase lines */
        Purchase purchase = new Purchase(purchaseId, purchaseDate, _client, 
                                            _company, purchaseLines); 

        /* (3) split the purchase in orders through ordersplit strategy */
        List<List<PurchaseLine>>  splitOrders;
        splitOrders = _orderSplit.split(purchaseLines); /** split work */
        
        /* create each order after the split */
        for(List<PurchaseLine> oPLines: splitOrders){
            
            Order order = new Order(purchaseId, purchaseDate, oPLines, 
                                    getAdditionalServices(), 
                                    getTransportMethod()); 

            /** save each order in the final purchase */
            purchase.addOrder(order);
        }

        /* (4) decrease company product invenory */
        for (Product product: _basketLines.keySet()){
            int quantity = _basketLines.get(product);
            _company.addUnits(product, -quantity); /** removeUnits */
        }

        /* (5) charge the client for the purchase */
        double TotalCost = purchase.calcTotalCost();
        _client.addToTotalSpent(TotalCost);

        /* (6) company start following the client */
        _company.addClient(_client);
        _company.addPurchase(purchase);

        /* (7) clear this client basket */
        clearBasket();

        return purchase; /** finally, return the result of the checkout */
    }

    /**
    * String representation of the basket.
    * 
    * @return a string representation of the basket.
    */
    @Override
    public String toString(){

        String string = "";
        
        /* 1st line */
        /* email - idCompany - strategy - transp. description - totalCost */
        string += (getClient().getEmail() + " - " + getCompany().getId() 
                    + " - " + getOrderSplit().getKey() 
                    + " - " + getTransportDescription() 
                    + " - " + String.format("%.2f", getAllLinesTotalCost()));
        
        /* basket lines */
        /* ␣␣idProduct - name - quantity - sellPrice - totalLineCost */
        for(Map.Entry<Product, Integer> entry : getAllBasketLines().entrySet()) {
            
            Product product = entry.getKey();

            int quantity = entry.getValue();
            int productId = product.getID();
            String productName = product.getName();
            double unitPrice = _company.getProductSellPrice(product);
            double totalLineCost = getTotalLineCost(product, quantity);

            string += ("\n  " + productId + " - " + productName + " - " 
                    + quantity + " - " + String.format("%.2f", unitPrice) 
                    + " - " + String.format("%.2f", totalLineCost));
        }
        
        return string;
    }

    /**
    * Two baskets are considered equal when they have the same client,
    * company, split strategy, modality, additional services and lines.
    * 
    * @param o basket to compare
    * @return true if the baskets are equals, false otherwise.
    */
    @Override 
    public boolean equals (Object o){
        if (o instanceof Basket){
            Basket b = (Basket) o;
            return (_client.equals(b.getClient()) && 
                    _company.equals(b.getCompany()) &&
                    _orderSplit.equals(b.getOrderSplit()) &&
                    (_transportMethod != null && _transportMethod.equals(b.getTransportMethod())) &&
                    _additionalServices.equals(b.getAdditionalServices()) &&
                    _basketLines.equals(b.getAllBasketLines()));
        }
        return false;
    }

}




