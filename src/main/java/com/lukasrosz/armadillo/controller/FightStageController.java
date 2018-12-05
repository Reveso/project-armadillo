package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import lombok.var;

import java.util.Collections;

public class FightStageController {

    @FXML
    private BorderPane borderPane;
    @Getter @Setter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
    @Getter @Setter
    private GameConfigDto gameConfigDto;

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        populateScoreboard();
    }

    public void initialize()  {
    }

    private void populateScoreboard(){
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

}
