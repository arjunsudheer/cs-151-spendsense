package main.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests PopupNotifier
public class PopupNotifierTest {

    @Test
    public void sendNotificationStoresLastMessage() {
        PopupNotifier notifier = new PopupNotifier();

        notifier.sendNotification("Budget exceeded");

        assertEquals("Budget exceeded", notifier.getLastMessage());
    }
}