package erp.app.product;

import erp.ErpManager;
import erp.exceptions.InvalidSearchFilterException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME maybe import classes

/** Searches the product registry by one of the criteria the core offers. */
class DoPerformSearch extends Command<ErpManager> {

    DoPerformSearch(ErpManager receiver) {
        super(Label.PERFORM_SEARCH, receiver);
        addStringField("criterion", Prompt.searchCriterion());
    }

    @Override
    protected final void execute() throws CommandException {
        String criterion = stringField("criterion");

        try{
        _display.popup(_receiver.search(criterion));
        }
        catch (InvalidSearchFilterException e){}
    }

}
