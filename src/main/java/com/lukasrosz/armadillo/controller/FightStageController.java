package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Board;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.*;

public class FightStageController {

    @Getter
    @Setter
    private GameConfigDto gameConfigDto;

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
    private Timeline gameAnimationTimeline;
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
        gameAnimationTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(gameConfigDto.getRefreshDelay()),
                        event -> setupTimelineEvent(game)
                ));
        gameAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupNewGame(Game game) {
        fightTitleLabel.setText(game.getMovingPlayer().getPlayerDetails().getAlias() + " vs "
                + game.getWaitingPlayer().getPlayerDetails().getAlias());
        game.getBoard().getOccupiedFields().forEach(point -> setFieldOccupied(point, "#420000", "#3d0101"));
    }

    private void setupTimelineEvent(Game game) {
        if(!game.isStarted()) {
            setupNewGame(game);
        }
        
        if (!game.isEnded()) {
            try {
                playRound(game);
            } catch (PlayerInitializationException e) {
                System.err.println(e);
            }
        } else {
            sumUpGame(game);
        }
    }
    
    private void playRound(Game game) throws PlayerInitializationException {
        if(game.getMovingPlayer() instanceof HumanFXPlayer) {
            ((HumanFXPlayer) game.getMovingPlayer()).setNextMove(nextHumanMove);
        }

        Move move = game.nextMove();
        System.out.println(move);
        if(move != null) {
            setFieldOccupied(move.getPoint1(), "#872443", "#4c1325");
            setFieldOccupied(move.getPoint2(), "#872443", "#4c1325");
        }
        if(game.getMovingPlayer() instanceof HumanFXPlayer && !game.isEnded()) {
            getNextMoveFromGui();
        }
    }
    
    private void sumUpGame(Game game) {
        saveGameResult(game.getGameResult());
        Collections.sort(scoreboardList);
        scoreboardTable.refresh();
        fightTitleLabel.setText("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                + game.getGameResult().getLoser().getAlias());
        gameAnimationTimeline.stop();
        playGame();
    }

    private List<Point> nextHumanMovePoints = new ArrayList<>();
    private Move nextHumanMove;
    private void onPaneClicked(Event event) {
        //TODO the best way would be to evaluate if move is correct here, and send Move object to HumanFXPlayer
        String targetId = ((Pane) event.getTarget()).getId();
        System.out.println(nextHumanMovePoints.size());
        if(nextHumanMovePoints.size() != 1) {
            nextHumanMovePoints.add(PointsMapper.getStringAsPoint(targetId));
            //TODO get a Pane from Map and toggle "selected" class
        } else {
            nextHumanMovePoints.add(PointsMapper.getStringAsPoint(targetId));
            //TODO get a Pane from Map and toggle "selected" class
            if(Board.checkIfNeighbours(gameConfigDto.getBoardSize(),
                    nextHumanMovePoints.get(0), nextHumanMovePoints.get(1))) {
                nextHumanMove = new Move(nextHumanMovePoints.get(0), nextHumanMovePoints.get(1));
                gameAnimationTimeline.play();
            } else {
                //TODO toggle off classes in both Panes
                nextHumanMovePoints.clear();
            }
        }
    }

    private void getNextMoveFromGui() {
        gameAnimationTimeline.pause();
        nextHumanMovePoints.clear();
    }

    private void playGame() {
        if(makeTimeline()) {
            makeBoard();
            gameAnimationTimeline.play();
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
            gameAnimationTimeline.pause();
            pauseButton.setText("Play");
        } else {
            if(gameAnimationTimeline != null) {
                gameAnimationTimeline.play();
            } else {
                playGame();
            }
            pauseButton.setText("Pause");
        }
    }

}
