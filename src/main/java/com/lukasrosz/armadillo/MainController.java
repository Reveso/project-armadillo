package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.subcontrollers.AIvsPController;
import com.lukasrosz.armadillo.subcontrollers.PvPController;
import com.lukasrosz.armadillo.subcontrollers.bitwaController;
import com.lukasrosz.armadillo.subcontrollers.soloController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

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

    public void startClicked(ActionEvent actionEvent) {

        int size;

        if (bitwaRadioButton.isArmed()) {
            size = Integer.parseInt(bitwaController.sizeText.getCharacters().toString());
        } else if (soloRadioButton.isArmed()) {
            size = Integer.parseInt(soloController.sizeText.getCharacters().toString());
        } else if (PvPRadioButton.isArmed()) {
            size = Integer.parseInt(pvPController.sizeText.getCharacters().toString());
        } else if (PvAIRadioButton.isArmed()) {
            size = Integer.parseInt(aIvsPController.sizeText.getCharacters().toString());
        }
    }
}
