package main.notification;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// tests SliderNotifier
public class SliderNotifierTest {

    @Test
    public void sendNotificationStoresLastMessage() {
        SliderNotifier notifier = new SliderNotifier();

        notifier.sendNotification("Food limit reached");

        assertEquals("Food limit reached", notifier.getLastMessage());
    }
}