package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.replay.GameReplay;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class ReplayInfoController {

    @FXML
    private Label winnerLabel;
    @FXML
    private Label loserLabel;
    @FXML
    private Label disqualifiedLabel;
    @FXML
    private Label finishTypeLabel;
    @FXML
    private Label winnerAliasLabel;
    @FXML
    private Label loserAliasLabel;
    @FXML
    private Label disqualifiedValueLabel;
    @FXML
    private Label finishTypeValueLabel;

    public void initialize() {
        FontWeight fontWeight = FontWeight.NORMAL;
        int fontSize = 16;

        winnerLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));
        loserLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));
        disqualifiedLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));
        finishTypeLabel.setFont(Font.font(null, FontWeight.BOLD, fontSize));

        winnerAliasLabel.setFont(Font.font(null, fontWeight, fontSize));
        loserAliasLabel.setFont(Font.font(null, fontWeight, fontSize));
        disqualifiedValueLabel.setFont(Font.font(null, fontWeight, fontSize));
        finishTypeValueLabel.setFont(Font.font(null, fontWeight, fontSize));
    }

    public void setup(GameReplay gameReplay) {
        winnerAliasLabel.setText(gameReplay.getWinner());
        loserAliasLabel.setText(gameReplay.getLoser());
        disqualifiedValueLabel.setText(String.valueOf(gameReplay.getDisqualification()));
        finishTypeValueLabel.setText(gameReplay.getFinishType());
    }

    @FXML
    public void onCloseButtonMouseClicked(){
        Stage stage = (Stage)winnerLabel.getScene().getWindow();
        stage.close();
    }
}
