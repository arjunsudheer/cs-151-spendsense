package notification.confirm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SliderConfirmNotifier implements ConfirmNotifier {
    private boolean result = false;

    @Override
    public boolean pushPrompt(String message) {
        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        Label label = new Label(message);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        HBox btnBox = new HBox(10, yesBtn, noBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, label, btnBox);
        root.setStyle(
                "-fx-background-color: #444444; -fx-padding: 15px; -fx-background-radius: 5px; -fx-border-color: #666; -fx-border-radius: 5px;");
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);

        // Center on screen
        stage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getWidth() / 2 - 150);
        stage.setY(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY());

        yesBtn.setOnAction(e -> {
            result = true;
            closeStage(stage);
        });

        noBtn.setOnAction(e -> {
            result = false;
            closeStage(stage);
        });

        // Slide up — use wrapper property for animation target
        stage.show();
        javafx.beans.property.DoubleProperty yWrapper = new javafx.beans.property.SimpleDoubleProperty(stage.getY());
        yWrapper.addListener((obs, oldV, newV) -> stage.setY(newV.doubleValue()));
        Timeline slideIn = new Timeline();
        KeyValue kv = new KeyValue(yWrapper, javafx.stage.Screen.getPrimary().getVisualBounds().getHeight() / 2 - 50);
        KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
        slideIn.getKeyFrames().add(kf);
        slideIn.play();

        stage.showAndWait();
        return result;
    }

    private void closeStage(Stage stage) {
        Timeline slideOut = new Timeline();
        KeyValue kvOut = new KeyValue(new javafx.beans.property.SimpleDoubleProperty(stage.getY()),
                javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY() + 50);
        KeyFrame kfOut = new KeyFrame(Duration.millis(300), kvOut);
        slideOut.getKeyFrames().add(kfOut);
        slideOut.setOnFinished(e -> stage.close());
        slideOut.play();
    }
}
