package hva.app.main;

import hva.HotelManager;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoOpenEmployeesMenu extends Command<HotelManager> {

    DoOpenEmployeesMenu(HotelManager receiver) {
        super(Label.MENU_EMPLOYEES, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        (new hva.app.employee.Menu(_receiver.getHotel())).open();
    }

}
