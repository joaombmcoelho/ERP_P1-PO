package hva.app.animal;

import hva.Hotel;
import hva.animal.AnimalUnknownException;
import hva.app.exceptions.UnknownAnimalKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoTransferToHabitat extends Command<Hotel> {

    DoTransferToHabitat(Hotel hotel) {
        super(Label.TRANSFER_ANIMAL_TO_HABITAT, hotel);
        addStringField("animalKey", Prompt.animalKey());
        addStringField("habitatKey", hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            _receiver.moveAnimal(stringField("animalKey"), stringField("habitatKey"));
        } catch (AnimalUnknownException e) {
            throw new UnknownAnimalKeyException(e.getKey());
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        }
    }

}
