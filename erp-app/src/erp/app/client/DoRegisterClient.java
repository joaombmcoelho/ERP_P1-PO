package erp.app.client;

import erp.ErpManager;
import erp.app.exceptions.ClientRegistrationFailedException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/** Registers a client. The e-mail address is its identifier, so it must be new. */
class DoRegisterClient extends Command<ErpManager> {

    /** define the fields */
    DoRegisterClient(ErpManager receiver) {
        super(Label.REGISTER_CLIENT, receiver);
        addStringField("name", Prompt.clientName());
        addStringField("email", Prompt.clientEMail());
    }

    @Override
    protected final void execute() throws CommandException {
        /** get the fields the uilib stored  */
        String name = stringField("name");
        String email = stringField("email");

        if(!_receiver.registerClient(email, name)){
            throw new ClientRegistrationFailedException(name, email);
        }

        _display.popup(Message.registrationSuccessful(email));
    }

}