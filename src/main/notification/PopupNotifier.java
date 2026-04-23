package main.notification;

// sends notification popup style
public class PopupNotifier implements Notifier {
    private String lastMessage;

    @Override
    public void sendNotification(String message) {
        lastMessage = message;
        System.out.println("[POPUP] " + message);
    }

    public String getLastMessage() {

        return lastMessage;
    }
}