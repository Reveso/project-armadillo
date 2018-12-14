package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightStageController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;
import java.io.IOException;

public class Fx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        Stage fightStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fxml.fxml"));
        BorderPane fightStageRoot = fxmlLoader.load();

        fightStage.setScene(new Scene(fightStageRoot));

        fightStage.show();
    }
}
