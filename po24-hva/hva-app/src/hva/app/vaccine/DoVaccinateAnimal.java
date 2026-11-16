package hva.app.vaccine;

import hva.Hotel;
import hva.animal.AnimalUnknownException;
import hva.animal.SpeciesUnknownException;
import hva.app.exceptions.*;
import hva.employee.EmployeeUnknownException;
import hva.employee.VeterinarianUnknownException;
import hva.vaccine.VaccineUnknownException;
import hva.vaccine.CannotVaccinateException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoVaccinateAnimal extends Command<Hotel> {

    DoVaccinateAnimal(Hotel receiver) {
        super(Label.VACCINATE_ANIMAL, receiver);
        addStringField("vaccineKey", Prompt.vaccineKey());
        addStringField("veterinaryKey", Prompt.veterinarianKey());
        addStringField("animalKey", hva.app.animal.Prompt.animalKey());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            if (!_receiver.vaccinateAnimal(stringField("veterinaryKey"), stringField("animalKey"), stringField("vaccineKey")))
                _display.popup(Message.wrongVaccine(stringField("vaccineKey"), stringField("animalKey")));
        } catch (EmployeeUnknownException e) {
            throw new UnknownVeterinarianKeyException(stringField("veterinaryKey"));
        } catch (VeterinarianUnknownException e) {
            throw new UnknownVeterinarianKeyException(stringField("veterinaryKey"));
        } catch (AnimalUnknownException e) {
            throw new UnknownAnimalKeyException(e.getKey());
        } catch (SpeciesUnknownException e) {
            throw new UnknownSpeciesKeyException(e.getKey());
        } catch (VaccineUnknownException e) {
            throw new UnknownVaccineKeyException(e.getKey());
        } catch (CannotVaccinateException e) {
            throw new VeterinarianNotAuthorizedException(e.getVeterinarianKey(), e.getSpeciesKey());
        }
    }

}
