package erp.purchase;

public class GiftWrap implements AdditionalService{

    @Override 
    public void getAdditionalCost(){
        return 2;
    }

    @Override 
    public void getAdditionalTime(){
        return 1;
    }

    @Override
    public void getKey(){
        return "GIFT WRAP";
    }
}