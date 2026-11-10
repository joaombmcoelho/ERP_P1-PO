package erp.purchase;

import java.io.Serializable;
// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Collections;
// import java.util.Map;

public interface OrderSplit extends Serializable{

    public PurchaseLine[][] split(PurchaseLine[] purchaseLines);

    public String getKey();

}