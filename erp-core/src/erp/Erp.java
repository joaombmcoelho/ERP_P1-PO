package erp;

import erp.exceptions.*;
import java.io.IOException;
import java.io.Serializable;

//imported by me
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

//import this to use
import erp.purchase.TransportMethod;
import erp.company.Company;
import erp.client.Client;
import erp.product.Product;
import erp.company.ProductInventory;

/** The whole ERP system: holds all the state and the domain logic. */
class Erp implements Serializable {

    @java.io.Serial
    private static final long serialVersionUID = 202608261200L;

    //FIXME maybe define attributes
    //FIXME maybe implement constructor
    //FIXME maybe implement methods

    // --- text file import --------------------------------------------------

    void importFile(String filename) throws UnrecognizedEntryException, IOException
                       /*TODO  FIXME maybe other exceptions */ {
        
        List<String> fileLines = Files.readAllLines(Path.of(filename));
        int num_lines = fileLines.size();

        for(int i = 0; i < num_lines; i++){
            parseLine(fileLines.get(i));
        }
    }

    private void parseLine(String line) throws UnrecognizedEntryException {

        String[] lineContent;

        line = line.trim();
        lineContent = line.split(":");

        try{
            switch (lineContent[0]) {
                case "RATE"         -> registerTransportMethod(lineContent[1], lineContent[2], lineContent[3]);
                case "COMPANY"      -> registerCompany(lineContent[1]);
                case "CLIENT"       -> registerClient(lineContent[1], lineContent[2]);
                case "NATIONAL"     -> registerNationalProduct(lineContent[1], lineContent[2], lineContent[3], lineContent[4]);
                case "IMPORTED"     -> registerImportedProduct(lineContent[1], lineContent[2], lineContent[3], lineContent[4], lineContent[5], lineContent[6]);
                case "SUPPLY"       -> registerProductInCompany(lineContent[1], lineContent[2], lineContent[3], lineContent[4]);
                
                default             -> throw new UnrecognizedEntryException(line);
            }
        }
        catch (Exception e) {   //TODO SEE WHAT EXCEPTION
            throw new UnrecognizedEntryException(line, e);
        }
    }
}