package erp.purchase;

/**
 * Defines the additional order Gift Card additional cost, shipping 
 * time and name.
 */
public class GiftCard implements AdditionalService{

    /** Serial number for serialization. */
    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    /**
     * @return gift card additional cost = 1.
     */
    @Override 
    public int getAdditionalCost(){
        return 1;
    }

    /**
     * @return gift card additional time = 0.
     */
    @Override 
    public int getAdditionalTime(){
        return 0;
    }

    /**
     * @return name of the additional service.
     */
    @Override
    public String getKey(){
        return "GIFT CARD";
    }

    /**
    * Two gitf cards are considered equal when they have the same cost,
    * time and key.
    * 
    * @param o additional service to compare
    * @return true if additional services are equals, false otherwise.
    */
    @Override
    public boolean equals(Object o){
        if (o instanceof AdditionalService){
            AdditionalService gC = (AdditionalService) o;
            return (getAdditionalCost() == gC.getAdditionalCost() &&
                    getAdditionalTime() == gC.getAdditionalTime() &&
                    getKey().equals(gC.getKey()));
        }
        return false;
    }

}