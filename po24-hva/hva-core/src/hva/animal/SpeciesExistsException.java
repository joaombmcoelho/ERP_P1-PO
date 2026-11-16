package hva.animal;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class SpeciesExistsException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public SpeciesExistsException(String key) {
		super(key);
	}

}
