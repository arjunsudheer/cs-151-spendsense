package notification.info;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class SliderInfoNotifier implements InfoNotifier {
    private static SliderInfoNotifier instance;

    private SliderInfoNotifier() {
    }

    public static SliderInfoNotifier getInstance() {
        if (instance == null) {
            instance = new SliderInfoNotifier();
        }
        return instance;
    }

    @Override
    public void pushNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle("SpendSense Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}