package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.controller.FightSceneController;
import com.lukasrosz.armadillo.controller.ReplayController;
import com.lukasrosz.armadillo.controller.SoloGameController;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import com.lukasrosz.armadillo.replay.GameReplay;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.val;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigSceneController {

    @FXML
    private RadioButton tournamentRadioButton;
    @FXML
    private RadioButton soloRadioButton;
    @FXML
    private RadioButton replayRadioButton;

    @FXML
    private TextField sizeText;
    @FXML
    private TextField filePathTextField;
    @FXML
    private Button fileChooser;
    @FXML
    private Label fileChooserLabel;

    @FXML
    private Label boardSize;

    private enum GameGenre {BATTLE, SOLO, REPLAY}

    public void initialize() {
        setAllVisible(false);
    }

    public void onSoloRadioButton(ActionEvent actionEvent) {
        setAllVisible(true);
        fileChooser.setOnAction(this::folderChooserHandler);
        filePathTextField.setPromptText("Program folder");
        fileChooserLabel.setText("Algorithm program folder");
    }

    public void onTournamentRadioButton(ActionEvent actionEvent) {
        setAllVisible(true);
        fileChooser.setOnAction(this::folderChooserHandler);
        filePathTextField.setPromptText("Tournament root folder");
        fileChooserLabel.setText("Tournament root folder");

    }

    public void onReplayRadioButton(ActionEvent actionEvent) {
        setAllVisible(false);
        setReplayCenterVisible(true);
        fileChooser.setOnAction(this::filePickerHandler);
        filePathTextField.setPromptText("replay.rep");
        fileChooserLabel.setText("Replay file");

    }

    private void setReplayCenterVisible(boolean visible) {
        clearFields();
        fileChooserLabel.setVisible(visible);
        filePathTextField.setVisible(visible);
        fileChooser.setVisible(visible);
    }

    private void setAllVisible(boolean visible) {
        clearFields();
        fileChooserLabel.setVisible(visible);
        fileChooser.setVisible(visible);
        sizeText.setVisible(visible);
        filePathTextField.setVisible(visible);
        boardSize.setVisible(visible);
    }

    @FXML
    public void startClicked(ActionEvent actionEvent) throws Exception {
        if (tournamentRadioButton.isSelected()) {
            startGame(GameGenre.BATTLE);
        } else if (soloRadioButton.isSelected()) {
            startGame(GameGenre.SOLO);
        } else if (replayRadioButton.isSelected()) {
            startGame(GameGenre.REPLAY);
        }
    }

    private Stage getStage() {
        return (Stage) sizeText.getScene().getWindow();
    }

    private int getBoardSize() {
        return Integer.parseInt(sizeText.getText());
    }

    private File getTextFieldFile(TextField textField) {
        return new File(String.valueOf(Paths.get(String.valueOf((textField.getText())))));
    }

    public void clearClicked(ActionEvent actionEvent) {
        clearFields();
    }

    private void clearFields() {
        filePathTextField.clear();
        sizeText.clear();
    }

    public void folderChooserHandler(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            filePathTextField.clear();
            filePathTextField.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void filePickerHandler(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        fileChooser.setInitialDirectory(new File(String.valueOf(path)));

        File selectedFile = fileChooser.showOpenDialog(getStage());

        if (selectedFile != null) {
            filePathTextField.clear();
            filePathTextField.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }

    }

    //TODO data verification (+ proper communiaction/alert if failed):
    //TODO proper boardSize
    //TODO games set > 0
    //TODO .rep file for replay, and if correctly unmarshaled
    private void startGame(GameGenre gameGenre) throws Exception {
        Stage stage = getStage();

        if (gameGenre.equals(GameGenre.SOLO)) {
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newSoloGame(null, getTextFieldFile(filePathTextField), getBoardSize());
            showSoloScene(stage, gameConfigDto);
        } else if (gameGenre.equals(GameGenre.BATTLE)) {
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newTournament(getTextFieldFile(filePathTextField), getBoardSize());
            showTournamentScene(stage, gameConfigDto);
        } else if (gameGenre.equals(GameGenre.REPLAY)) {
            File chooserFile = getTextFieldFile(filePathTextField);

            JAXBContext jaxbContext = JAXBContext.newInstance(GameReplay.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            System.out.println(chooserFile.getAbsolutePath());
            GameReplay gameReplay = (GameReplay) jaxbUnmarshaller.unmarshal(chooserFile);
            showReplayScene(stage, gameReplay);
        }
    }


    private void showSoloScene(Stage stage, GameConfigDto gameConfigDto) throws IOException {
        stage.setTitle("Solo");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/solo-game.fxml"));
        Parent stageRoot = fxmlLoader.load();
        SoloGameController soloSceneController = fxmlLoader.getController();
        soloSceneController.setup(gameConfigDto);
        stage.setScene(new Scene(stageRoot));
    }

    private void showTournamentScene(Stage stage, GameConfigDto gameConfigDto) throws IOException {
        stage.setTitle("Tournament");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-scene.fxml"));
        Parent stageRoot = fxmlLoader.load();
        FightSceneController fightSceneController = fxmlLoader.getController();
        fightSceneController.setup(gameConfigDto);
        stage.setScene(new Scene(stageRoot));
    }

    private void showReplayScene(Stage stage, GameReplay gameReplay) throws IOException {
        stage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                + gameReplay.getGameResult().getLoser().getAlias());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-scene.fxml"));
        Parent stageRoot = fxmlLoader.load();
        ReplayController replayController = fxmlLoader.getController();

        replayController.setup(gameReplay, stage.getScene(), "reee",
                Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(new Scene(stageRoot));
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

}
