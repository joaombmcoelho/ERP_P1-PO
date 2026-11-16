package hva.habitat;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class TreeExistsException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public TreeExistsException(String key) {
        super(key);
    }

}
