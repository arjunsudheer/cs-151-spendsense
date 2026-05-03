package notification;

import notification.confirm.ConfirmNotifier;
import notification.confirm.PopupConfirmNotifier;
import notification.confirm.SliderConfirmNotifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConfirmNotifierTest {

    @Test
    public void testConfirmNotifierInstances() {
        PopupConfirmNotifier popup = new PopupConfirmNotifier();
        assertNotNull(popup);

        SliderConfirmNotifier slider = new SliderConfirmNotifier();
        assertNotNull(slider);
    }

    @Test
    public void testConfirmNotifierPolymorphism() {
        ConfirmNotifier popup = new PopupConfirmNotifier();
        assertNotNull(popup);

        ConfirmNotifier slider = new SliderConfirmNotifier();
        assertNotNull(slider);
    }
}