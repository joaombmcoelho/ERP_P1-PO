package erp.client;

/**
 * Defines a notification system.
 */
public interface NotificationSystem {

    //! todo not used
    /**
     * @return notification type.
     */
    public NotType getNotType();

    /**
     * Sends a notification to a client.
     * 
     * @param c client.
     */
    public void sendNot(Client c);

    /**
     * Shows all the notifications of a client.
     * 
     * @param c client.
     */
    public void showAllNotif (Client c);

}