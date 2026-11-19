package erp.exceptions;

/**
 * Reperesents an error when at least one of the product fields
 * is not valid.
*/
public class InvalidProductFieldsException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    public InvalidProductFieldsException(){
        super();
    }
}

