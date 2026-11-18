package erp.app.main;

import erp.ErpManager;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Shows the current date of the system.
 */
class DoDisplayDate extends Command<ErpManager> {

    DoDisplayDate(ErpManager receiver) {
        super(Label.DISPLAY_DATE, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        int date = _receiver.getCurrentDate();
        
        _display.popup(Message.currentDate(date));
    }

}
