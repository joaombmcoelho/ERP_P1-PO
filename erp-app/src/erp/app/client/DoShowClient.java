package erp.app.client;

import erp.ErpManager;
import erp.exceptions.UnknownClientException;

import erp.app.exceptions.UnknownClientKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Shows one client, given its identifier (an e-mail address).
 */
class DoShowClient extends Command<ErpManager> {

    DoShowClient(ErpManager receiver) {
        super(Label.SHOW_CLIENT, receiver);
        addStringField("email", Prompt.clientEMail());
    }

    @Override
    protected final void execute() throws CommandException {
        String email = stringField("email");
        try {
            _display.popup(_receiver.showClient(email));
        }
        catch (UnknownClientException e){
            throw new UnknownClientKeyException(email);
        }
    }
}

