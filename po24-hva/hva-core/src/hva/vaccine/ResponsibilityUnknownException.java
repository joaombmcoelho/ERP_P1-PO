package hva.vaccine;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class ResponsibilityUnknownException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	private final String _responsibilityKey;

	public ResponsibilityUnknownException(String employeeKey, String responsibilityKey) {
		super(employeeKey);
		_responsibilityKey = responsibilityKey;
	}

	public String getResponsibilityKey() {
		return _responsibilityKey;
	}
}
