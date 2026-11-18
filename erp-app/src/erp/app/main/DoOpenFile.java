package erp.app.main;

import java.io.IOException;

import erp.ErpManager;
import erp.exceptions.UnavailableFileException;
import erp.exceptions.MissingFileAssociationException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

import erp.app.exceptions.FileOpenFailedException;

/**
 * Replaces the current state of the application with the state previously 
 * saved to a file. Asks for the file name.
 */
class DoOpenFile extends Command<ErpManager> {

    DoOpenFile(ErpManager receiver) {
        super(Label.OPEN_FILE, receiver);
        addStringField("fileToLoad", Prompt.openFile());
    }

    @Override
    protected final void execute() throws CommandException {
        /* get the fields the uilib stored  */
        String fileToLoad = stringField("fileToLoad");

        /* check if there is unsaved changes to save */
        if (_receiver.hasUnsavedChanges()){
            /* ask user if he wants to save the file before loading a new one*/
            boolean saveUnsavedChanges = Form.confirm(Prompt.saveBeforeExit());

            /* if yes, save the unsaved changes */
            if (saveUnsavedChanges){
                /* ask user the destination filename */
                String fileToSave = Form.requestString(Prompt.newSaveAs());

                try {
                    _receiver.saveAs(fileToSave);
                }
                /*  problem if filename is valid or errors in saving */
                catch (MissingFileAssociationException | IOException e){
                    e.printStackTrace();
                }
            }
            else {
                /* user didnt want to save so do nothing, load the new file */
            }
        }

        /* open (load) the file */
        try {
            _receiver.load(fileToLoad);
        }
        catch (UnavailableFileException e){
            throw new FileOpenFailedException(e);
        }
    }
}
