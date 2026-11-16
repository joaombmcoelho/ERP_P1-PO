package hva.app;

import hva.EntityWriter;
import hva.animal.Animal;
import hva.animal.Species;
import hva.employee.Employee;
import hva.employee.GroundsKeeper;
import hva.employee.Veterinarian;
import hva.habitat.Habitat;
import hva.habitat.Tree;
import hva.habitat.deciduous.DeciduousTree;
import hva.habitat.evergreen.EvergreenTree;
import hva.vaccine.Vaccination;
import hva.vaccine.Vaccine;

import java.util.stream.Collectors;

public class TextWriter implements EntityWriter {

    @Override
    public String writeAnimal(Animal animal) {
        String healthRecord = animal.healthRecord().stream().collect(Collectors.joining(","));
        return "ANIMAL|" + animal.getKey() + "|" + animal.getName() + "|" + animal.getSpecies().getKey() + "|" +
                healthRecord + "|" + animal.getHabitat().getKey();
    }

    @Override
    public String writeSpecies(Species species) {
        return "ÉSPECIE|" + species.getKey() + "|" + species.getName();
    }

    private String writeEmployee(Employee employee) {
        var responsibilities = employee.responsibilities().stream().collect(Collectors.joining(","));
        return employee.getRole() + "|" + employee.getKey() + "|" + employee.getName() +
                (responsibilities.isEmpty() ? "" : "|" + responsibilities);
    }

    @Override
    public String writeVeterinarian(Veterinarian veterinarian) {
        return writeEmployee(veterinarian);
    }

    public String writeGroundsKeeper(GroundsKeeper groundsKeeper) {
        return writeEmployee(groundsKeeper);
    }

    @Override
    public String writeHabitat(Habitat habitat) {
        var text = "HABITAT|" + habitat.getKey() + "|" + habitat.getName() + "|" + habitat.getArea() + "|" + habitat.numberOfTrees();
        String treeText = habitat.getTrees().values().stream()
                .sorted()
                .map(tree -> tree.accept(this))
                .collect(Collectors.joining("\n"));
        if (!treeText.isEmpty()) text += "\n" + treeText;
        return text;
    }

    private String writeTree(Tree tree) {
        return "ÁRVORE|" + tree.getKey() + "|" + tree.getName() + "|" + tree.age() + "|" + Math.round(tree.getBaseUpkeep()) + "|" +
                tree.typeName() + "|" + tree.lifecycle();
    }

    @Override
    public String writeDeciduousTree(DeciduousTree tree) {
        return writeTree(tree);
    }

    @Override
    public String writeEvergreenTree(EvergreenTree tree) {
        return writeTree(tree);
    }

    @Override
    public String writeVaccine(Vaccine vaccine) {
        var species = vaccine.species().keySet().stream().sorted().collect(Collectors.joining(","));
        return "VACINA|" + vaccine.getKey() + "|" + vaccine.getName() + "|" + vaccine.applications() + (species.isEmpty() ? "" : "|" + species);
    }

    @Override
    public String writeVaccination(Vaccination vaccination) {
        return "REGISTO-VACINA|" + vaccination.getVaccine().getKey() + "|" + vaccination.getVeterinarian().getKey() + "|" +
                vaccination.getAnimal().getSpecies().getKey();
    }

}
