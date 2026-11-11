package erp.client;

public interface NotificationSystem {

    public NotType getNotType();
    public void sendNot(Client c);
    public void showAllNotif (Client c);

}