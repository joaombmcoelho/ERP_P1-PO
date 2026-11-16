package hva.animal;

import hva.EntityWriter;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Species implements Serializable, Comparable<Species> {

    @Serial
    private static final long serialVersionUID = 202407081733L;
    private final String _key;
    private final String _name;
    private Map<String, Animal> _population = new HashMap<>();

    public Species(String key, String name) {
        _key = key;
        _name = name;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public Animal getAnimal(String key) throws AnimalUnknownException {
        if (!_population.containsKey(key)) throw new AnimalUnknownException(key);
        return _population.get(key);
    }

    public void addAnimal(Animal animal) throws AnimalExistsException {
        if (_population.containsKey(animal.getKey())) throw new AnimalExistsException(animal.getKey());
        _population.put(animal.getKey(), animal);
    }

    public int population() {
        return _population.size();
    }

    public String accept(EntityWriter writer) {
        return writer.writeSpecies(this);
    }

    @Override
    public int compareTo(Species species) {
        return _key.compareToIgnoreCase(species._key);
    }
}
