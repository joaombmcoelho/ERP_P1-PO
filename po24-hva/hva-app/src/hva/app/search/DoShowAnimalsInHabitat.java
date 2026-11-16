package hva.app.search;

import hva.Hotel;
import hva.app.TextWriter;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAnimalsInHabitat extends Command<Hotel> {

    DoShowAnimalsInHabitat(Hotel receiver) {
        super(Label.ANIMALS_IN_HABITAT, receiver);
        addStringField("key", hva.app.habitat.Prompt.habitatKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.showHabitatAnimals(stringField("key")).stream().sorted()
                    .forEach(a -> _display.addLine(a.accept(new TextWriter())));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        }
    }

}
