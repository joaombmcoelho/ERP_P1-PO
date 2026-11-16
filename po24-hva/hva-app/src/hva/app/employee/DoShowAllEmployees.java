package hva.app.employee;

import hva.Hotel;
import hva.app.TextWriter;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowAllEmployees extends Command<Hotel> {

    DoShowAllEmployees(Hotel receiver) {
        super(Label.SHOW_ALL_EMPLOYEES, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _receiver.selectEmployees(_ -> true).stream().sorted()
                .forEach(e -> _display.addLine(e.accept(new TextWriter())));
    }

}
