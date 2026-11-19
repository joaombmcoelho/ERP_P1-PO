package erp.exceptions;


    
/**
 * Reperesents an error when the name of a company is invalid (empty).
*/
public class InvalidCompanyNameException extends Exception{

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    public InvalidCompanyNameException(){
        super();
    }
}

