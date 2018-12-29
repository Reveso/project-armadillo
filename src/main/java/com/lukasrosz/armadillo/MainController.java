package com.lukasrosz.armadillo;

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
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import lombok.val;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    @FXML
    public Button startButton;
    @FXML
    public RadioButton bitwaRadioButton;
    @FXML
    public RadioButton soloRadioButton;
    @FXML
    public TextField sizeText;
    @FXML
    public TextField soloText1;
    @FXML
    public Button soloFileChooser1;
    @FXML
    public TextField soloText2;
    @FXML
    public Button soloFileChooser2;
    @FXML
    public CheckBox checkBox2;
    @FXML
    public BorderPane subPain;
    @FXML
    public Label boardsizeLabel;
    @FXML
    public RadioButton replayRadioButton;

    @FXML
    private BorderPane mainPain;

    private int size = 16;
    private int delay = 1;
    private enum GameGenre{ BATTLE, SOLO, REPLAY};
    private  GameGenre previousGame = null;

    public void loadSolo(ActionEvent actionEvent) throws IOException {
        setAllVisible(true);
    }

    private void setOnlyFewVisible(boolean visible) {
        clearFields();
        sizeText.setVisible(visible);
        soloText1.setVisible(visible);
        soloFileChooser1.setVisible(visible);
        boardsizeLabel.setVisible(visible);
    }

    public void onExitClicked(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    public void startClicked(ActionEvent actionEvent) throws Exception {
        if (bitwaRadioButton.isSelected()) {
            startGame(GameGenre.BATTLE);
        } else if (soloRadioButton.isSelected()) {
            startGame(GameGenre.SOLO);
        } else if (replayRadioButton.isSelected()) {
            startGame(GameGenre.REPLAY);
        }
    }

    private Stage getWindow() {
        return (Stage) sizeText.getScene().getWindow();
    }

    private int getSize() {
        return Integer.parseInt(sizeText.getText());
    }

    private File getFile(TextField textField) {
        return new File(String.valueOf(Paths.get(String.valueOf((textField.getText())))));
    }

    public void clearClicked(ActionEvent actionEvent) {
            clearFields();
    }

    private void clearFields() {
        soloText1.clear();
        soloText2.clear();
        sizeText.clear();
    }

    public void soloFilechooser1Handler(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            soloText1.clear();
            soloText1.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void soloFileChooser2Handler(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            soloText2.clear();
            soloText2.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void checkBox2selected(ActionEvent actionEvent) {
        if (checkBox2.isSelected()) {
            soloFileChooser2.setDisable(true);
            soloText2.setDisable(true);
            soloText2.clear();
        } else {
            soloFileChooser2.setDisable(false);
            soloText2.setDisable(false);
        }
    }

    private void setAllVisible(boolean visible) {
        clearFields();
        soloFileChooser1.setVisible(visible);
        soloFileChooser2.setVisible(visible);
        sizeText.setVisible(visible);
        checkBox2.setVisible(visible);
        soloText1.setVisible(visible);
        soloText2.setVisible(visible);
        boardsizeLabel.setVisible(visible);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAllVisible(false);
    }

    public void loadReplay(ActionEvent actionEvent) {
        setAllVisible(false);
        setOnlyFewVisible(true);
    }

    private void startGame(GameGenre gameGenre) throws Exception {

        this.size = size;
        this.delay = delay;
        Stage fightStage = (Stage) getWindow();

        if (gameGenre.equals(GameGenre.SOLO)) {
            previousGame = GameGenre.SOLO;
            fightStage.setTitle("Solo");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/solo-game.fxml"));
            Parent fightStageRoot = fxmlLoader.load();
            SoloGameController fightSceneController = fxmlLoader.getController();
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newSoloGame(getFile(soloText1), getFile(soloText2), getSize());
            fightSceneController.setup(gameConfigDto);
            fightStage.setScene(new Scene(fightStageRoot));
            fightStage.setOnCloseRequest(event -> onExitClicked());
            fightStage.show();
        } else if (gameGenre.equals(GameGenre.BATTLE)) {
            previousGame = GameGenre.BATTLE;
            fightStage.setTitle("Tournament");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/fight-scene.fxml"));
            Parent fightStageRoot = fxmlLoader.load();
            FightSceneController fightSceneController = fxmlLoader.getController();
            val gameMaker = new GameMaker();
            val gameConfigDto = gameMaker.newTournament(getFile(soloText1), getSize());
            fightSceneController.setup(gameConfigDto);
            fightStage.setScene(new Scene(fightStageRoot));
            fightStage.setOnCloseRequest(event -> onExitClicked());
            fightStage.show();
        } else if (gameGenre.equals(GameGenre.REPLAY)) {
            previousGame = GameGenre.REPLAY;
            File file = getFile(soloText1);
            JAXBContext jaxbContext = JAXBContext.newInstance(GameReplay.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            GameReplay gameReplay = (GameReplay) jaxbUnmarshaller.unmarshal(file);

            fightStage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                    + gameReplay.getGameResult().getLoser().getAlias());

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-scene.fxml"));
            Parent fightStageRoot = fxmlLoader.load();
            ReplayController replayController = fxmlLoader.getController();

            replayController.setup(gameReplay, fightStage.getScene(), "reee",
                    Screen.getPrimary().getVisualBounds().getHeight());
            fightStage.setScene(new Scene(fightStageRoot));
            fightStage.setOnCloseRequest(event -> onExitClicked());
            fightStage.show();
        }
    }

    private void onExitClicked() {
        Platform.exit();
        System.exit(0);
    }
}
