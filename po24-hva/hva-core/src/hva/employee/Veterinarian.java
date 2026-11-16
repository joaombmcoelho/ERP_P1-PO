package hva.employee;

import hva.EntityWriter;
import hva.Hotel;
import hva.animal.Species;
import hva.animal.SpeciesUnknownException;
import hva.vaccine.ResponsibilityUnknownException;

import java.io.Serial;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Veterinarian extends Employee {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final Map<String, Species> _species = new HashMap<>();

    public Veterinarian(Hotel hotel, String key, String[] fields) throws ResponsibilityUnknownException {
        super(hotel, key, fields[2], "VET");
        if (fields.length > 3) addResponsibilities(fields[3].split(","));
    }

    public Collection<Species> getSpecies() {
        return _species.values();
    }

    @Override
    public List<String> responsibilities() {
        return _species.values().stream().map(Species::getKey).sorted(String::compareToIgnoreCase).toList();
    }

    public void addSpecies(Species species) {
        _species.put(species.getKey(), species);
    }

    public void removeSpecies(String species) throws ResponsibilityUnknownException {
        if (!_species.containsKey(species)) throw new ResponsibilityUnknownException(getKey(), species);
        _species.remove(species);
    }

    @Override
    public void addResponsibilities(String... responsibilities) throws ResponsibilityUnknownException {
        for (var speciesKey : responsibilities) {
            try {
                var species = _hotel.speciesByKey(speciesKey);
                _species.put(speciesKey, species);
            } catch (SpeciesUnknownException e) {
                throw new ResponsibilityUnknownException(getKey(), speciesKey);
            }
        }
    }

    @Override
    public void removeResponsibilities(String... responsibilities) throws ResponsibilityUnknownException {
        for (var r : responsibilities)
            removeSpecies(r);
    }

    @Override
    public boolean hasResponsibility(String speciesKey) {
        return _species.containsKey(speciesKey);
    }

    @Override
    public double satisfaction() {
        return 20 - satisfactionWorkload();
    }

    public double satisfactionWorkload() {
        return _species.values().stream()
                .mapToDouble(s -> _hotel.veterinariansForSpecies(s) == 0
                        ? 0
                        : s.population() / (double)_hotel.veterinariansForSpecies(s))
                .sum();
    }

    @Override
    public String accept(EntityWriter writer) {
        return writer.writeVeterinarian(this);
    }

}
