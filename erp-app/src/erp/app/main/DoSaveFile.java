package erp.app.main;

import java.io.IOException;

import erp.ErpManager;
import erp.exceptions.MissingFileAssociationException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.forms.Form;
//FIXME maybe import classes

/**
 * Saves the current state of the application to a file using the serialization
 * mechanism. If no file is currently associated with the application, asks the user for
 * the name of the file to save to.
 */
class DoSaveFile extends Command<ErpManager> {

    DoSaveFile(ErpManager receiver) {
        super(Label.SAVE_FILE, receiver);
    }

    @Override
    protected final void execute() {

        /* only save if has unsaved changes */
        if (_receiver.hasUnsavedChanges()){
            try {
                _receiver.save();
            }
            /**user cant save if filename is not defined */
            catch (MissingFileAssociationException e1) {
                /* ask user the filename */
                String filename = Form.requestString(Prompt.newSaveAs());

                try {
                    _receiver.saveAs(filename);
                }
                /*  problem if filename is valid or errors in saving */
                catch (MissingFileAssociationException | IOException e3){
                    e3.printStackTrace();
                }
            }
            /* if there is a problem in saving */
            catch (IOException e2){
                e2.printStackTrace();
            }
        }
    }

}
