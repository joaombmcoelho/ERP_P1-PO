package hva.app.habitat;

import hva.Hotel;
import hva.app.TextWriter;
import hva.app.exceptions.DuplicateTreeKeyException;
import hva.app.exceptions.UnknownHabitatKeyException;
import hva.habitat.HabitatUnknownException;
import hva.habitat.TreeExistsException;
import hva.habitat.TreeTypeUnknownException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

class DoAddTreeToHabitat extends Command<Hotel> {

    DoAddTreeToHabitat(Hotel receiver) {
        super(Label.ADD_TREE_TO_HABITAT, receiver);
        addStringField("habitatKey", Prompt.habitatKey());
        addStringField("treeKey", Prompt.treeKey());
        addStringField("treeName", Prompt.treeName());
        addIntegerField("treeAge", Prompt.treeAge());
        addIntegerField("treeBaseUpkeep", Prompt.treeDifficulty());
        addOptionField("treeType", Prompt.treeType(), "PERENE", "CADUCA");
    }

    @Override
    protected void execute() throws CommandException {
        try {
            var tree = _receiver.addTreeToHabitat(stringField("habitatKey"), stringField("treeKey"),
                    stringField("treeName"), integerField("treeAge"), integerField("treeBaseUpkeep"),
                    optionField("treeType"));
            _display.addLine(tree.accept(new TextWriter()));
        } catch (HabitatUnknownException e) {
            throw new UnknownHabitatKeyException(e.getKey());
        } catch (TreeTypeUnknownException e) {
            // CANNOT HAPPEN
        } catch (TreeExistsException e) {
            throw new DuplicateTreeKeyException(e.getKey());
        }
    }

}
