package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import lombok.val;

import java.util.Collections;

public class FightStageController {

    @FXML
    private BorderPane borderPane;
    private TableView<Score> table = new TableView<Score>();

    private final ObservableList<Score> scores = FXCollections.observableArrayList(
            new Score(new PlayerDetails("s", "a", "f", "w"))
    );

    public void initialize() throws Exception {

        val aliasColumn = new TableColumn("Alias");
        aliasColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("alias"));

        val surnameColumn = new TableColumn("Surname");
        surnameColumn.setCellValueFactory(new PropertyValueFactory<Score, String>("surname"));

        val victoriesColumn = new TableColumn("Victories");
        victoriesColumn.setCellValueFactory(new PropertyValueFactory<Score, Integer>("victories"));

        val defeatsColumn = new TableColumn("Defeats");
        defeatsColumn.setCellValueFactory(new PropertyValueFactory<Score, Integer>("defeats"));

        val disqualificationsColumn = new TableColumn("Disqualifications");
        disqualificationsColumn.setCellValueFactory(new PropertyValueFactory<Score, Integer>("disqualifications"));

        table.setItems(scores);
        table.getColumns().addAll(aliasColumn, surnameColumn, victoriesColumn, defeatsColumn, disqualificationsColumn);

        borderPane.setRight(table);

        PlayerDetails playerDetails = new PlayerDetails("reeeeeeeee", "w", "e", "w");
        Score score = new Score(playerDetails);
        scores.add(score);
        score.incrementDefeats();
        score.incrementVictories();
        score.incrementVictories();

        score = new Score(new PlayerDetails("e", "f", "g", "h"));
        scores.add(score);
        score.incrementVictories();
        score.incrementVictories();

        score = new Score(new PlayerDetails("e", "f", "g", "h"));
        scores.add(score);
        score.incrementDefeats();
        score.incrementDisqualifications();
        score.incrementVictories();
        score.incrementVictories();

        Collections.sort(scores);
        playerDetails.setAlias("kke");
    }

}
