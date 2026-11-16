package hva.app.animal;

import hva.Hotel;
import hva.animal.AnimalUnknownException;
import hva.app.exceptions.UnknownAnimalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoShowSatisfactionOfAnimal extends Command<Hotel> {

    DoShowSatisfactionOfAnimal(Hotel receiver) {
        super(Label.SHOW_SATISFACTION_OF_ANIMAL, receiver);
        addStringField("key", Prompt.animalKey());
    }

    @Override
    protected final void execute() throws CommandException {
        try {
            var satisfaction = Math.round(_receiver.showAnimalSatisfaction(stringField("key")));
            _display.addLine(satisfaction);
        } catch (AnimalUnknownException e) {
            throw new UnknownAnimalKeyException(e.getKey());
        }
    }

}