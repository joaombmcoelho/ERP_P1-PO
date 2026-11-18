package erp.client;


public class Default implements NotificationSystem {
    
    //! todo not implemented
    /**
     * @return notification type.
     */
    @Override 
    public NotType getNotType(){
        return NotType.ENTREGUE;
    }

    /**
     * Sends a default notification to a client.
     * 
     * @param c client.
     */
    @Override
    public void sendNot(Client c){
    }

    /**
     * Shows all default notifications of a client.
     * 
     * @param c client.
     */
    @Override
    public void showAllNotif (Client c){
    }
}