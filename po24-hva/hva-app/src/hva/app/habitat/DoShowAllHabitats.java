package hva.app.habitat;

import hva.Hotel;
import hva.app.TextWriter;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAllHabitats extends Command<Hotel> {

    DoShowAllHabitats(Hotel receiver) {
        super(Label.SHOW_ALL_HABITATS, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _receiver.selectHabitats(_ -> true).stream().sorted()
                .forEach(h -> _display.addLine(h.accept(new TextWriter())));
    }

}
