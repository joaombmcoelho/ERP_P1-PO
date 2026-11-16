package hva.vaccine;

import java.io.Serial;

public class CannotVaccinateException extends Exception {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    // Vaccine ID and species ID
    String _veterinarianKey;
    String _speciesKey;

    public CannotVaccinateException(String veterinarianKey, String speciesKey) {
        _veterinarianKey = veterinarianKey;
        _speciesKey = speciesKey;
    }

    public String getVeterinarianKey() {
        return _veterinarianKey;
    }

    public String getSpeciesKey() {
        return _speciesKey;
    }

}
