package notification.info;

import javafx.scene.control.Alert;

public class PopupInfoNotifier implements InfoNotifier {
    private static PopupInfoNotifier instance;

    private PopupInfoNotifier() {
    }

    public static PopupInfoNotifier getInstance() {
        if (instance == null) {
            instance = new PopupInfoNotifier();
        }
        return instance;
    }

    @Override
    public void pushNotification(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("SpendSense Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
