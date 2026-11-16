package hva.app.habitat;

import hva.Hotel;
import hva.animal.SpeciesUnknownException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.app.exceptions.UnknownSpeciesKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoChangeHabitatInfluence extends Command<Hotel> {

    DoChangeHabitatInfluence(Hotel receiver) {
        super(Label.CHANGE_HABITAT_INFLUENCE, receiver);
        addStringField("habitatKey", Prompt.habitatKey());
        addStringField("speciesKey", hva.app.animal.Prompt.speciesKey());
        addOptionField("influence", Prompt.habitatInfluence(), "NEG", "NEU", "POS");
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.changeHabitatInfluence(stringField("habitatKey"), stringField("speciesKey"), optionField("influence"));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (SpeciesUnknownException e) {
            throw new UnknownSpeciesKeyException(e.getKey());
        }
    }

}
