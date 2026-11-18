package erp.app.product;

import erp.ErpManager;
import erp.exceptions.InvalidSearchFilterException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/** Searches the product registry by one of the criteria the core offers. */
class DoPerformSearch extends Command<ErpManager> {

    DoPerformSearch(ErpManager receiver) {
        super(Label.PERFORM_SEARCH, receiver);
    }

    @Override
    protected final void execute() throws CommandException {
        String criterion = "";

        /* need to ask the search Criterion until user gets it right */
        while (true) {
            criterion = Form.requestString(Prompt.searchCriterion());
            if (_receiver.searchFilterKeys().contains(criterion)) {
                break;  /* found the right key, stop asking in loop */
            }
        }

        String term = Form.requestString(Prompt.searchTerm());

        try {
            _display.popup(_receiver.search(criterion, term));
        } catch (InvalidSearchFilterException e) {
            e.printStackTrace();
            /* never happens */
        }
    }

}
