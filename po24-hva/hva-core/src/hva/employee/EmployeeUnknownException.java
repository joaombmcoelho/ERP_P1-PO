package hva.employee;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class EmployeeUnknownException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public EmployeeUnknownException(String key) {
        super(key);
    }

}
