package erp.app.company;

import erp.ErpManager;
import erp.exceptions.InvalidCompanyNameException;

import erp.app.exceptions.CompanyRegistrationFailedException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Registers a company; its identifier is assigned automatically.
 */
class DoRegisterCompany extends Command<ErpManager> {

    /* define the fields */
    DoRegisterCompany(ErpManager receiver) {
        super(Label.REGISTER_COMPANY, receiver);
        addStringField("name", Prompt.companyName());
    }

    @Override
    protected final void execute() throws CommandException {
        /* get the fields */
        String companyName = stringField("name");

        try{
            /* registration successful */
            int idCompany = _receiver.registerCompany(companyName);
            _display.popup(Message.registrationSuccessful(idCompany));
        }
        catch (InvalidCompanyNameException e){
            /* if name is an empty string */
            throw new CompanyRegistrationFailedException(companyName);
        }
    }

}
