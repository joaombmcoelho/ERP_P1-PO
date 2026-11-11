package erp.purchase;

import java.io.Serializable;
// import java.util.Map;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Collections;
// import java.util.Map;
import java.util.List;

public interface OrderSplit extends Serializable{

    public List<List<PurchaseLine>> split(List <PurchaseLine> purchaseLines);

    public String getKey();

}