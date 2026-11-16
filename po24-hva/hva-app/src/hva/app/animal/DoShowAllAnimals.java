package hva.app.animal;

import hva.Hotel;
import hva.app.TextWriter;
import pt.tecnico.uilib.menus.Command;

class DoShowAllAnimals extends Command<Hotel> {

    DoShowAllAnimals(Hotel receiver) {
        super(Label.SHOW_ALL_ANIMALS, receiver);
    }

    @Override
    protected final void execute() {
        _receiver.selectAnimals(_ -> true).stream().sorted().forEach(a -> _display.addLine(a.accept(new TextWriter())));
    }

}
