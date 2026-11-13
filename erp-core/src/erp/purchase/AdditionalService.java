package erp.purchase;

import java.io.Serializable;
«

public interface AdditionalService extends Serializable{
    
    public void getAdditionalCost();
    public void getAdditionalTime();
    public String getKey();
    
}