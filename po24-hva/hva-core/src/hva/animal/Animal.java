package hva.animal;

import hva.EntityWriter;
import hva.Hotel;
import hva.habitat.Habitat;
import hva.vaccine.Vaccination;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Animal implements Serializable, Comparable<Animal> {

    @Serial
    private static final long serialVersionUID = 202407081733L;
    private final String _key;
    private final String _name;
    private final Species _species;
    public List<Vaccination> _healthRecord = new ArrayList<>();
    private Habitat _habitat;

    /**
     * Habitat is left unfilled.
     * ANIMAL|A1|Farrusco|E1|H1
     */
    public Animal(String key, String name, Species species, Hotel hotel) throws AnimalExistsException {
        _key = key;
        _name = name;
        _species = species;
        _species.addAnimal(this);
    }

    public String getKey() {
        return _key;
    }

    public String getName() {
        return _name;
    }

    public Species getSpecies() {
        return _species;
    }

    public Habitat getHabitat() {
        return _habitat;
    }

    public void setHabitat(Habitat habitat) {
        _habitat = habitat;
    }

    public int damage(Vaccination vaccination) {
        if (vaccination.getVaccine().appropriateFor(_species)) return 0;
        return vaccination.getVaccine().damage(_species);
    }

    public List<String> healthRecord() {
        if (_healthRecord.isEmpty()) return List.of("VOID");
        return _healthRecord.stream().map(vaccination -> {
            var d = damage(vaccination);
            if (d == 0) return vaccination.getVaccine().appropriateFor(_species) ? "NORMAL" : "CONFUSÃO";
            else if (d >= 1 && d <= 4) return "ACIDENTE";
            else return "ERRO";
        }).toList();
    }

    public void addVaccination(Vaccination vaccination) {
        _healthRecord.add(vaccination);
    }

    public double satisfaction() {
        return 20 + satisfactionDiversity() + satisfactionDensity() + satisfactionAdequacy();
    }

    public double satisfactionDiversity() {
        var same = 3 * (_habitat.populationOfSpecies(_species.getKey()) - 1);
        var other = 2 * _habitat.populationNotOfSpecies(_species.getKey());
        return same - other;
    }

    public double satisfactionDensity() {
        return 1 / _habitat.density();
    }

    public double satisfactionAdequacy() {
        return _habitat.influence(_species).value();
    }

    public String accept(EntityWriter writer) {
        return writer.writeAnimal(this);
    }

    @Override
    public int compareTo(Animal animal) {
        return _key.compareTo(animal._key);
    }
}
