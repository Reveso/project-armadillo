package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightSceneController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/MainController/fight-stage.MainController"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        Stage fightStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-scene.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightSceneController fightSceneController = fxmlLoader.getController();

        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newTournament(new File("ai_test_directory"), 45);
//        val gameConfigDto = gameMaker.newHumanVsAIGame(new File("ai_test_directory/283822"), 16);
//        val gameConfigDto = gameMaker.newAiVsAiGame(new File("ai_test_directory/283822"), new File("ai_test_directory/283823"), 16);
//        val gameConfigDto = gameMaker.newSoloGame(null, new File("ai_test_directory/283822"), 16);
        gameConfigDto.setRefreshDelay(1);

        fightSceneController.setup(gameConfigDto);
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
