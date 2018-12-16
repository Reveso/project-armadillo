package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class bitwaController extends Controller {
    @FXML
    public Button bitwaFileChooser;
    @FXML
    public TextField bitwText;
    @FXML
    public TextField sizeText;

    private File selectedFile;

    public void bitwaFileChooser(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            bitwText.clear();
            bitwText.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public static void main(String[] args) {
        System.out.println(System.getProperties().getProperty("user.dir"));
    }
}
