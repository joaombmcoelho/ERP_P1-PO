package erp.basket;

import java.io.Serializable;
//FIXME maybe import classes

import java.util.Map;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import erp.client.Client;
import erp.purchase.OrderSplit;
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.purchase.AdditionalService;
import erp.product.Product;
import erp.purchase.PurchaseLine;

/**
 * A client's shopping basket at one company: what it intends to buy, and how it
 * wants the purchase shipped.
 *
 * <p>The basket menu is opened <em>over</em> a basket, so this class is the
 * receiver of every command in {@code erp.app.basket}. Its name and package are
 * fixed by that code; its content is yours to define.</p>
 */
public class Basket implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    //FIXME maybe define attributes
    //FIXME maybe implement constructor
    //FIXME maybe implement methods

    private Client _client;
    private Company _company;
    private OrderSplit _orderSplit;
    private TransportMethod _transportMethod;
    private List<AdditionalService> _additionalServices;
    
    private Map<Product, Integer> _basketLines; 

    public Basket (Client client, Company company, OrderSplit orderSplit,
                    TransportMethod transportMethod, List<AdditionalService> additionalServices){                   
        
        _client = client;
        _company = company;
        _orderSplit = orderSplit;
        _transportMethod = transportMethod;
        _additionalServices = additionalServices;
        _basketLines = new HashMap<>();
    }

    /** GETTERS */

    public Client getClient(){
        return _client;
    }

    public Company getCompany(){
        return _company;
    }

    public OrderSplit getOrderSplit(){
        return _orderSplit;
    }

    public TransportMethod getTransportMethod(){
        return _transportMethod;
    }

    public List<AdditionalService> getAdditionalServices(){
        return Collections.unmodifiableList(_additionalServices);
    }

    public Map<Product, Integer> getBasketLines() {
        return Collections.unmodifiableMap(_basketLines);
    }

    /** SETTERS */

    public void setOrderSplit(OrderSplit orderSplit){
        _orderSplit = orderSplit;
    }

    public void setTransportMethod(TransportMethod transportMethod){
        _transportMethod = transportMethod;
    }

    public void addAdditionalService(AdditionalService additionalService){
        _additionalServices.add(additionalService);
    }

    @Override 
    public boolean equals (Object o){
        if (o instanceof Basket){
            Basket b = (Basket) o;
            return (_client.equals(b.getClient()) && 
                    _company.equals(b.getCompany()) &&
                    _orderSplit.equals(b.getOrderSplit()) &&
                    _transportMethod.equals(b.getTransportMethod()) &&
                    _additionalServices.equals(b.getAdditionalServices()) &&
                    _basketLines.equals(b.getBasketLines()));
        }
        return false;
    }

    public boolean isEmpty(){
        return _basketLines.isEmpty();
    }

    public boolean hasTransportMethod(){
        return _transportMethod != null;
    }

    public AddToBasketStatus addToBasket(Product product, int quantity){

        Integer currentQuantity = _basketLines.get(product);

        // Check if the user is adding a new product
        if(currentQuantity == null){
            currentQuantity = 0;
        }

        // problem if user wants to remove a product that's not in basket
        if(quantity <= 0 && currentQuantity == 0){
            return AddToBasketStatus.NOT_IN_BASKET;
        }

        int finalQuantity = currentQuantity + quantity;

        // problem if user wants to add more quantity than inventory
        if (quantity > 0){
            if (finalQuantity > _company.getProductQuantity(product)){ //TODO
                    return AddToBasketStatus.NOT_ENOUGH;
            }
        }

        // Check if we want to remove existing product
        if (finalQuantity <= 0 && newProduct == false){
            _basketLines.remove(product);
            return AddToBasketStatus.REMOVED;
        }
        // Add a new Product do the Basket or increase/decrease quantity
        _basketLines.put(product, finalQuantity);

        return AddToBasketStatus.ADDED;

    }
}

public CheckoutStatus checkout(){

    // problem if user wants to checkout empty basket
    if (isEmpty()){
        return CheckoutStatus.CANT_CHECKOUT_EMPTY_BASKET;
    }

    // problem if user wants to checkout a basket without transport method set
    else if (hasTransportMethod() == false){
        return CheckoutStatus.NO_TRANSPORT_METHOD;
    }

    // problem if the company doesnt all the required quantities of products
    for (Product product: _basketLines.keySet()){
        int quantity = _basketLines.get(product);
        
        if (_company.getProductQuantity(product) < quantity){
            return CheckoutStatus.NOT_ENOUGH;
        }
    }

    //todo ver o q fazer aqui
    PurchaseLine<List> purchaseLines;

    // perform the checkout
    for (Product product: _basketLines.keySet()){
        int quantity = _basketLines.get(product);
        
        // lock the sale price in purchase lines

    }

    

}

