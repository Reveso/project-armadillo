package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightStageController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import com.lukasrosz.armadillo.player.AIPlayer;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/fxml/fight-stage.fxml"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        Stage fightStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();

        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newBattleGame(new File("ai_test_directory"), 16);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        fightStage.show();

    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }
    public static void main(String[] args) {
        launch(args);
    }

}
