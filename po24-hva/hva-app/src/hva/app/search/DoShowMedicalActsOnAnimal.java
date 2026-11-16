package hva.app.search;

import hva.Hotel;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowMedicalActsOnAnimal extends Command<Hotel> {

    DoShowMedicalActsOnAnimal(Hotel receiver) {
        super(Label.MEDICAL_ACTS_ON_ANIMAL, receiver);
        addStringField("key", hva.app.animal.Prompt.animalKey());
    }

    @Override
    protected void execute() throws CommandException {
        var key = stringField("key");
        try {
            _receiver.animalByKey(key);
        } catch (hva.animal.AnimalUnknownException e) {
            throw new hva.app.exceptions.UnknownAnimalKeyException(e.getKey());
        }
        _receiver.selectVaccinations(v -> v.getAnimal().getKey().equalsIgnoreCase(key)).stream()
                .forEach(v -> _display.addLine(v.accept(new hva.app.TextWriter())));
    }

}
