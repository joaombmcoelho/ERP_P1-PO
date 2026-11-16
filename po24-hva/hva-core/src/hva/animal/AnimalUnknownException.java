package hva.animal;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class AnimalUnknownException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public AnimalUnknownException(String key) {
		super(key);
	}

}
