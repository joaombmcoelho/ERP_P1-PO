package hva.vaccine;

import hva.EntityWriter;
import hva.animal.Animal;
import hva.employee.Veterinarian;

import java.io.Serial;
import java.io.Serializable;

public class Vaccination implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private final Veterinarian _veterinarian;
    private final Animal _animal;
    private final Vaccine _vaccine;

    public Vaccination(Veterinarian veterinarian, Animal animal, Vaccine vaccine, int date) {
        _veterinarian = veterinarian;
        _animal = animal;
        _vaccine = vaccine;
    }

    public Veterinarian getVeterinarian() {
        return _veterinarian;
    }

    public Animal getAnimal() {
        return _animal;
    }

    public Vaccine getVaccine() {
        return _vaccine;
    }

    public String accept(EntityWriter writer) {
        return writer.writeVaccination(this);
    }

}
