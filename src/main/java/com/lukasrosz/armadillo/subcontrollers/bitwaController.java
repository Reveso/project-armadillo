package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

public class bitwaController {
    @FXML
    public Button bitwaFileChooser;
    @FXML
    public TextField bitwText;
    @FXML
    public TextField sizeText;

    private File selectedFile;

    public void bitwaFileChooser(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File("./"));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            bitwText.clear();
            bitwText.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }
}