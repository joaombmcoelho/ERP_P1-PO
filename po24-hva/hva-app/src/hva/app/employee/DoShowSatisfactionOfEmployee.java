package hva.app.employee;

import hva.Hotel;
import hva.app.exceptions.UnknownEmployeeKeyException;
import hva.employee.EmployeeUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowSatisfactionOfEmployee extends Command<Hotel> {

    DoShowSatisfactionOfEmployee(Hotel receiver) {
        super(Label.SHOW_SATISFACTION_OF_EMPLOYEE, receiver);
        addStringField("key", Prompt.employeeKey());
    }

    @Override
    protected void execute() throws CommandException {
        try {
            var satisfaction = Math.round(_receiver.showSatisfactionOfEmployee(stringField("key")));
            _display.addLine(satisfaction);
        } catch (EmployeeUnknownException e) {
            throw new UnknownEmployeeKeyException(e.getKey());
        }
    }

}
