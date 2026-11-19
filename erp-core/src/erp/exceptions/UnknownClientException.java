package erp.exceptions;

/**
 * Represents an error when the given client email (id) doenst match 
 * with any client, because it doenst exist.
 */
public class UnknownClientException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    /** client id = email */
    private String _clientEmail;

    public UnknownClientException(String email){
        super("Unknown Client:" + email);
        _clientEmail= email;
    }

    public String getClientEmail(){
        return _clientEmail;
    }
}