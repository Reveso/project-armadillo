package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.subcontrollers.Controller;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

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
    public RadioButton PvPRadioButton;
    @FXML
    private BorderPane mainPain;

    private com.lukasrosz.armadillo.subcontrollers.AIvsPController aIvsPController;
    private com.lukasrosz.armadillo.subcontrollers.bitwaController bitwaController;
    private com.lukasrosz.armadillo.subcontrollers.PvPController pvPController;
    private com.lukasrosz.armadillo.subcontrollers.soloController soloController;
    private FXMLLoader loader;
    private BorderPane subPain;
    private int size = 16;
    private int delay = 1;

    public void loadPvP(ActionEvent actionEvent) throws IOException {
        loader = new FXMLLoader(getClass().getResource("/fxml/gamemodecenter/replay.fxml"));
        subPain = loader.load();
        mainPain.setRight(subPain);
        pvPController = loader.getController();
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
        GameHandler gameHandler = new GameHandler();

        if (bitwaRadioButton.isSelected()) {
            size = getSize(bitwaController);
            File file = getFile(bitwaController.bitwText);
            gameHandler.startBattleGame(size, file, 100);
        } else if (soloRadioButton.isSelected()) {
            size = getSize(soloController);
            File file1 = getFile(soloController.soloText1);
            File file2 = getFile(soloController.soloText2);
            gameHandler.startSoloGame(file1, file2, size, 1);
        } else if (PvPRadioButton.isSelected()) {
            gameHandler.startPreviousGame(); //replay
        }
    }

    private int getSize(Controller controller) {
        int size;
        size = Integer.parseInt(controller.sizeText.getCharacters().toString());
        return size;
    }

    private File getFile(TextField textField) {
        return new File(String.valueOf(Paths.get(String.valueOf((textField.getText())))));
    }

}
