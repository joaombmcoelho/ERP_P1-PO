package hva.app.habitat;

import hva.Hotel;
import hva.app.TextWriter;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAllTreesInHabitat extends Command<Hotel> {

    DoShowAllTreesInHabitat(Hotel receiver) {
        super(Label.SHOW_TREES_IN_HABITAT, receiver);
        addStringField("key", Prompt.habitatKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.showHabitatTrees(stringField("key")).stream().sorted()
                    .forEach(t -> _display.addLine(t.accept(new TextWriter())));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        }
    }

}
