package erp.app.main;

import erp.ErpManager;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Advances the current date by a number of days, raising the delivery
 * notifications that the jump crosses. A non-positive number of days has no effect.
 */
class DoAdvanceDate extends Command<ErpManager> {

    DoAdvanceDate(ErpManager receiver) {
        super(Label.ADVANCE_DATE, receiver);
        addIntegerField("days", Prompt.daysToAdvance());
    }

    @Override
    protected final void execute() throws CommandException {
        int daysToAdvance = integerField("days");

        if(!(_receiver.advanceDate(daysToAdvance))){
            /* daysToAdvance <= 0,  operacao sem efeito */
        }
        else {
            /* if success, advance date and do nothing */
        }
    }

    //todo
    //! o sistema deve desenrolar as notificacoes correspondentes a cada um dos
    //! dias percorridos (ver gestao de tempo) Note-se que a situacao de uma
    //! encomenda (entregue ou nao) nao eh guardada: deriva da data actual

}
