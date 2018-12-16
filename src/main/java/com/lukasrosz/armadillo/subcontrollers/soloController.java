package com.lukasrosz.armadillo.subcontrollers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class soloController extends Controller {
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
    @FXML
    public CheckBox checkBox2;

    private File selectedFile;

    public void soloFileChooser1(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
            soloText1.clear();
            soloText1.setText(selectedFile.getAbsolutePath());
        } else {
            System.err.println("wrong selected file");
        }
    }

    public void soloFileChooser2(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        Path path = Paths.get(System.getProperties().getProperty("user.dir"));
        dc.setInitialDirectory(new File(String.valueOf(path)));
        File selectedFile = dc.showDialog(null);

        if (selectedFile != null) {
            this.selectedFile = selectedFile;
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
    
}
