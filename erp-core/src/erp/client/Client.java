package erp.client;

import java.io.Serializable;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import erp.basket.Basket;

/**
 * Class for clients registered in ERP.
 */
public class Client implements Serializable {

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** Client email */
    private String _email;

    /** Client name */
    private String _name;

    /** How much the client has spent in ERP */
    private double _totalSpent;

    /** All the Baskets from various companies the client has */
    private Map<Integer, Basket> _clientBaskets; 

    /** Constructor.
     * 
     * @param email
     * @param name
     */
    public Client(String email, String name){
        _email = email;
        _name = name;
        _totalSpent = 0;
        _clientBaskets = new HashMap<>();
    }

    // --- getters ---------------------------------------------------

    /**
     * @return client email.
     */
    public String getEmail(){
        return _email;
    }

    /**
     * @return client name.
     */
    public String getName(){
        return _name;
    }

    /**
     * @return client total spent value.
     */
    public double getTotalSpent(){
        return _totalSpent;
    }   

    /**
     * @return copy of all the client baskets.
     */
    public Map<Integer, Basket> getAllBaskets() {
        return Collections.unmodifiableMap(_clientBaskets);
    }

    /**
     * Get the basket of this client in the company with given id.
     * 
     * @return that basket.
     */
    public Basket getBasket(int companyId){
        return _clientBaskets.get(companyId);
    }

    // --- setters ---------------------------------------------------

    /**
     * Changes the email of a client.
     * 
     * @param newEmail
     */
    public void setEmail(String newEmail){
        _email = newEmail;
    }

    /**
     * Changes the name of a client.
     * 
     * @param newName
     */
    public void setName(String newName){
        _name = newName;
    }

    /**
     * Adds (remove not supposed) to client total spent.
     * 
     * @param ValueToAdd
     */
    public void addToTotalSpent(double ValueToAdd){
        _totalSpent += ValueToAdd;
    }

    /**
    * Adds the provided basket, from the company with given id to the
    * client registry of baskets.
    * @param company_id
    * @param basket
    */
    public void addBasket(int company_id, Basket basket) {
        _clientBaskets.put(company_id, basket);
    }

    // --- methods ---------------------------------------------------

    /**
    * Two clients are considered equal when they have the same email,
    * name, totalSpent and baskets.
    * 
    * @param o client to compare
    * @return true if the clients are equals, false otherwise.
    */
    @Override 
    public boolean equals (Object o){
        if (o instanceof Client){
            Client c = (Client) o;
            return (_email.equals(c.getEmail()) && 
                    _name.equals(c.getName()) &&
                    _totalSpent == c.getTotalSpent() &&
                    _clientBaskets.equals(c.getAllBaskets()));
        }
        return false;
    }

    /**    
     * String representation of a client: 
     * email - name - totalSpent
     * 
     * @return a string representation of the client.
     */
    @Override 
    public String toString(){
        return _email + " - " + _name + " - " + 
                String.format("%.2f", _totalSpent);
    }
}
