package erp;

import erp.exceptions.*;
import java.io.*;
//my imports:
import java.io.ObjectOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

//FIXME maybe import classes

/**
 * The façade of the ERP core: the single entry point through which the
 * application interacts with the domain.
 *
 * <p><b>Façade</b> pattern.</p>
 */
public class ErpManager {

    /** The object doing all the actual work. */
    private Erp _erp = new Erp();
    private String _filename = "";  /** added for save */

    //FIXME maybe define constructors

    // --- persistence -------------------------------------------------------

    public void save() throws MissingFileAssociationException, IOException {

        if (_filename == null || _filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        /** using try with resources */
        try (ObjectOutputStream oos =   new ObjectOutputStream(
                                        new BufferedOutputStream(
                                        new FileOutputStream(_filename)))){
            oos.writeObject(_erp);
        }
    }

    public void saveAs(String filename) throws MissingFileAssociationException, IOException {
        
        if (filename == null || filename.isEmpty()){
            throw new MissingFileAssociationException();
        }

        _filename = filename;
        
        try{
            save();
        } catch (MissingFileAssociationException e){
            e.printStackTrace();
        }
    }

    public void load(String filename) throws UnavailableFileException {
        
        if (filename == null || filename.isEmpty()){
            throw new UnavailableFileException(filename);
        }

        /** using try with resources */
        try (ObjectInputStream ois =    new ObjectInputStream(
                                        new BufferedInputStream(
                                        new FileInputStream(filename)))){
            /** attatch the new filename and erp to the manager */
            _erp = (Erp) ois.readObject();
            _filename = filename;
        }
        catch(IOException | ClassNotFoundException e){
            throw new UnavailableFileException(filename);
        }

    }

    public void importFile(String filename) throws ImportFileException {
        try {
            if (filename != null && !filename.isEmpty())
                _erp.importFile(filename);
        } catch (IOException | UnrecognizedEntryException /* FIXME maybe other exceptions */ e) {
            throw new ImportFileException(filename, e);
        }
    }

    //FIXME implement other methods

}
