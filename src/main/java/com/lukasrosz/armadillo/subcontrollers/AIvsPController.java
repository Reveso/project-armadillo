package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class AIvsPController {
    @FXML
    public Button AIvsPFileChooser;
    @FXML
    public TextField AIvsPText;
    public TextField sizeText;

    private File selectedFile;


    public void AIvsPFileChooser(ActionEvent actionEvent) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            AIvsPText.clear();
            AIvsPText.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }
}
