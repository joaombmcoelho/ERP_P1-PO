package erp.app.company;

import erp.ErpManager;
import erp.exceptions.UnknownCompanyException;

import erp.app.exceptions.UnknownCompanyKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/** Shows what a company supplies: units, price factor and selling price. */
class DoShowCompanyProducts extends Command<ErpManager> {

    DoShowCompanyProducts(ErpManager receiver) {
        super(Label.SHOW_COMPANY_PRODUCTS, receiver);
        addIntegerField("companyId", Prompt.companyId());
    }

    @Override
    protected final void execute() throws CommandException {
        int companyId = integerField("companyId");

        try{
            _display.popup(_receiver.showCompanyProducts(companyId));
        }
        catch (UnknownCompanyException e){
            throw new UnknownCompanyKeyException(companyId);
        }
    }
}
