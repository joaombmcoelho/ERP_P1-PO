package erp.company;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import erp.company.ProductInventory;
import erp.product.Product;

// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;


public class Company implements Serializable{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private int _id;
    private String _name;
    private Map <Product, ProductInventory> _inventory;
    private List <Client> _clients;
    private Map <Integer, Purchase> _purchases; //map of purchases with purchase id

    public Company(int id, String name){
        _id = id;
        _name = name;
        _inventory = new HashMap<>();


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

    public Map<Integer, Basket> getBaskets() {
        return Collections.unmodifiableMap(_clientBaskets);
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
                    _clientBaskets.equals(c.getBaskets()));
        }
        return false;
    }

    @Override 
    public String toString(){
        return _email + " - " + _name + " - " + _totalSpent;
    }
}
}