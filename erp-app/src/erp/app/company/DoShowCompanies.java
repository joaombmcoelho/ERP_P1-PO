package erp.app.company;

import erp.ErpManager;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
/**
 * Shows every company, ordered by identifier.
 */
class DoShowCompanies extends Command<ErpManager> {

    /* define the fields */
    DoShowCompanies(ErpManager receiver) {
        super(Label.SHOW_COMPANIES, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        _display.popup(_receiver.showAllCompanies());
    }

}
