package hva.vaccine;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class VaccineUnknownException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public VaccineUnknownException(String key) {
		super(key);
	}

}
