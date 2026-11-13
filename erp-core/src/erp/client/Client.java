package erp.client;

import java.io.Serializable;
import java.util.Map;
import java.util.Collections;
import java.util.HashMap;

import erp.basket.Basket;


public class Client implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private String _email;
    private String _name;
    private double _totalSpent;
    private Map<Integer, Basket> _clientBaskets; 

    public Client(String email, String name){
        _email = email;
        _name = name;
        _totalSpent = 0;
        _clientBaskets = new HashMap<>();
    }

    /** GETTERS */

    public String getEmail(){
        return _email;
    }

    public String getName(){
        return _name;
    }

    public double getTotalSpent(){
        return _totalSpent;
    }

    public Map<Integer, Basket> getAllBaskets() {
        return Collections.unmodifiableMap(_clientBaskets);
    }

    public Basket getBasket(int companyId){
        return _clientBaskets.get(companyId);
    }

    /** SETTERS */

    public void setEmail(String newEmail){
        _email = newEmail;
    }

    public void setName(String newName){
        _name = newName;
    }

    public void addToTotalSpent(double ValueToAdd){
        _totalSpent += ValueToAdd;
    }

    public void addBasket(int company_id, Basket basket) {
        _clientBaskets.put(company_id, basket);
    }

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

    @Override 
    public String toString(){
        return _email + " - " + _name + " - " + _totalSpent;
    }
}
