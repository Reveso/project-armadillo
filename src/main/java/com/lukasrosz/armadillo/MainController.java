package com.lukasrosz.armadillo;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

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
    public RadioButton PvPRadioButton;
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
    private BorderPane mainPain;

    private int size = 16;
    private int delay = 1;

    public void loadSolo(ActionEvent actionEvent) throws IOException {
        setAllVisible(true);
    }

    public void loadBitwa(ActionEvent actionEvent) throws IOException {
        setAllVisible(false);
        setOnlyFewVisible(true);
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
    public void startClicked(ActionEvent actionEvent) throws IOException {
        GameHandler gameHandler = new GameHandler();

        if (bitwaRadioButton.isSelected()) {
            size = getSize();
            File file = getFile(soloText1);
            gameHandler.startBattleGame(size, file, 100);
        } else if (soloRadioButton.isSelected()) {
            size = getSize();
            File file1 = getFile(soloText1);
            File file2 = getFile(soloText2);
            gameHandler.startSoloGame(file1, file2, size, 1);
        }
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
}
