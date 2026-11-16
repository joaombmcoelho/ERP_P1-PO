package hva.animal;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class AnimalExistsException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public AnimalExistsException(String key) {
		super(key);
	}

}
