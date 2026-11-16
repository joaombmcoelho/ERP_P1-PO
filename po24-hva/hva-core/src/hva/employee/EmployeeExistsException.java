package hva.employee;

import hva.exceptions.HotelKeyException;

import java.io.Serial;

public class EmployeeExistsException extends HotelKeyException {

	@Serial
	private static final long serialVersionUID = 202407081733L;

	public EmployeeExistsException(String key) {
		super(key);
	}

}
