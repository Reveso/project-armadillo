package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.SoloGameController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;

public class SoloExampleMain extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
//        Parent root = FXMLLoader.load(getClass().getResource("/ConfigSceneController/fight-stage.ConfigSceneController"));
//        primaryStage.setTitle("Hello World");
//        primaryStage.setScene(new Scene(root, 300, 275));
//        primaryStage.show();

        Stage fightStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/solo-game.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        SoloGameController controller = fxmlLoader.getController();

        val gameMaker = new GameMaker();
//        val gameConfigDto = gameMaker.newTournament(new File("ai_test_directory"), 20);
//        val gameConfigDto = gameMaker.newHumanVsAIGame(new File("ai_test_directory/283822"), 16);
//        val gameConfigDto = gameMaker.newAiVsAiGame(new File("ai_test_directory/283822"), new File("ai_test_directory/283823"), 16);
        val gameConfigDto = gameMaker.newSoloGame(null, new File("ai_test_directory/444444"), 5);

        gameConfigDto.getGames().forEach(game ->
                System.out.println(
                        game.getMovingPlayer().getPlayerDetails().getAlias()
                                + " "
                                + game.getWaitingPlayer().getPlayerDetails().getAlias()));

        controller.setup(gameConfigDto, fightStage.getScene(), "previousTitle");
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
