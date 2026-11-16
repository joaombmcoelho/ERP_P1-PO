package hva.employee;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class VeterinarianUnknownException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public VeterinarianUnknownException(String key) {
        super(key);
    }

}
