package erp.exceptions;

public class UnknownPurchaseException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    private int _purchaseID;

    public UnknownPurchaseException(int id){
        super("Unknown Purchase:" + id);
        _purchaseID = id;
    }

    public int getPurchaseID(){
        return _purchaseID;
    }
}
    