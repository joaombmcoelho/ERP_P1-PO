package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenAnimalsMenu extends Command<HotelManager> {

    DoOpenAnimalsMenu(HotelManager receiver) {
        super(Label.MENU_ANIMALS, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        (new hva.app.animal.Menu(_receiver.getHotel())).open();
    }

}
