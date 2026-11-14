package erp.exceptions;

public class UnknownCompanyException extends Exception {

    @java.io.Serial
    private static final long serialVersionUID = 202607021200L;

    private int _companyID;

    public UnknownCompanyException(int id){
        super("Unknown Company:" + id);
        _companyID = id;
    }

    public int getCompanyID(){
        return _companyID;
    }
}