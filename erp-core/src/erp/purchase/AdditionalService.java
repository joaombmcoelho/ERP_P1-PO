package erp.purchase;

import java.io.Serializable;

/**
 * Defines an additional service.
 */
public interface AdditionalService extends Serializable{
    
    /**
     * @return additional service cost.
     */
    public int getAdditionalCost();

    /**
     * @return additional service time.
     */
    public int getAdditionalTime();

    /**
     * @return name of the additional service.
     */
    public String getKey();
    
}