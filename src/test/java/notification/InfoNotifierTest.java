package notification;

import notification.info.InfoNotifier;
import notification.info.PopupInfoNotifier;
import notification.info.SliderInfoNotifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoNotifierTest {

    @Test
    public void testPopupInfoNotifierSingleton() {
        PopupInfoNotifier instance1 = PopupInfoNotifier.getInstance();
        PopupInfoNotifier instance2 = PopupInfoNotifier.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2, "PopupInfoNotifier should be a singleton");
    }

    @Test
    public void testSliderInfoNotifierSingleton() {
        SliderInfoNotifier instance1 = SliderInfoNotifier.getInstance();
        SliderInfoNotifier instance2 = SliderInfoNotifier.getInstance();
        assertNotNull(instance1);
        assertSame(instance1, instance2, "SliderInfoNotifier should be a singleton");
    }

    @Test
    public void testInfoNotifierPolymorphism() {
        InfoNotifier popup = PopupInfoNotifier.getInstance();
        assertNotNull(popup);

        InfoNotifier slider = SliderInfoNotifier.getInstance();
        assertNotNull(slider);
    }
}