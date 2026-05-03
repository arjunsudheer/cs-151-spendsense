package notification.input;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import java.util.Optional;

public class PopupInputNotifier implements InputNotifier {

    @Override
    public Optional<Pair<String, String>> pushInputPrompt(
            String title,
            String header,
            String field1Name,
            String field2Name
    ) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);

        ButtonType submitButtonType = new ButtonType("Submit", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        TextField field1 = new TextField();
        field1.setPromptText(field1Name);

        TextField field2 = new TextField();
        field2.setPromptText(field2Name);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        grid.add(new Label(field1Name + ":"), 0, 0);
        grid.add(field1, 1, 0);
        grid.add(new Label(field2Name + ":"), 0, 1);
        grid.add(field2, 1, 1);

        Node submitButton = dialog.getDialogPane().lookupButton(submitButtonType);
        submitButton.setDisable(true);

        field1.textProperty().addListener((obs, oldValue, newValue) ->
                submitButton.setDisable(newValue.trim().isEmpty() || field2.getText().trim().isEmpty())
        );

        field2.textProperty().addListener((obs, oldValue, newValue) ->
                submitButton.setDisable(newValue.trim().isEmpty() || field1.getText().trim().isEmpty())
        );

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(field1::requestFocus);

        dialog.setResultConverter(button -> {
            if (button == submitButtonType) {
                return new Pair<>(field1.getText().trim(), field2.getText().trim());
            }
            return null;
        });

        return dialog.showAndWait();
    }
}