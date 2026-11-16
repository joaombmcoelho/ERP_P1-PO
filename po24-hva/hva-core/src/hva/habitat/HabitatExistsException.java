package hva.habitat;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class HabitatExistsException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public HabitatExistsException(String key) {
        super(key);
    }

}
