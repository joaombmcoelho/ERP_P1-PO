package erp.app.company;

import erp.ErpManager;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME maybe import classes

/**
 * Changes what a company holds of a product — the amount is added to its
 * inventory and the factor replaces the previous one — starting the supply if
 * the company did not offer the product yet.
 *
 * <p>Reaching zero units only makes the product unavailable at that company: the
 * product itself stays in the system.</p>
 */
class DoChangeProductInventory extends Command<ErpManager> {

    DoChangeProductInventory(ErpManager receiver) {
        super(Label.CHANGE_PRODUCT_INVENTORY, receiver);

        addIntegerField("companyId", Prompt.companyId());
        addIntegerField("productId", erp.app.product.Prompt.productId());
        addIntegerField("amountToUpdate", Prompt.amountToUpdate());
        addRealField("priceFactor", Prompt.priceFactor());
    }

    @Override
    protected final void execute() throws CommandException {
        int companyId = integerField("companyId");
        int productId = integerField("productId");
        int amountToUpdate = integerField("ammountToUpdate");
        double priceFactor = realField("priceFactor");

        if(_receiver.changeInventory(companyId, productId, 
                                        amountToUpdate, priceFactor)){
            /** inventory change successful */
            _display.popup(Message.supplySuccessful(companyId, productId));
        }
        else {
            /** error case */
            _display.popup(Message.cannotChangeInventory(productId));
        }
    }

}
