package hva.app.employee;

import hva.Hotel;
import hva.app.exceptions.DuplicateEmployeeKeyException;
import hva.employee.EmployeeExistsException;
import hva.vaccine.ResponsibilityUnknownException;
import hva.exceptions.UnrecognizedEntryException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoRegisterEmployee extends Command<Hotel> {

    DoRegisterEmployee(Hotel receiver) {
        super(Label.REGISTER_EMPLOYEE, receiver);
        addStringField("key", Prompt.employeeKey());
        addStringField("name", Prompt.employeeName());
        addOptionField("type", Prompt.employeeType(), "VET", "TRT");
    }

    @Override
    protected void execute() throws CommandException {
        try {
            var type = switch (optionField("type")) {
                case "VET" -> "VETERINÁRIO";
                case "TRT" -> "TRATADOR";
                default -> throw new UnrecognizedEntryException(optionField("type"));
            };
            _receiver.processEmployee(type, stringField("key"), stringField("name"));
        } catch (EmployeeExistsException e) {
            throw new DuplicateEmployeeKeyException(e.getKey());
        } catch (UnrecognizedEntryException _) {
            // CANNOT HAPPEN
        } catch (ResponsibilityUnknownException _) {
            // CANNOT HAPPEN
        }
    }

}
