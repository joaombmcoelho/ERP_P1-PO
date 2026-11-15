package erp.app.company;

import erp.ErpManager;
import erp.exceptions.UnknownCompanyException;

import erp.app.exceptions.UnknownCompanyKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME maybe import classes

/**
 * Shows one company, given its identifier.
 */
class DoShowCompany extends Command<ErpManager> {

    DoShowCompany(ErpManager receiver) {
        super(Label.SHOW_COMPANY, receiver);
        addIntegerField("companyId", Prompt.companyId());
    }

    @Override
    protected final void execute() throws CommandException {
        int companyId = integerField("companyId");

        try{
            _display.popup(_receiver.showCompany(companyId));
        }
        catch (UnknownCompanyException e){
            throw new UnknownCompanyKeyException(companyId);
        }

    }

}
