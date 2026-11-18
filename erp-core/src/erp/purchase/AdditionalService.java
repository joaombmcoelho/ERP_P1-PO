package erp.purchase;

import java.io.Serializable;


public interface AdditionalService extends Serializable{
    
    public int getAdditionalCost();
    public int getAdditionalTime();
    public String getKey();
    
}