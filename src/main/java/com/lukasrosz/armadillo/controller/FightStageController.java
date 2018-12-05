package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;

public class FightStageController {

    @FXML
    private BorderPane borderPane;
    @Getter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
    @Getter @Setter
    private GameConfigDto gameConfigDto;

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        populateScoreboard();
        makeBoard();
        play();
    }

    private void makeBoard() {
        GridPane gridPane = new GridPane();
        for(int i=0; i<gameConfigDto.getBoardSize(); i++) {
            for(int j=0; j<gameConfigDto.getBoardSize(); j++) {
                Button button = new Button("{" + i + ";" + j + "}");
                button.setPrefSize(70, 70);
                gridPane.add(button, i, j);
            }
        }
        borderPane.setCenter(gridPane);
    }

    private void populateScoreboard() {
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

    //TODO this will be on the timeline or sth
    private void play() {
        boolean save = true;
        for(Game game : gameConfigDto.getGames()) {
            while(!game.isEnded()) {
                try {
                    Move move = game.nextMove();
                    System.out.println(move);
                } catch (PlayerInitializationException e) {
                    System.err.println(e);
                    save = false;
                }
            }
            if(save) {
                GameResult gameResult = game.getGameResult();
                scoreboardList.stream().filter(score -> score.getPlayerDetails().equals(gameResult.getWinner()))
                        .forEach(Score::incrementVictories);
                scoreboardList.stream().filter(score -> score.getPlayerDetails().equals(gameResult.getLoser()))
                        .forEach(score -> {
                            score.incrementDefeats();
                            if (gameResult.isDisqualified()) {
                                score.incrementDisqualifications();
                            }
                        });
                Collections.sort(scoreboardList);
            }
        }
    }

}
