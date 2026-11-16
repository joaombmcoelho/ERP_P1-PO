package hva.app.animal;

import hva.Hotel;
import hva.animal.AnimalExistsException;
import hva.animal.SpeciesExistsException;
import hva.animal.SpeciesUnknownException;
import hva.app.exceptions.DuplicateAnimalKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterAnimal extends Command<Hotel> {

    DoRegisterAnimal(Hotel receiver) {
        super(Label.REGISTER_ANIMAL, receiver);
        addStringField("animalKey", Prompt.animalKey());
        addStringField("animalName", Prompt.animalName());
        addStringField("speciesKey", Prompt.speciesKey());
        addStringField("habitatKey", hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            try {
                // Check if species exists
                _receiver.speciesByKey(stringField("speciesKey"));
            } catch (SpeciesUnknownException e) {
                var speciesName = Form.requestString(Prompt.speciesName());
                try {
                    _receiver.processSpecies("ESPÉCIE", stringField("speciesKey"), speciesName);
                } catch (SpeciesExistsException _) {
                    // CANNOT HAPPEN
                }
            }
            _receiver.processAnimal("ANIMAL", stringField("animalKey"), stringField("animalName"), stringField("speciesKey"), stringField("habitatKey"));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (AnimalExistsException e) {
            throw new DuplicateAnimalKeyException(e.getKey());
        } catch (SpeciesUnknownException _) {
            // CANNOT HAPPEN
        }
    }

}
