package notification.info;

import javafx.scene.control.Alert;

// Popup-based information notification using singleton pattern
public class PopupInfoNotifier implements InfoNotifier {
    private static PopupInfoNotifier instance;

    private PopupInfoNotifier() {
    }

    // Return shared popup notifier instance
    public static PopupInfoNotifier getInstance() {
        if (instance == null) {
            instance = new PopupInfoNotifier();
        }
        return instance;
    }

    // Display information popup message
    @Override
    public void pushNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SpendSense Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
