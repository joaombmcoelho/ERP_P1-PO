package hva.habitat;

import hva.EntityWriter;
import hva.animal.Animal;
import hva.animal.Species;
import hva.animal.SpeciesUnknownException;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Habitat implements Serializable, Comparable<Habitat> {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final String _key;

    private final String _name;

    private int _area = 0;

    private Map<String, Tree> _trees = new HashMap<>();

    private Map<String, Animal> _animals = new HashMap<>();

    private Map<String, Species> _species = new HashMap<>();

    private Map<String, HabitatInfluence> _influence = new HashMap<>();

    public Habitat(String key, String name, int area) {
        _key = key;
        _name = name;
        _area = area;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public int getArea() {
        return _area;
    }

    public void setArea(int area) {
        _area = area;
    }

    public void addSpecies(Species species, String influence) {
        _species.put(species.getKey(), species);
        try {
            _influence.put(species.getKey(), HabitatInfluence.valueOf(influence));
        } catch (IllegalArgumentException _) {
            _influence.put(species.getKey(), HabitatInfluence.NEU);
        }
    }

    public void removeSpecies(Species species) throws SpeciesUnknownException {
        if (!_species.containsKey(species.getKey())) throw new SpeciesUnknownException(species.getKey());
        _species.remove(species.getKey());
        _influence.remove(species.getKey());
    }

    public void addTree(Tree tree) {
        _trees.put(tree.getKey(), tree);
    }

    public Map<String, Tree> getTrees() {
        return _trees;
    }

    public int numberOfTrees() {
        return _trees.size();
    }

    public void addAnimal(Animal animal) {
        animal.setHabitat(this);
        _animals.put(animal.getKey(), animal);
        addSpecies(animal.getSpecies(), influence(animal.getSpecies()).toString());
    }

    public void removeAnimal(Animal animal) {
        animal.setHabitat(null);
        _animals.remove(animal.getKey());
        //DAVID FIXME - remove species if no animals of that species are left?
        if (populationOfSpecies(animal.getSpecies().getKey()) == 0) {
            try {
                removeSpecies(animal.getSpecies());
            } catch (SpeciesUnknownException e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, Animal> getAnimals() {
        return _animals;
    }

    public int population() {
        return _animals.size();
    }

    public int populationOfSpecies(String speciesKey) {
        return _animals.values().stream()
                .mapToInt(a -> a.getSpecies().getKey().equals(speciesKey) ? 1 : 0)
                .sum();
    }

    public int populationNotOfSpecies(String speciesKey) {
        return population() - populationOfSpecies(speciesKey);
    }

    public double density() {
        if (_area == 0) return 0;
        return population() / (double) _area;
    }

    public HabitatInfluence influence(Species species) {
        return _influence.getOrDefault(species.getKey(), HabitatInfluence.NEU);
    }

    public double upkeep() {
        return _area + 3 * population() + _trees.values().stream().mapToDouble(Tree::upkeep).sum();
    }

    public void advanceSeason() {
        _trees.values().forEach(Tree::advanceSeason);
    }

    public String accept(EntityWriter writer) {
        return writer.writeHabitat(this);
    }

    @Override
    public int compareTo(Habitat other) {
        return _key.compareToIgnoreCase(other._key);
    }

}
