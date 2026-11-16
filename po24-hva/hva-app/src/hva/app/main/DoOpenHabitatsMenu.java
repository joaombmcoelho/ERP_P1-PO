package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenHabitatsMenu extends Command<HotelManager> {

    DoOpenHabitatsMenu(HotelManager receiver) {
        super(Label.MENU_HABITATS, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        (new hva.app.habitat.Menu(_receiver.getHotel())).open();
    }

}
