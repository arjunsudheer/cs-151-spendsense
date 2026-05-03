package notification;

import notification.input.InputNotifier;
import notification.input.PopupInputNotifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InputNotifierTest {

    @Test
    public void testPopupInputNotifierInstance() {
        PopupInputNotifier inputNotifier = new PopupInputNotifier();
        assertNotNull(inputNotifier);
    }

    @Test
    public void testInputNotifierPolymorphism() {
        InputNotifier inputNotifier = new PopupInputNotifier();
        assertNotNull(inputNotifier);
    }
}