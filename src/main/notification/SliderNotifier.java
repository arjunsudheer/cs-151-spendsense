package main.notification;

// sends notification slider style
public class SliderNotifier implements Notifier {
    private String lastMessage;

    @Override
    public void sendNotification(String message) {
        lastMessage = message;
        System.out.println("[SLIDER] " + message);
    }

    public String getLastMessage() {
        return lastMessage;
    }
}