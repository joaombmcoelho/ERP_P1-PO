package hva.app.search;

import hva.Hotel;
import hva.app.TextWriter;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowWrongVaccinations extends Command<Hotel> {

    DoShowWrongVaccinations(Hotel receiver) {
        super(Label.WRONG_VACCINATIONS, receiver);
    }

    @Override
    protected void execute() throws CommandException {
        _receiver.selectVaccinations(v -> !v.getVaccine().appropriateFor(v.getAnimal().getSpecies())).stream()
                .forEach(v -> _display.addLine(v.accept(new TextWriter())));
    }

}
