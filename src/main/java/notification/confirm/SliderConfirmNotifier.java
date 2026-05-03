package notification.confirm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SliderConfirmNotifier implements ConfirmNotifier {
    private boolean result;

    @Override
    public boolean pushPrompt(String message) {
        result = false;

        Stage stage = new Stage();
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(300);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        Button yesBtn = new Button("Yes");
        Button noBtn = new Button("No");

        HBox btnBox = new HBox(10, yesBtn, noBtn);
        btnBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10, label, btnBox);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
                "-fx-background-color: #444444;" +
                        "-fx-padding: 15px;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #666666;" +
                        "-fx-border-radius: 8px;"
        );

        Scene scene = new Scene(root);
        scene.setFill(null);
        stage.setScene(scene);

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenBottom = Screen.getPrimary().getVisualBounds().getMaxY();

        stage.setX(screenWidth / 2 - 170);
        stage.setY(screenBottom + 50);

        yesBtn.setOnAction(e -> {
            result = true;
            closeStage(stage);
        });

        noBtn.setOnAction(e -> {
            result = false;
            closeStage(stage);
        });

        stage.show();

        DoubleProperty yWrapper = new SimpleDoubleProperty(stage.getY());
        yWrapper.addListener((obs, oldV, newV) -> stage.setY(newV.doubleValue()));

        Timeline slideIn = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(yWrapper, screenBottom - 150)
                )
        );

        slideIn.play();

        stage.showAndWait();
        return result;
    }

    private void closeStage(Stage stage) {
        double screenBottom = Screen.getPrimary().getVisualBounds().getMaxY();

        DoubleProperty yWrapper = new SimpleDoubleProperty(stage.getY());
        yWrapper.addListener((obs, oldV, newV) -> stage.setY(newV.doubleValue()));

        Timeline slideOut = new Timeline(
                new KeyFrame(
                        Duration.millis(300),
                        new KeyValue(yWrapper, screenBottom + 50)
                )
        );

        slideOut.setOnFinished(e -> stage.close());
        slideOut.play();
    }
}