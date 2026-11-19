package erp.exceptions;


/**
 * Represents an error when the company does not have the inventory
 * needed to perform an action on the products.
 */
public class InsufficientInventoryException extends Exception {
    @java.io.Serial
    private static final long serialVersionUID = 202611141853L;

    public InsufficientInventoryException() {
        super();
    }
}

