package erp.app.purchase;

import erp.ErpManager;
import erp.basket.Basket;
import erp.exceptions.UnknownClientException;
import erp.exceptions.UnknownCompanyException;

import erp.app.exceptions.UnknownClientKeyException;
import erp.app.exceptions.UnknownCompanyKeyException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME maybe import classes

/**
 * Starts (or resumes) a purchase: it asks whose basket, at which company, and
 * opens the basket menu over it.
 *
 * <p>The purchase itself is not made here — it is the basket menu's
 * {@code Finalizar compra} that turns the basket into a purchase. Since the
 * basket belongs to the client, leaving the menu without checking out keeps
 * everything for the next visit.</p>
 */
class DoDoPurchase extends Command<ErpManager> {

    DoDoPurchase(ErpManager receiver) {
        super(Label.DO_PURCHASE, receiver);
        addStringField("email", erp.app.client.Prompt.clientEMail());
        addIntegerField("companyId", erp.app.company.Prompt.companyId());
    }

    @Override
    protected final void execute() throws CommandException {
        String email = stringField("email");
        int companyId = integerField("companyId");

        try{
            Basket basket = _receiver.getBasket(email, companyId);
            (new erp.app.basket.Menu(_receiver, basket)).open();
        }

        catch (UnknownClientException e){
            throw new UnknownClientKeyException(email);
        }

        catch (UnknownCompanyException e){
            throw new UnknownCompanyKeyException(companyId);
        }
    }

} 
