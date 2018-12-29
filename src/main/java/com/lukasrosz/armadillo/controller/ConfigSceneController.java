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
        File selectedFile = dc.showDialog(getStage());

        if (selectedFile != null) {
            filePathTextField.clear();
            filePathTextField.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void filePickerHandler(ActionEvent actionEvent) {
        FileChooser.ExtensionFilter replayExtensions =
                new FileChooser.ExtensionFilter(
                        "Armadillo replays", "*.rep");

        FileChooser fileChooser = new FileChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        fileChooser.setInitialDirectory(new File(String.valueOf(path)));
        fileChooser.getExtensionFilters().add(replayExtensions);

        File selectedFile = fileChooser.showOpenDialog(getStage());
        if (selectedFile != null) {
            filePathTextField.clear();
            filePathTextField.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    private void startGame(GameGenre gameGenre) throws Exception {
        Stage stage = getStage();

        File chooserFile = new File(filePathTextField.getText());
        if(!chooserFile.canRead()) {
            showAlert("Error", "Could not find the path");
            return;
        }

        if(!verifyBoardSize() && sizeText.isVisible()) {
            showAlert("Error", "Board size must be from range (15; 52).");
            return;
        }

        if (gameGenre.equals(GameGenre.SOLO)) {
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newSoloGame(null, chooserFile, getBoardSize());
            if(gameConfigDto.getGames().size() < 1) {
                showAlert("Error", "Could not find info.txt");
                return;
            }
            showSoloScene(stage, gameConfigDto);
        } else if (gameGenre.equals(GameGenre.BATTLE)) {
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newTournament(chooserFile, getBoardSize());
            if(gameConfigDto.getGames().size() < 1) {
                showAlert("Error", "Could not find any info.txt");
                return;
            }
            showTournamentScene(stage, gameConfigDto);
        } else if (gameGenre.equals(GameGenre.REPLAY)) {
            if(!filePathTextField.getText().endsWith(".rep")) {
                showAlert("Error", "Replay file must have .rep extension.");
                return;
            }

            GameReplay gameReplay;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(GameReplay.class);

                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                System.out.println(chooserFile.getAbsolutePath());
                gameReplay = (GameReplay) jaxbUnmarshaller.unmarshal(chooserFile);
                showReplayScene(stage, gameReplay);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "Corrupted replay file.");
                return;
            }
            showReplayScene(stage, gameReplay);
        }
    }

    private boolean verifyBoardSize() {
        int boardSize;

        try {
            boardSize = getBoardSize();
        } catch (NumberFormatException e) {
            return false;
        }

        return (boardSize > 15 && boardSize < 52);
    }

    private void showSoloScene(Stage stage, GameConfigDto gameConfigDto) throws IOException {
        String previousTitle = stage.getTitle();
        stage.setTitle("Solo");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/solo-game.fxml"));
        Parent stageRoot = fxmlLoader.load();
        SoloGameController soloSceneController = fxmlLoader.getController();
        soloSceneController.setup(gameConfigDto, stage.getScene(), previousTitle);
        stage.setScene(new Scene(stageRoot));
    }

    private void showTournamentScene(Stage stage, GameConfigDto gameConfigDto) throws IOException {
        String previousTitle = stage.getTitle();
        stage.setTitle("Tournament");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-scene.fxml"));
        Parent stageRoot = fxmlLoader.load();
        FightSceneController fightSceneController = fxmlLoader.getController();
        fightSceneController.setup(gameConfigDto, stage.getScene(), previousTitle);
        stage.setScene(new Scene(stageRoot));
    }

    private void showReplayScene(Stage stage, GameReplay gameReplay) throws IOException {
        String previousTitle = stage.getTitle();
        stage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                + gameReplay.getGameResult().getLoser().getAlias());

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-scene.fxml"));
        Parent stageRoot = fxmlLoader.load();
        ReplayController replayController = fxmlLoader.getController();

        replayController.setup(gameReplay, stage.getScene(), previousTitle,
                Screen.getPrimary().getVisualBounds().getHeight());
        stage.setScene(new Scene(stageRoot));
    }

    private ButtonType showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult();
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

}
