package erp.exceptions;

/**
 * Represents an error when the given purchase id doenst match with any
 * purchase registered in the system, because it doenst exist.
 */
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
    