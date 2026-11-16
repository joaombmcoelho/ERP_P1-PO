package hva.vaccine;

import hva.EntityWriter;
import hva.animal.Species;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Vaccine implements Serializable, Comparable<Vaccine> {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final String _key;

    private final String _name;

    private final Map<String, Species> _species;

    private final List<Vaccination> _applications = new ArrayList<>();

    public Vaccine(String key, String name, Map<String, Species> species) {
        _key = key;
        _name = name;
        _species = species;
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public void registerApplication(Vaccination vaccination) {
        _applications.add(vaccination);
    }

    public int applications() {
        return _applications.size();
    }

    public Map<String, Species> species() {
        return _species;
    }

    public boolean appropriateFor(Species species) {
        return _species.containsKey(species.getKey());
    }

    public int damage(Species species) {
        //System.out.println("Vaccine name: " + getName() + "," + _species.values().stream().map(Species::getName).collect(Collectors.joining(",")));
        //System.out.println("Animal species: " + species.getName());
        //System.out.println("Vaccine damage: " + _species.values().stream()
        //        .mapToInt(s -> calculateDamage(species.getName(), s.getName()))
        //        .max()
        //        .orElse(0));
        return _species.values().stream()
                .mapToInt(s -> calculateDamage(species.getName(), s.getName()))
                .max()
                .orElse(0);
    }

    private int calculateDamage(String animalSpecies, String vaccineSpecies) {
        int maxLength = Math.max(animalSpecies.length(), vaccineSpecies.length());
        int commonChars = (int) animalSpecies.chars().distinct()
                .filter(ch -> vaccineSpecies.indexOf(ch) != -1).count();
        return maxLength - commonChars;
    }

    public String accept(EntityWriter w) {
        return w.writeVaccine(this);
    }

    @Override
    public int compareTo(Vaccine vaccine) {
        return _key.compareTo(vaccine._key);
    }

}
