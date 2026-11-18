package erp.app.company;

import erp.ErpManager;
import erp.exceptions.UnknownCompanyException;

import erp.app.exceptions.UnknownCompanyKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
/**
 * Shows the clients that have bought from a company, ordered by identifier
 * (their e-mail address).
 */
class DoShowCompanyClients extends Command<ErpManager> {

    DoShowCompanyClients(ErpManager receiver) {
        super(Label.SHOW_COMPANY_CLIENTS, receiver);
        addIntegerField("companyId", Prompt.companyId());
    }

    @Override
    protected final void execute() throws CommandException {
        int companyId = integerField("companyId");

        try{
            _display.popup(_receiver.showCompanyClients(companyId));
        }
        catch (UnknownCompanyException e){
            throw new UnknownCompanyKeyException(companyId);
        }
    }

}
