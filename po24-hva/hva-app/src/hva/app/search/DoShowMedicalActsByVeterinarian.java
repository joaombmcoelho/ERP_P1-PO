package hva.app.search;

import hva.Hotel;
import hva.app.exceptions.UnknownEmployeeKeyException;
import hva.app.exceptions.UnknownVeterinarianKeyException;
import hva.employee.Employee;
import hva.employee.EmployeeUnknownException;
import hva.employee.Veterinarian;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowMedicalActsByVeterinarian extends Command<Hotel> {

    DoShowMedicalActsByVeterinarian(Hotel receiver) {
        super(Label.MEDICAL_ACTS_BY_VET, receiver);
        addStringField("key", hva.app.employee.Prompt.employeeKey());
    }

    @Override
    protected void execute() throws CommandException {
        var key = stringField("key");
        Employee employee;
        try {
            employee = _receiver.employeeByKey(key);
        } catch (EmployeeUnknownException e) {
            throw new UnknownVeterinarianKeyException(e.getKey());
        }
        if (!(employee instanceof Veterinarian)) throw new UnknownVeterinarianKeyException(key);
        _receiver.selectVaccinations(v -> v.getVeterinarian().getKey().equalsIgnoreCase(key)).stream()
                .forEach(v -> _display.addLine(v.accept(new hva.app.TextWriter())));
    }

}
