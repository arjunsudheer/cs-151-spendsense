package notification.info;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

// Alternative info notifier implementation using singleton pattern
public class SliderInfoNotifier implements InfoNotifier {
    private static SliderInfoNotifier instance;

    private SliderInfoNotifier() {
    }

    // Return shared slider notifier instance
    public static SliderInfoNotifier getInstance() {
        if (instance == null) {
            instance = new SliderInfoNotifier();
        }
        return instance;
    }

    // Display notification message to the user
    @Override
    public void pushNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("SpendSense Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}