package erp.product;

import java.io.Serializable;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.List;

// import erp.basket.Basket;
// import erp.client.Client;
// import erp.company.ProductInventory;
import erp.product.Product;
// import erp.purchase.Purchase;

public interface Search extends Serializable{

    public boolean search(Product product, String term);

    public String getKey();
}