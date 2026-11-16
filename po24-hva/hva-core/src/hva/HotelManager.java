package hva;

import hva.animal.*;
import hva.employee.*;
import hva.exceptions.*;
import hva.habitat.*;
import hva.vaccine.*;

import java.io.*;

public class HotelManager {

    private Hotel _hotel = new Hotel();

    public void save() throws FileNotFoundException, MissingFileAssociationException, IOException {
        String filename = _hotel.getFilename();
        if (filename == null) throw new MissingFileAssociationException();
//              if (!changed())
//                      return;
        try (var oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
            oos.writeObject(_hotel);
            _hotel.setChanged(false);
        }
    }

    public void saveAs(String filename) throws FileNotFoundException, MissingFileAssociationException, IOException {
        _hotel.setFilename(filename);
        save();
    }

    public void load(String filename) throws UnavailableFileException {
        try (var ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))) {
            _hotel = (Hotel) ois.readObject();
            _hotel.setFilename(filename); // update filename
            _hotel.setChanged(false);
        } catch (IOException | ClassNotFoundException _) {
            throw new UnavailableFileException(filename);
        }
    }

    public void importFile(String filename) throws ImportFileException {
        try {
            _hotel.importFile(filename);
        } catch (UnrecognizedEntryException | IOException | EmployeeExistsException | HabitatExistsException |
                 HabitatUnknownException | AnimalExistsException | SpeciesUnknownException | SpeciesExistsException |
                 TreeExistsException | TreeUnknownException | VaccineExistsException | ResponsibilityUnknownException e) {
            throw new ImportFileException(filename, e);
        }
    }

    public boolean changed() {
        return _hotel.changed();
    }

    public Hotel getHotel() {
        return _hotel;
    }

    public void reset() {
        _hotel = new Hotel();
    }

    public double globalSatisfaction() {
        return _hotel.globalSatisfaction();
    }

    public int advanceSeason() {
        return _hotel.advanceSeason();
    }
}
