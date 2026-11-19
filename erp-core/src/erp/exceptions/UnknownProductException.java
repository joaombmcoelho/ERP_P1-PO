package erp.exceptions;

/**
 * Represents an error when the given product id doenst match with any
 * product registered in the system, because it doenst exist.
 */
public class UnknownProductException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    private int _productID;

    public UnknownProductException(int id){
        super("Unknown Product" + id);
        _productID = id;
    }

    public int getProductID(){
        return _productID;
    }
}
