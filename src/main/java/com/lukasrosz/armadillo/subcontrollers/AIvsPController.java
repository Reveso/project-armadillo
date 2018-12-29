package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class AIvsPController extends Controller {
    @FXML
    public Button AIvsPFileChooser;
    @FXML
    public TextField AIvsPText;
    @FXML
    public TextField sizeText;

    private File selectedFile;


    public void AIvsPFileChooser(ActionEvent actionEvent) {
//        FileChooser fc = new FileChooser();
        DirectoryChooser dc = new DirectoryChooser();
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            AIvsPText.clear();
            AIvsPText.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }
}
