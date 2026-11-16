package hva;

import hva.animal.Animal;
import hva.animal.Species;
import hva.employee.Veterinarian;
import hva.employee.GroundsKeeper;
import hva.habitat.deciduous.DeciduousTree;
import hva.habitat.evergreen.EvergreenTree;
import hva.habitat.Habitat;
import hva.vaccine.Vaccination;
import hva.vaccine.Vaccine;

public interface EntityWriter {
    String writeAnimal(Animal animal);

    String writeSpecies(Species species);

    String writeVeterinarian(Veterinarian veterinarian);

    String writeGroundsKeeper(GroundsKeeper groundsKeeper);

    String writeHabitat(Habitat habitat);

    String writeVaccine(Vaccine vaccine);

    String writeVaccination(Vaccination vaccination);

    String writeDeciduousTree(DeciduousTree tree);

    String writeEvergreenTree(EvergreenTree tree);
}
