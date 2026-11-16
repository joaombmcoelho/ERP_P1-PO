package hva.app.habitat;

import hva.Hotel;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoChangeHabitatArea extends Command<Hotel> {

    DoChangeHabitatArea(Hotel receiver) {
        super(Label.CHANGE_HABITAT_AREA, receiver);
        addStringField("key", Prompt.habitatKey());
        addIntegerField("area", Prompt.habitatArea());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.changeHabitatArea(stringField("key"), integerField("area"));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        }
    }

}
