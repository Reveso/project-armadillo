package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightStageController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;
import java.io.IOException;


public class GameHandler {

    private enum GameGenre{ BATTLE, SOLO};
    private static GameGenre previousGame = null;

    private static File dir1, dir2;
    private static int size = 0, delay = 0;

    public void startAiVsAiGame(File aiDir1, File aiDir2, int size, int delay) throws IOException {
        Stage fightStage = new Stage();
        this.dir1 = aiDir1;
        this.dir2 = aiDir2;
        this.size = size;
        this.delay = delay;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();

        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newAiVsAiGame(aiDir1, aiDir2, size);
        gameConfigDto.setRefreshDelay(delay);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        fightStage.show();
    }
    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }

    public void startBattleGame(int size, File mainDir, int delay) throws IOException {
        Stage fightStage = new Stage();
        this.dir1 = mainDir;

        this.size = size;
        this.delay = delay;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();


        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newBattleGame(mainDir, size);
        gameConfigDto.setRefreshDelay(delay);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        previousGame = GameGenre.BATTLE;
        fightStage.show();
    }

    public void startSoloGame(File aiDir1, File aiDir2, int size, int delay) throws IOException {
        Stage fightStage = new Stage();

        this.dir1 = aiDir1;
        this.dir2 = aiDir2;
        this.size = size;
        this.delay = delay;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();
        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newSoloGame(aiDir1, aiDir2, size);
        gameConfigDto.setRefreshDelay(delay);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        previousGame = GameGenre.SOLO;
        fightStage.show();
    }

    public void startPreviousGame() throws IOException {
        if (previousGame == null) {
            return;
        } else if (previousGame.equals(GameGenre.SOLO)) {
            startSoloGame(dir1, dir2, size, delay);
        } else if (previousGame.equals(GameGenre.BATTLE)) {
            startBattleGame(size,dir1, delay);
        }
    }
}
