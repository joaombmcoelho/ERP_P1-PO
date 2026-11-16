package erp.app.product;

import erp.ErpManager;
import erp.exceptions.InvalidProductFieldsException;

import erp.app.exceptions.ProductRegistrationFailedException;

import java.util.List;
import java.util.ArrayList;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;
//FIXME maybe import classes

/**
 * Registers a product in the system's global registry. The extra fields of the
 * imported type are only asked for once that type is chosen, so a new product
 * type only adds its own branch here.
 */
class DoRegisterProduct extends Command<ErpManager> {

    DoRegisterProduct(ErpManager receiver) {
        super(Label.REGISTER_PRODUCT, receiver);
        addStringField("type", Prompt.productType());
        addStringField("name", Prompt.productName());
        addIntegerField("base price", Prompt.basePrice());
        addStringField("category", Prompt.productCategory());
        addIntegerField("base delay", Prompt.baseDelay());
    }

    @Override
    protected final void execute() throws CommandException {
        String productType = stringField("type");
        String productName = stringField("name");
        int basePrice = integerField("base price");
        String productCategory = stringField("category");
        int baseDelay = integerField ("base delay");

        String[] extraArgs = {};

        if (productType.equals("IMPORTADO")){
            extraArgs = new String[]{
                Form.requestString(Prompt.originCountry()),
                Form.requestString(Prompt.customsTax())
            };
        }

        try {
            int productId = _receiver.registerProduct(productType, productName, basePrice,
                                                        productCategory, baseDelay, extraArgs);
            _display.popup(Message.registrationSuccessful(productId));
        }

        catch (InvalidProductFieldsException e){
            throw new ProductRegistrationFailedException(productName);
        }
    }

}
