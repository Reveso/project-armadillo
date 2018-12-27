package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.replay.GameReplay;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.IOException;
import java.util.List;

public class MatchHistoryController {


    @FXML
    private TableView<GameReplay> gameReplaysTable;

    @Getter
    private final ObservableList<GameReplay> gameReplaysList = FXCollections.observableArrayList();

    public void initialize() {
        gameReplaysTable.setTooltip(new Tooltip("Double click for replay"));
        gameReplaysTable.setRowFactory(this::gameReplaysTableRowFactory);
    }

    public void setup(List<GameReplay> gameReplays) {
        gameReplaysList.addAll(gameReplays);
    }

    private TableRow<GameReplay> gameReplaysTableRowFactory(TableView tableView) {
        TableRow<GameReplay> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (! row.isEmpty())) {
                GameReplay gameReplay = row.getItem();
                setReplayScene(gameReplay);
            }
        });
        return row;
    }

    private void setReplayScene(GameReplay gameReplay) {
        Stage stage = (Stage) gameReplaysTable.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-scene.fxml"));

        try {
            Parent stageRoot = fxmlLoader.load();
            ReplayController controller = fxmlLoader.getController();
            controller.setup(gameReplay, stage.getScene(), stage.getTitle(), stage.getMaxHeight());

            stage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                    + gameReplay.getGameResult().getLoser().getAlias());

            stage.setScene(new Scene(stageRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onBackButtonMouseClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) gameReplaysTable.getScene().getWindow();
        stage.close();
    }
}
