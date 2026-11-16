package hva.animal;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class SpeciesUnknownException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public SpeciesUnknownException(String key) {
		super(key);
	}

}
