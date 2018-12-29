package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightSceneController;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
<<<<<<< HEAD
import javafx.scene.paint.Color;
=======
import javafx.scene.layout.BorderPane;
>>>>>>> parent of 2d17c8b... Data verification in Config Menu, icon, removed deprecated classes, new build
import javafx.stage.Stage;
import lombok.val;

import java.io.File;

public class Main extends Application {
<<<<<<< HEAD
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/MainController/fight-stage.MainController"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        Stage fightStage = new Stage();
        fightStage.setTitle("Tournament");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-scene.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightSceneController fightSceneController = fxmlLoader.getController();

        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newTournament(new File("ai_test_directory"), 20);
//        val gameConfigDto = gameMaker.newHumanVsAIGame(new File("ai_test_directory/283822"), 16);
//        val gameConfigDto = gameMaker.newAiVsAiGame(new File("ai_test_directory/283822"), new File("ai_test_directory/283823"), 16);
//        val gameConfigDto = gameMaker.newSoloGame(null, new File("ai_test_directory/283822"), 16);

        gameConfigDto.getGames().forEach(game -> System.out.println(game.getMovingPlayer().getPlayerDetails().getAlias() + " " + game.getWaitingPlayer().getPlayerDetails().getAlias()));

        fightSceneController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        fightStage.show();
=======

    public static void main(String[] args) {
        launch(args);
    }

    //TODO stage icon
    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/main-global.fxml"));
        Parent stageRoot = fxmlLoader.load();

        primaryStage.setTitle("Armadillo");
        primaryStage.setScene(new Scene(stageRoot));
        primaryStage.setOnCloseRequest(event -> onExitClicked());
        primaryStage.show();
>>>>>>> parent of 2d17c8b... Data verification in Config Menu, icon, removed deprecated classes, new build
    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
