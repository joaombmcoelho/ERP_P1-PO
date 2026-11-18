package erp.exceptions;

public class ProductNotSuppliedException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    private int _productID;

    public ProductNotSuppliedException(int id){
        super("Product Not Supplied:" + id);
        _productID = id;
    }

    public ProductNotSuppliedException(){
        super();
    }

    public int getProductID(){
        return _productID;
    }
}