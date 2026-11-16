package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenLookupsMenu extends Command<HotelManager> {

    DoOpenLookupsMenu(HotelManager receiver) {
        super(Label.MENU_LOOKUPS, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        (new hva.app.search.Menu(_receiver.getHotel())).open();
    }

}
