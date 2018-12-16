package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.FightStageController;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class MainController {
    @FXML
    public Button startButton;
    @FXML
    public RadioButton bitwaRadioButton;
    @FXML
    public RadioButton soloRadioButton;
    @FXML
    public RadioButton PvAIRadioButton;
    @FXML
    public RadioButton PvPRadioButton;
    //    @FXML
//    private RadioButton solo;
    @FXML
    private BorderPane mainPain;
    /*@FXML
    private BorderPane soloPane;
    @FXML
    private BorderPane PvPPane;
    @FXML
    private BorderPane bitwaPane;
    @FXML
    private BorderPane AIvsPPane;*/
    private com.lukasrosz.armadillo.subcontrollers.AIvsPController aIvsPController;
    private com.lukasrosz.armadillo.subcontrollers.bitwaController bitwaController;
    private com.lukasrosz.armadillo.subcontrollers.PvPController pvPController;
    private com.lukasrosz.armadillo.subcontrollers.soloController soloController;



    private FXMLLoader loader;
    private BorderPane subPain;

    public void loadPvP(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/gamemodecenter/PvP.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
        pvPController = loader.getController();
    }

    public void loadAIvP(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/gamemodecenter/AIvsP.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
        aIvsPController = loader.getController();
    }

    public void loadSolo(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/gamemodecenter/solo.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
        soloController = loader.getController();
    }

    public void loadBitwa(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/gamemodecenter/bitwa.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
        bitwaController = loader.getController();
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void startClicked(ActionEvent actionEvent) throws IOException {


        int size;
        if (bitwaRadioButton.isSelected()) {
            size = Integer.parseInt(bitwaController.sizeText.getCharacters().toString());
            File file = new File(String.valueOf(Paths.get(String.valueOf((bitwaController.bitwText.getText())))));
            startBattleGame(size, file, 1);
        } else if (soloRadioButton.isSelected()) {
            size = Integer.parseInt(soloController.sizeText.getCharacters().toString());
            File file1 = new File(String.valueOf(Paths.get(String.valueOf((soloController.soloText1.getText())))));
            File file2 = new File(String.valueOf(Paths.get(String.valueOf((soloController.soloText2.getText())))));
            startAiVsAiGame(file1, file2, size, 1);
        } else if (PvPRadioButton.isSelected()) {
            size = Integer.parseInt(pvPController.sizeText.getCharacters().toString());
            startSingleGame();
        } else if (PvAIRadioButton.isSelected()) {
            size = Integer.parseInt(aIvsPController.sizeText.getCharacters().toString());
            File file = new File(String.valueOf(Paths.get(String.valueOf((aIvsPController.AIvsPText.getText())))));
            startHumanVsAIGame(size, file, 1);
        }
    }

    private void startSingleGame() {
        /*Stage fightStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();


        val gameMaker = new GameMaker();
//        val gameConfigDto = gameMaker.;     -------------------
        gameConfigDto.setRefreshDelay(delay);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        fightStage.show();*/
    }

    private void startHumanVsAIGame(int size, File mainDir, int delay) throws IOException {
        Stage fightStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();


        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newHumanVsAIGame(mainDir, size);
        gameConfigDto.setRefreshDelay(delay);
        fightStageController.setup(gameConfigDto);
        fightStage.setScene(new Scene(fightStageRoot));
        fightStage.setOnCloseRequest(event -> onExitClicked());
        fightStage.show();
    }

    private void startAiVsAiGame(File aiDir1, File aiDir2, int size, int delay) throws IOException {
        Stage fightStage = new Stage();

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

    private void startBattleGame(int size, File mainDir, int delay) throws IOException {
        Stage fightStage = new Stage();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-stage.fxml"));
        Parent fightStageRoot = fxmlLoader.load();
        FightStageController fightStageController = fxmlLoader.getController();


        val gameMaker = new GameMaker();
        val gameConfigDto = gameMaker.newBattleGame(mainDir, size);
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
}
