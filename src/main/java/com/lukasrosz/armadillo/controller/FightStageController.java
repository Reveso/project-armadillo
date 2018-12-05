package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class FightStageController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Score> scoreboardTable;
    @FXML
    private Label fightTitleLabel;
    @FXML
    private GridPane boardGridPane;

    @Getter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
    @Getter
    @Setter
    private GameConfigDto gameConfigDto;
    private Timeline timeline;
    private Iterator<Game> gameIterator;
    private HashMap<Point, Button> buttonMap;


    public void onAction() {
        makeTimeline();
        makeBoard();
        playGame();
    }

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        gameIterator = gameConfigDto.getGames().iterator();
        populateScoreboard();
        makeBoard();
    }

    private void makeBoard() {
        //TODO sth like clear children wouldn't be bad
        boardGridPane = new GridPane();
        boardGridPane.setPadding(new Insets(100));
        buttonMap = new HashMap<>();
        for (int i = 0; i < gameConfigDto.getBoardSize(); i++) {
            for (int j = 0; j < gameConfigDto.getBoardSize(); j++) {
                val region = new Button();
                region.setPrefSize(1000, 1000);
                region.setStyle("-fx-background-color: white; -fx-border-color: lightgray;");
                boardGridPane.add(region, i, j);
                buttonMap.put(new Point(i, j), region);
            }
        }
        borderPane.setCenter(boardGridPane);
    }

    //TODO Toggle ready CSS class would be better
    private void setButtonColorOnPoint(Point point, String color) {
        buttonMap.get(point).setStyle("-fx-background-color: " + color + "; -fx-border-color: lightgray;");
    }

    private void populateScoreboard() {
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

    private void makeTimeline() {
        Game game = gameIterator.next();
        makeTimeline(game);
    }

    private void makeTimeline(Game game) {
        timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(100),
                        event -> setupTimelineEvent(game)
                ));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupTimelineEvent(Game game) {
        if(!game.isStarted()) {
            fightTitleLabel.setText(game.getPlayer1().getPlayerDetails().getAlias() + " vs "
                    + game.getPlayer2().getPlayerDetails().getAlias());
            //TODO fill board with occupied fields
            game.getBoard().getOccupiedCells().forEach(point -> setButtonColorOnPoint(point, "orange"));
        }
        if (!game.isEnded()) {
            try {
                Move move = game.nextMove();
                System.out.println(move);
                if(move != null) {
                    setButtonColorOnPoint(move.getPoint1(), "#7593DA");
                    setButtonColorOnPoint(move.getPoint2(), "#7593DA");
                }
            } catch (PlayerInitializationException e) {
                System.err.println(e);
            }
        } else {
            saveGameResult(game.getGameResult());
            Collections.sort(scoreboardList);
            scoreboardTable.refresh();
            timeline.stop();
            onAction();
        }

    }

    private void playGame() {
        timeline.play();

    }

    private void saveGameResult(GameResult gameResult) {
        scoreboardList.stream().filter(score -> score.getPlayerDetails().equals(gameResult.getWinner()))
                .forEach(Score::incrementVictories);
        scoreboardList.stream().filter(score -> score.getPlayerDetails().equals(gameResult.getLoser()))
                .forEach(score -> {
                    score.incrementDefeats();
                    if (gameResult.isDisqualified()) {
                        score.incrementDisqualifications();
                    }
                });
    }

}
