package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenVaccinesMenu extends Command<HotelManager> {

    DoOpenVaccinesMenu(HotelManager receiver) {
        super(Label.MENU_VACCINES, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        (new hva.app.vaccine.Menu(_receiver.getHotel())).open();
    }

}
