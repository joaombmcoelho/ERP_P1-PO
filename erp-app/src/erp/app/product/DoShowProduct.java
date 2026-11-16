package erp.app.product;

import erp.ErpManager;
import erp.exceptions.UnknownProductException;

import erp.app.exceptions.UnknownProductKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME maybe import classes

/**
 * Shows one product, given its identifier.
 */
class DoShowProduct extends Command<ErpManager> {

    DoShowProduct(ErpManager receiver) {
        super(Label.SHOW_PRODUCT, receiver);
        addIntegerField("productId", Prompt.productId());
    }

    @Override
    protected final void execute() throws CommandException {
        int productId = integerField("productId");

        try{
            _display.popup(_receiver.showProduct(productId));
        }
        catch (UnknownProductException e){
            throw new UnknownProductKeyException(productId);
        }
    }

}
