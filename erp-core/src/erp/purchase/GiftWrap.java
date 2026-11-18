package erp.purchase;

public class GiftWrap implements AdditionalService{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override 
    public int getAdditionalCost(){
        return 2;
    }

    @Override 
    public int getAdditionalTime(){
        return 1;
    }

    @Override
    public String getKey(){
        return "GIFT WRAP";
    }

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