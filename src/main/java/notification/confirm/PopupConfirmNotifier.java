package notification.confirm;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

// Standard popup confirmation dialog
public class PopupConfirmNotifier implements ConfirmNotifier {

    // Show confirmation popup and return user choice
    @Override
    public boolean pushPrompt(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SpendSense Confirmation");
        alert.setHeaderText(null);
        alert.setContentText(message);

        return alert.showAndWait()
                .filter(button -> button == ButtonType.OK)
                .isPresent();
    }
}