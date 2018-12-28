package com.lukasrosz.armadillo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWithGlobal extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-global.fxml"));
        BorderPane fightStageRoot = fxmlLoader.load();
        MainController controller = (MainController)  fxmlLoader.getController();
        controller.setStage(primaryStage);
        stage.setScene(new Scene(fightStageRoot));
        stage.setOnCloseRequest(event -> onExitClicked());

        stage.show();
    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }
}
