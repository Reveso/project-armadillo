package com.lukasrosz.armadillo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-global.fxml"));
        Parent stageRoot = fxmlLoader.load();

        primaryStage.getIcons().add(new Image("image/icon.png"));
        primaryStage.setTitle("Armadillo");
        primaryStage.setScene(new Scene(stageRoot));
        primaryStage.setOnCloseRequest(event -> onExitClicked());
        primaryStage.show();
    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }

}
