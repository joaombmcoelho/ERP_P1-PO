package hva;

import hva.animal.*;
import hva.employee.*;
import hva.exceptions.UnrecognizedEntryException;
import hva.habitat.*;
import hva.habitat.deciduous.DeciduousTree;
import hva.habitat.evergreen.EvergreenTree;
import hva.vaccine.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Hotel implements Serializable {

    @Serial
    private static final long serialVersionUID = 202407081733L;

    private transient boolean _changed = false;

    private String _filename = null;

    private final String _name = null;

    private final Map<String, Animal> _animals = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, Species> _species = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, Employee> _employees = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, Habitat> _habitats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, Tree> _trees = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final Map<String, Vaccine> _vaccines = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final List<Vaccination> _vaccinations = new ArrayList<>();

    private Season _season = Season.SPRING;

    public Hotel() {
        // EMPTY
    }

    public String getFilename() {
        return _filename;
    }

    public void setFilename(String filename) {
        _filename = filename;
        _changed = true;
    }

    public String getName() {
        return _name;
    }

    public void setChanged(boolean changed) { _changed = changed; }

    public boolean changed() {
        return _changed;
    }

    public Animal animalByKey(String key) throws AnimalUnknownException {
        var animal = _animals.get(key);
        if (animal == null) throw new AnimalUnknownException(key);
        return animal;
    }

    public Species speciesByKey(String key) throws SpeciesUnknownException {
        var species = _species.get(key);
        if (species == null) throw new SpeciesUnknownException(key);
        return species;
    }

    public Employee employeeByKey(String key) throws EmployeeUnknownException {
        var employee = _employees.get(key);
        if (employee == null) throw new EmployeeUnknownException(key);
        return employee;
    }

    public Habitat habitatByKey(String key) throws HabitatUnknownException {
        var habitat = _habitats.get(key);
        if (habitat == null) throw new HabitatUnknownException(key);
        return habitat;
    }

    public Tree treeByKey(String key) throws TreeUnknownException {
        var tree = _trees.get(key);
        if (tree == null) throw new TreeUnknownException(key);
        return tree;
    }

    public Vaccine vaccineByKey(String key) throws VaccineUnknownException {
        var vaccine = _vaccines.get(key);
        if (vaccine == null) throw new VaccineUnknownException(key);
        return vaccine;
    }

    public void registerAnimal(Animal animal) {
        _animals.put(animal.getKey(), animal);
    }

    void importFile(String filename) throws ResponsibilityUnknownException, IOException, UnrecognizedEntryException, EmployeeExistsException,
            HabitatExistsException, HabitatUnknownException, AnimalExistsException, SpeciesUnknownException,
            SpeciesExistsException, TreeExistsException, TreeUnknownException, VaccineExistsException {
        var reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            var fields = line.split("\\|");
            process(fields);
        }
        _changed = true;
        reader.close();
    }

    public void addResponsibility(String key, String responsibility) throws EmployeeUnknownException, ResponsibilityUnknownException {
        employeeByKey(key).addResponsibilities(responsibility);
        _changed = true;
    }

    public void removeResponsibility(String key, String responsibility) throws EmployeeUnknownException, ResponsibilityUnknownException {
        employeeByKey(key).removeResponsibilities(responsibility);
        _changed = true;
    }

    public void changeHabitatArea(String key, int area) throws HabitatUnknownException {
        var habitat = habitatByKey(key);
        habitat.setArea(area);
        _changed = true;
    }

    public void changeHabitatInfluence(String habitatKey, String speciesKey, String influence) throws HabitatUnknownException, SpeciesUnknownException {
        var habitat = habitatByKey(habitatKey);
        var species = speciesByKey(speciesKey);
        habitat.addSpecies(species, influence);
        _changed = true;
    }

    public void addSpeciesToHabitat(String key, String speciesKey) throws HabitatUnknownException, SpeciesUnknownException {
        var habitat = habitatByKey(key);
        var species = speciesByKey(speciesKey);
        habitat.addSpecies(species, habitat.influence(species).toString());
        _changed = true;
    }

    public void removeSpeciesFromHabitat(String key, String sKey) throws HabitatUnknownException, SpeciesUnknownException {
        var habitat = habitatByKey(key);
        var species = speciesByKey(sKey);
        habitat.removeSpecies(species);
        _changed = true;
    }

    public Tree addTreeToHabitat(String habitatKey, String treeKey, String treeName, int treeAge, int treeBaseUpkeep, String treeType) throws HabitatUnknownException, TreeTypeUnknownException, TreeExistsException {
        var habitat = habitatByKey(habitatKey);
        Tree tree;
        try {
            treeByKey(treeKey);  // just to check if it exists
            throw new TreeExistsException(treeKey);
        } catch (TreeUnknownException e) {
            tree = switch (treeType) {
                case "CADUCA" -> new DeciduousTree(treeKey, treeName, treeBaseUpkeep, treeAge, _season);  //DAVID FIXME
                case "PERENE" -> new EvergreenTree(treeKey, treeName, treeBaseUpkeep, treeAge, _season);  //DAVID FIXME
                default -> throw new TreeTypeUnknownException(treeType);
            };
            habitat.addTree(tree);
            _changed = true;
        }
        return tree;
    }

    public long groundsKeepersForHabitat(Habitat habitat) {
        return _employees.values().stream().filter(e -> e instanceof GroundsKeeper gk && gk.hasResponsibility(habitat.getKey())).count();
    }

    public long veterinariansForSpecies(Species species) {
        return _employees.values().stream().filter(e -> e instanceof Veterinarian v && v.hasResponsibility(species.getKey())).count();
    }

    public Collection<Animal> showHabitatAnimals(String key) throws HabitatUnknownException {
        return habitatByKey(key).getAnimals().values();
    }

    public Collection<Tree> showHabitatTrees(String key) throws HabitatUnknownException {
        return habitatByKey(key).getTrees().values();
    }

    public List<Animal> selectAnimals(Selector<Animal> selector) {
        return _animals.values().stream().filter(selector::ok).sorted().collect(Collectors.toList());
    }

    public List<Employee> selectEmployees(Selector<Employee> selector) {
        return _employees.values().stream().filter(selector::ok).sorted().collect(Collectors.toList());
    }

    public List<Habitat> selectHabitats(Selector<Habitat> selector) {
        return _habitats.values().stream().filter(selector::ok).sorted().collect(Collectors.toList());
    }

    public List<Vaccine> selectVaccines(Selector<Vaccine> selector) {
        return _vaccines.values().stream().filter(selector::ok).sorted().collect(Collectors.toList());
    }

    public List<Vaccination> selectVaccinations(Selector<Vaccination> selector) {
        return _vaccinations.stream().filter(selector::ok).collect(Collectors.toList());
    }

    public double showAnimalSatisfaction(String key) throws AnimalUnknownException {
        return animalByKey(key).satisfaction();
    }

    public double showSatisfactionOfEmployee(String key) throws EmployeeUnknownException {
        return employeeByKey(key).satisfaction();
    }

    public double globalSatisfaction() {
        var satisfaction = 0.0;
        satisfaction += _animals.values().stream().mapToDouble(Animal::satisfaction).sum();
        satisfaction += _employees.values().stream().mapToDouble(Employee::satisfaction).sum();
        return satisfaction;
    }

    public void moveAnimal(String animalKey, String habitatKey) throws AnimalUnknownException, HabitatUnknownException {
        var animal = animalByKey(animalKey);
        var habitat = habitatByKey(habitatKey);
        var oldHabitat = animal.getHabitat();
        oldHabitat.removeAnimal(animal);
        habitat.addAnimal(animal);
        _changed = true;
    }

    public boolean vaccinateAnimal(String veterinarianKey, String animalKey, String vaccineKey)
            throws EmployeeUnknownException, AnimalUnknownException, VaccineUnknownException, VeterinarianUnknownException, CannotVaccinateException, SpeciesUnknownException {
        var employee = employeeByKey(veterinarianKey);
        if (employee instanceof Veterinarian veterinarian) {
            var animal = animalByKey(animalKey);
            var speciesKey = animal.getSpecies().getKey();
            if (!veterinarian.hasResponsibility(speciesKey))
                throw new CannotVaccinateException(veterinarianKey, speciesKey);
            var vaccine = vaccineByKey(vaccineKey);
            var vaccination = new Vaccination(veterinarian, animal, vaccine, 0);
            var species = speciesByKey(speciesKey);
            _vaccinations.add(vaccination);
            vaccine.registerApplication(vaccination);
            animal.addVaccination(vaccination);
            _changed = true;
            //DAVID FIXME add vaccination to veterinarian
            return vaccine.appropriateFor(species);
        } else {
            throw new VeterinarianUnknownException(veterinarianKey);
        }
    }

    public void process(String[] fields) throws ResponsibilityUnknownException, UnrecognizedEntryException,
            SpeciesExistsException, AnimalExistsException, EmployeeExistsException, HabitatExistsException,
            HabitatUnknownException, SpeciesUnknownException, TreeExistsException, TreeUnknownException,
            VaccineExistsException {
        switch (fields[0]) {
            case "ESPÉCIE" -> processSpecies(fields);
            case "ANIMAL" -> processAnimal(fields);
            case "TRATADOR", "VETERINÁRIO" -> processEmployee(fields);
            case "ÁRVORE" -> processTree(fields);
            case "HABITAT" -> processHabitat(fields);
            case "VACINA" -> processVaccine(fields);
            default -> throw new UnrecognizedEntryException(fields[0]);
        }
    }

    public Species processSpecies(String... fields) throws SpeciesExistsException {
        String key = fields[1], name = fields[2];
        if (_species.containsKey(key)) throw new SpeciesExistsException(key);
        var species = new Species(key, name);
        _species.put(key, species);
        _changed = true;
        return species;
    }

    // ANIMAL|id|nome|idEspécie|idHabitat
    public Animal processAnimal(String... fields) throws AnimalExistsException, SpeciesUnknownException, HabitatUnknownException {
        String key = fields[1], name = fields[2], sKey = fields[3];
        if (_animals.containsKey(key)) throw new AnimalExistsException(key);
        var species = speciesByKey(sKey);
        var animal = new Animal(key, name, species, this);
        _animals.put(key, animal);
        if (fields.length == 5) {
            var habitat = habitatByKey(fields[4]);
            habitat.addAnimal(animal);
        }
        _changed = true;
        return animal;
    }

    // TRATADOR|id|nome|idHabitat1,...,idHabitatN
    // VETERINÁRIO|id|nome|idEspécie1,...,idEspécieN
    public Employee processEmployee(String... fields) throws ResponsibilityUnknownException, UnrecognizedEntryException, EmployeeExistsException {
        String type = fields[0], key = fields[1];
        if (_employees.containsKey(key)) throw new EmployeeExistsException(key);
        var employee = switch (type) {
            case "TRATADOR" -> new GroundsKeeper(this, key, fields);
            case "VETERINÁRIO" -> new Veterinarian(this, key, fields);
            default -> throw new UnrecognizedEntryException("Invalid employee type: " + fields[0]);
        };
        _employees.put(key, employee);
        _changed = true;
        return employee;
    }

    // ÁRVORE|id|nome|idade|dificuldade|tipo
    public Tree processTree(String... fields) throws UnrecognizedEntryException, TreeExistsException {
        String key = fields[1], name = fields[2], type = fields[5];
        var age = Integer.parseInt(fields[3]);
        var baseUpkeep = Double.parseDouble(fields[4]);
        if (_trees.containsKey(key)) throw new TreeExistsException(key);
        var tree = switch (type) {
            case "CADUCA" -> new DeciduousTree(key, name, baseUpkeep, age, _season);
            case "PERENE" -> new EvergreenTree(key, name, baseUpkeep, age, _season);
            default -> throw new UnrecognizedEntryException("Invalid tree type: " + type);
        };
        _trees.put(key, tree);
        _changed = true;
        return tree;
    }

    // HABITAT|id|nome|área|idÁrvore1,...,idÁrvoreN
    public Habitat processHabitat(String... fields) throws HabitatExistsException, TreeUnknownException {
        String key = fields[1], name = fields[2], area = fields[3];
        if (_habitats.containsKey(key)) throw new HabitatExistsException(_habitats.get(key).getKey());
        var habitat = new Habitat(key, name, Integer.parseInt(area));
        if (fields.length == 5) {
            // process list of trees
            for (var treeKey : fields[4].split(","))
                habitat.addTree(treeByKey(treeKey));
        }
        _habitats.put(key, habitat);
        _changed = true;
        return habitat;
    }

    public Vaccine processVaccine(String... fields) throws VaccineExistsException, SpeciesUnknownException {
        var key = fields[1];
        if (_vaccines.containsKey(key)) throw new VaccineExistsException(key);
        var name = fields[2];
        Map<String, Species> species = new HashMap<>();
        if (fields.length > 3) {
            var speciesKeys = fields[3].split(",");
            for (var speciesKey : speciesKeys) {
                species.put(speciesKey, speciesByKey(speciesKey));
            }
        }
        var vaccine = new Vaccine(key, name, species);
        _vaccines.put(key, vaccine);
        _changed = true;
        return vaccine;
    }

    public int advanceSeason() {
        _season = _season.next();
        _habitats.values().forEach(Habitat::advanceSeason);
        _changed = true;
        return _season.ordinal();
    }

}
