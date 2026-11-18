package erp.purchase;

/**
 * Defines the additional order Gift Wrap additional cost, shipping 
 * time and name.
 */
public class GiftWrap implements AdditionalService{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;
    
    /**
     * @return gift wrap additional cost = 2.
     */
    @Override 
    public int getAdditionalCost(){
        return 2;
    }

    /**
     * @return gift wrap additional time = 1.
     */
    @Override 
    public int getAdditionalTime(){
        return 1;
    }

    /**
     * @return name of the additional service.
     */
    @Override
    public String getKey(){
        return "GIFT WRAP";
    }

    /**
    * Two gitf wraps are considered equal when they have the same cost,
    * time and key.
    * 
    * @param o additional service to compare
    * @return true if additional services are equals, false otherwise.
    */
    @Override
    public boolean equals(Object o){
        if (o instanceof AdditionalService){
            AdditionalService gW = (AdditionalService) o;
            return (getAdditionalCost() == gW.getAdditionalCost() &&
                    getAdditionalTime() == gW.getAdditionalTime() &&
                    getKey().equals(gW.getKey()));
        }
        return false;
    }
}