package erp.purchase;

import java.io.Serializable;
// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Collections;
// import java.util.Map;

public class TransportMethod implements Serializable{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    private String _key;
    private int _shippingFee;
    private int _shippingTime;


    public TransportMethod(String key, int shippingFee, int shippingTime){
        _key = key;
        _shippingFee = shippingFee;
        _shippingTime = shippingTime;
    }

    /** GETTERS */

    public String getKey(){
        return _key;
    }

    public int getShippingFee(){
        return _shippingFee;
    }

    public int getShippingTime(){
        return _shippingTime;
    }

    @Override 
    public boolean equals (Object o){
        if (o instanceof TransportMethod){
            TransportMethod tm = (TransportMethod) o;
            return (_key.equals(tm.getKey()) && 
                    _shippingFee == tm.getShippingFee() &&
                    _shippingTime == tm.getShippingTime());
        }
        return false;
    }

    //@Override 
    //TODO VER
    //public String toString(){
    //}

}