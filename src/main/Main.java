package main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        Button btn = new Button("Hello JavaFX");
        stage.setScene(new Scene(btn, 300, 200));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}