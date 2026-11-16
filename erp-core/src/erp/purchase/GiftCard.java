package erp.purchase;

public class GiftCard implements AdditionalService{

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    @Override 
    public int getAdditionalCost(){
        return 1;
    }

    @Override 
    public int getAdditionalTime(){
        return 0;
    }

    @Override
    public String getKey(){
        return "GIFT CARD";
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof GiftCard){
            GiftCard gC = (GiftCard) o;
            return (getAdditionalCost() == gC.getAdditionalCost() &&
                    getAdditionalTime() == gC.getAdditionalTime() &&
                    getKey().equals(gC.getKey()));
        }
        return false;
    }

}