package hva.habitat;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class TreeUnknownException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public TreeUnknownException(String key) {
        super(key);
    }

}
