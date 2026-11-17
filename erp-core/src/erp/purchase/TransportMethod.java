package erp.purchase;

import java.io.Serializable;

/**
 * Class that represents a Transport Method (modality) in the ERP system.
 * TransportMethod
 */
public class TransportMethod implements Serializable{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /** name of this transport method. */
    private String _key;

    /** cost of this transport method. */
    private int _shippingFee;

    /** number of days this transport method takes. */
    private int _shippingTime;

    /**
     * Constructor.
     * @param key
     * @param shippingFee
     * @param shippingTime
     */
    public TransportMethod(String key, int shippingFee, int shippingTime){
        _key = key;
        _shippingFee = shippingFee;
        _shippingTime = shippingTime;
    }

    // --- getters ---------------------------------------------------

    /**
     * @return transport method name.
     */
    public String getKey(){
        return _key;
    }

    /**
     * @return transport method cost.
     */
    public int getShippingFee(){
        return _shippingFee;
    }

    /**
     * @return  number of days this transport method takes.
     */
    public int getShippingTime(){
        return _shippingTime;
    }

    /**
    * Two Transport Methods are considered equal when they have the 
    * same key (name), shipping fee and shipping time.
    * 
    * @param o transport method to compare.
    * @return true if the modalities are equals, false otherwise.
    */
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

}