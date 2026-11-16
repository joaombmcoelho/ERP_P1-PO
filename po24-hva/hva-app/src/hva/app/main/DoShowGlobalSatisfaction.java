package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowGlobalSatisfaction extends Command<HotelManager> {

    DoShowGlobalSatisfaction(HotelManager receiver) {
        super(Label.SHOW_GLOBAL_SATISFACTION, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        _display.addLine(Math.round(_receiver.globalSatisfaction()));
    }

}
