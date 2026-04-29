package notification;

import org.junit.jupiter.api.Test;

import notification.confirm.PopupConfirmNotifier;
import notification.confirm.SliderConfirmNotifier;

import static org.junit.jupiter.api.Assertions.*;

public class ConfirmNotifierTest {

    @Test
    public void testConfirmNotifierInstances() {
        // Just verifying instantiation works without throwing exceptions
        PopupConfirmNotifier popup = new PopupConfirmNotifier();
        assertNotNull(popup);

        SliderConfirmNotifier slider = new SliderConfirmNotifier();
        assertNotNull(slider);
    }
}
