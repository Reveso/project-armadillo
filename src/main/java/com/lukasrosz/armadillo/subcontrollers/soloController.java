package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class soloController {
    @FXML
    public Button soloFileChooser1;
    @FXML
    public Button soloFileChooser2;
    @FXML
    public TextField soloText1;
    @FXML
    public TextField soloText2;
    @FXML
    public TextField sizeText;

    private File selectedFile;

    public void soloFileChooser1(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            soloText1.clear();
            soloText1.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void soloFileChooser2(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            soloText2.clear();
            soloText2.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }
}
