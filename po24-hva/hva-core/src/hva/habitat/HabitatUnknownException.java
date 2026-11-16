package hva.habitat;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class HabitatUnknownException extends HotelKeyException {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    public HabitatUnknownException(String key) {
        super(key);
    }

}
