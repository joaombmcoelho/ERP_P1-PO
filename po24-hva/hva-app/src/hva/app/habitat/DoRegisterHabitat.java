package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.DuplicateHabitatKeyException;
import hva.app.exceptions.UnknownTreeKeyException;
import hva.habitat.HabitatExistsException;
import hva.habitat.TreeUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterHabitat extends Command<Hotel> {

    DoRegisterHabitat(Hotel receiver) {
        super(Label.REGISTER_HABITAT, receiver);
        addStringField("key", Prompt.habitatKey());
        addStringField("name", Prompt.habitatName());
        addStringField("area", Prompt.habitatArea());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.processHabitat("HABITAT", stringField("key"), stringField("name"), stringField("area"));
        } catch (HabitatExistsException e) {
            throw new DuplicateHabitatKeyException(stringField("key"));
        } catch (TreeUnknownException e) {
            throw new UnknownTreeKeyException(e.getKey());
        }
    }

}
