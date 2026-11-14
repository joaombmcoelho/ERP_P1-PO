package erp.exceptions;

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
