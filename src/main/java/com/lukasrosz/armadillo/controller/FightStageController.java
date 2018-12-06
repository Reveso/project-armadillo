package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import com.lukasrosz.armadillo.player.HumanFXPlayer;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.*;

public class FightStageController {

    @FXML
    private BorderPane borderPane;
    @FXML
    private TableView<Score> scoreboardTable;
    @FXML
    private Label fightTitleLabel;
    @FXML
    private GridPane boardGridPane;
    @FXML
    private Button pauseButton;

    @Getter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
    @Getter
    @Setter
    private GameConfigDto gameConfigDto;
    private Timeline timeline;
    private Iterator<Game> gameIterator;
    private HashMap<Point, Pane> fieldMap;

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        gameIterator = gameConfigDto.getGames().iterator();
        populateScoreboard();
        makeBoard();
    }

    private void makeBoard() {
        boardGridPane.getChildren().clear();
        boardGridPane.setPrefSize(1000, 1000);
        fieldMap = new HashMap<>();

        String color1 = "#995f49";
        String color2 = "#ECCEA6";
        String currentColor = color1;
        for (int i = 0; i < gameConfigDto.getBoardSize(); i++) {
            currentColor = currentColor.equals(color1) ? color2 : color1;

            for (int j = 0; j < gameConfigDto.getBoardSize(); j++) {
                val pane = new Pane();
                pane.setPrefSize(50, 50);
                pane.setStyle("-fx-background-color: " + currentColor + "; ");
                boardGridPane.add(pane, i, j);
                fieldMap.put(new Point(i, j), pane);
                pane.setId("{" + i + ";" + j + "}");
                pane.setOnMouseClicked(this::onPaneClicked);
                currentColor = currentColor.equals(color1) ? color2 : color1;
            }
        }
    }

    //TODO Toggle ready CSS class would be better
    private void setFieldOccupied(Point point, String blockColor, String borderColor) {
        fieldMap.get(point).setStyle("-fx-background-color: " + blockColor + "; -fx-border-color: "+ borderColor + "; -fx-border-width: 2");
        fieldMap.get(point).setDisable(true);
    }

    private void populateScoreboard() {
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

    private boolean makeTimeline() {
        if(gameIterator.hasNext()) {
            makeTimeline(gameIterator.next());
            return true;
        } else {
            pauseButton.setDisable(true);
            return false;
        }
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
            game.getBoard().getOccupiedCells().forEach(point -> setFieldOccupied(point, "#420000", "#3d0101"));
        }
        if (!game.isEnded()) {
            try {
                if(game.getPlayer1() instanceof HumanFXPlayer) {
                    ((HumanFXPlayer) game.getPlayer1()).setNextMove(nextHumanMove);
                }
                Move move = game.nextMove();
                System.out.println(move);
                if(move != null) {
                    setFieldOccupied(move.getPoint1(), "#872443", "#4c1325");
                    setFieldOccupied(move.getPoint2(), "#872443", "#4c1325");
                }
                if(game.getPlayer1() instanceof HumanFXPlayer && !game.isEnded()) {
                    getNextMoveFromGui();
                }
            } catch (PlayerInitializationException e) {
                System.err.println(e);
            }
        } else {
            saveGameResult(game.getGameResult());
            Collections.sort(scoreboardList);
            scoreboardTable.refresh();
            fightTitleLabel.setText("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                    + game.getGameResult().getLoser().getAlias());
            timeline.stop();
            playGame();
        }
    }

    private List<Point> nextHumanMovePoints = new ArrayList<>();
    private Move nextHumanMove;
    private void onPaneClicked(Event event) {
        //TODO the best way would be to evaluate if move is correct here, and send Move object to HumanFXPlayer
        String targetId = ((Pane) event.getTarget()).getId();
        System.out.println(nextHumanMovePoints.size());
        if(nextHumanMovePoints.size() != 1) {
            nextHumanMovePoints.add(Mapper.getStringAsPoint(targetId));
            //TODO get a Pane from Map and toggle "selected" class
        } else {
            nextHumanMovePoints.add(Mapper.getStringAsPoint(targetId));
            //TODO get a Pane from Map and toggle "selected" class
            if(checkIfNeighbours(nextHumanMovePoints.get(0), nextHumanMovePoints.get(1))) {
                nextHumanMove = new Move(nextHumanMovePoints.get(0), nextHumanMovePoints.get(1));
                timeline.play();
            } else {
                //TODO toggle off classes in both Panes
                nextHumanMovePoints.clear();
            }
        }
    }

    private boolean checkIfNeighbours(Point p1, Point p2) {
        int size = gameConfigDto.getBoardSize();
        if(Math.abs(p1.getX() - p2.getX()) == 1 && Math.abs(p1.getY() - p2.getY()) == 0 ||
                Math.abs(p1.getX() - p2.getX()) == 0 && Math.abs(p1.getY() - p2.getY()) == 1) {
            return true;
        } else if((Math.abs(p1.getX() - p2.getX()) == size-1 && Math.abs(p1.getY() - p2.getY()) == 0) ||
                (Math.abs(p1.getX() - p2.getX()) == 0 && Math.abs(p1.getY() - p2.getY()) == size-1)){
            return true;
        } else return false;
    }

    private void getNextMoveFromGui() {
        timeline.pause();
        nextHumanMovePoints.clear();
    }

    private void playGame() {
        if(makeTimeline()) {
            makeBoard();
            timeline.play();
        }
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

    @FXML
    private void onPauseButtonAction() {
        if (pauseButton.getText().toLowerCase().equals("pause")) {
            timeline.pause();
            pauseButton.setText("Play");
        } else {
            if(timeline != null) {
                timeline.play();
            } else {
                playGame();
            }
            pauseButton.setText("Pause");
        }
    }

}
