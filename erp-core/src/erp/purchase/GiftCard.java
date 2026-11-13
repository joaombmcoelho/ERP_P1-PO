package erp.purchase;

public class GiftCard implements AdditionalService{

    @Override 
    public void getAdditionalCost(){
        return 1;
    }

    @Override 
    public void getAdditionalTime(){
        return 0;
    }

    @Override
    public void getKey(){
        return "GIFT CARD";
    }
}