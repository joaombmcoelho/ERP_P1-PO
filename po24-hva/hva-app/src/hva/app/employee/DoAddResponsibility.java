package hva.app.employee;

import hva.Hotel;
import hva.app.exceptions.NoResponsibilityException;
import hva.app.exceptions.UnknownEmployeeKeyException;
import hva.employee.EmployeeUnknownException;
import hva.vaccine.ResponsibilityUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoAddResponsibility extends Command<Hotel> {

    DoAddResponsibility(Hotel receiver) {
        super(Label.ADD_RESPONSABILITY, receiver);
        addStringField("key", Prompt.employeeKey());
        addStringField("responsibility", Prompt.responsibilityKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            _receiver.addResponsibility(stringField("key"), stringField("responsibility"));
        } catch (EmployeeUnknownException e) {
            throw new UnknownEmployeeKeyException(e.getKey());
        } catch (ResponsibilityUnknownException e) {
            throw new NoResponsibilityException(e.getKey(), e.getResponsibilityKey());
        }
    }

}
