package hva.employee;

import hva.EntityWriter;
import hva.Hotel;
import hva.habitat.Habitat;
import hva.habitat.HabitatUnknownException;
import hva.vaccine.ResponsibilityUnknownException;

import java.io.Serial;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroundsKeeper extends Employee {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private Map<String, Habitat> _habitats = new HashMap<>();

    public GroundsKeeper(Hotel hotel, String key, String[] fields) throws ResponsibilityUnknownException {
        super(hotel, key, fields[2], "TRT");
        if (fields.length > 3) addResponsibilities(fields[3].split(","));
    }

    public Collection<Habitat> getHabitats() {
        return _habitats.values();
    }

    @Override
    public List<String> responsibilities() {
        return _habitats.values().stream().map(Habitat::getKey).sorted(String::compareToIgnoreCase).toList();
    }

    @Override
    public void addResponsibilities(String... responsibilities) throws ResponsibilityUnknownException {
        for (var habitatKey : responsibilities) {
            try {
                var habitat = _hotel.habitatByKey(habitatKey);
                _habitats.put(habitatKey, habitat);
            } catch (HabitatUnknownException e) {
                throw new ResponsibilityUnknownException(getKey(), habitatKey);
            }
        }
    }

    @Override
    public void removeResponsibilities(String... responsibilities) throws ResponsibilityUnknownException {
        for (var habitatKey : responsibilities) {
            if (!_habitats.containsKey(habitatKey))
                throw new ResponsibilityUnknownException(getKey(), habitatKey);
            _habitats.remove(habitatKey);
        }
    }

    @Override
    public boolean hasResponsibility(String habitatKey) {
        return _habitats.containsKey(habitatKey);
    }

    @Override
    public double satisfaction() {
        return 300 - satisfactionWorkload();
    }

    public double satisfactionWorkload() {
        return _habitats.values().stream()
                .mapToDouble(h -> _hotel.groundsKeepersForHabitat(h) == 0
                        ? 0
                        : h.upkeep() / _hotel.groundsKeepersForHabitat(h))
                .sum();
    }

    @Override
    public String accept(EntityWriter writer) {
        return writer.writeGroundsKeeper(this);
    }

}
