package hva.app.vaccine;

import hva.Hotel;
import hva.app.TextWriter;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAllVaccines extends Command<Hotel> {

    DoShowAllVaccines(Hotel receiver) {
        super(Label.SHOW_ALL_VACCINES, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        _receiver.selectVaccines(_ -> true).stream().sorted()
                .forEach(vaccine -> _display.addLine(vaccine.accept(new TextWriter())));
    }

}
