package notification.info;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

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
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setAlwaysOnTop(true);

            Label label = new Label(message);
            label.setStyle(
                    "-fx-background-color: #333333; -fx-text-fill: white; -fx-padding: 10px; -fx-background-radius: 5px;");

            StackPane root = new StackPane(label);
            root.setStyle("-fx-background-color: transparent;");
            root.setPadding(new Insets(10));
            root.setAlignment(Pos.BOTTOM_RIGHT);

            Scene scene = new Scene(root);
            scene.setFill(null);
            stage.setScene(scene);

            // Position it at the bottom right of the screen
            stage.setX(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxX() - 300);
            stage.setY(javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY());

            stage.show();

            // Slide up animation — use a writable property wrapper so KeyValue can update
            // it
            javafx.beans.property.DoubleProperty yWrapper = new javafx.beans.property.SimpleDoubleProperty(
                    stage.getY());
            yWrapper.addListener((obs, oldV, newV) -> stage.setY(newV.doubleValue()));

            Timeline slideIn = new Timeline();
            KeyValue kv = new KeyValue(yWrapper, javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY() - 100);
            KeyFrame kf = new KeyFrame(Duration.millis(300), kv);
            slideIn.getKeyFrames().add(kf);

            // Slide out and close
            Timeline slideOut = new Timeline();
            KeyValue kvOut = new KeyValue(yWrapper, javafx.stage.Screen.getPrimary().getVisualBounds().getMaxY() + 50);
            KeyFrame kfOut = new KeyFrame(Duration.millis(300), kvOut);
            slideOut.getKeyFrames().add(kfOut);
            slideOut.setOnFinished(e -> stage.close());

            slideIn.setOnFinished(e -> {
                new Thread(() -> {
                    try {
                        Thread.sleep(2000); // Display for 2 seconds
                        Platform.runLater(slideOut::play);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }).start();
            });

            slideIn.play();
        });
    }
}
