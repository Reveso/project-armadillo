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
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
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
    //    private Timeline gameAnimationTimeline;
    private AnimationTimer animationTimer;
    private Iterator<Game> gameIterator;
    private Map<Point, Pane> fieldMap = new HashMap<>();

    //for human moves
    private List<Point> nextHumanMovePoints = new ArrayList<>();
    private Move nextHumanMove;
    private Pane selectedPane;

    public void initialize() {
        boardGridPane.getStylesheets().add("/css/styles.css");
        fightTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 32));
    }

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        gameIterator = gameConfigDto.getGames().iterator();
        populateScoreboard();
        makeBoard();
    }

    private void makeBoard() {
        boardGridPane.getChildren().clear();
        boardGridPane.setPrefSize(1000, 1000);
        fieldMap.clear();

        String paneClass1 = "pane1";
        String paneClass2 = "pane2";
        String currentPaneClass = paneClass1;
        for (int i = 0; i < gameConfigDto.getBoardSize(); i++) {
            if (gameConfigDto.getBoardSize() % 2 == 0) {
                currentPaneClass = currentPaneClass.equals(paneClass1) ?
                        paneClass2 : paneClass1;
            }

            for (int j = 0; j < gameConfigDto.getBoardSize(); j++) {
                val pane = newBoardPane(currentPaneClass, i, j);
                fieldMap.put(new Point(i, j), pane);
                boardGridPane.add(pane, i, j);

                currentPaneClass = currentPaneClass.equals(paneClass1) ?
                        paneClass2 : paneClass1;
            }
        }
    }

    private Pane newBoardPane(String paneClass, int x, int y) {
        val pane = new Pane();

        pane.setPrefSize(50, 50);
        pane.getStyleClass().add(paneClass);
        pane.setId("{" + x + ";" + y + "}");
        pane.setOnMouseClicked(this::onPaneClicked);

        return pane;
    }

    private void setFieldOccupied(Point point, String cssClass) {
        fieldMap.get(point).getStyleClass().add(cssClass);
        fieldMap.get(point).setDisable(true);
    }

    private void populateScoreboard() {
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

    private boolean makeTimeline() {
        if (gameIterator.hasNext()) {
            makeTimeline(gameIterator.next());
            return true;
        } else {
            pauseButton.setDisable(true);
            return false;
        }
    }

    private void makeTimeline(Game game) {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                setupTimelineEvent(game);
            }
        };
//
//        gameAnimationTimeline = new Timeline(
//                new KeyFrame(
//                        Duration.millis(gameConfigDto.getRefreshDelay()),
//                        event -> setupTimelineEvent(game)
//                ));
//        gameAnimationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupNewGame(Game game) {
        fightTitleLabel.setText(game.getMovingPlayer().getPlayerDetails().getAlias() + " vs "
                + game.getWaitingPlayer().getPlayerDetails().getAlias());
        game.getBoard().getOccupiedFields()
                .forEach(point -> setFieldOccupied(point, "pane-occupied-from-start"));
    }

    private void setupTimelineEvent(Game game) {
        if (!game.isStarted()) {
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
        if (game.getMovingPlayer() instanceof HumanFXPlayer) {
            ((HumanFXPlayer) game.getMovingPlayer()).setNextMove(nextHumanMove);
        }

        Move move = game.nextMove();
        System.out.println(move);
        if (move != null) {
            setFieldOccupied(move.getPoint1(), "pane-occupied-by-player");
            setFieldOccupied(move.getPoint2(), "pane-occupied-by-player");
        }
        if (game.getMovingPlayer() instanceof HumanFXPlayer && !game.isEnded()) {
            getNextMoveFromGui();
        }
    }

    private void sumUpGame(Game game) {
        saveGameResult(game.getGameResult());
        Collections.sort(scoreboardList);
        scoreboardTable.refresh();
        fightTitleLabel.setText("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                + game.getGameResult().getLoser().getAlias());
//        gameAnimationTimeline.stop();
        animationTimer.stop();
        playGame();
    }

    private void onPaneClicked(Event event) {
        String targetId = ((Pane) event.getTarget()).getId();
        System.out.println(nextHumanMovePoints.size());
        Point point = PointsMapper.getStringAsPoint(targetId);

        if (nextHumanMovePoints.size() != 1) {
            nextHumanMovePoints.add(point);
            selectedPane = (Pane) event.getTarget();
            selectedPane.getStyleClass().add("selected-pane");
        } else {
            nextHumanMovePoints.add(point);
            selectedPane.getStyleClass().remove("selected-pane");

            if (Board.checkIfNeighbours(gameConfigDto.getBoardSize(),
                    nextHumanMovePoints.get(0), nextHumanMovePoints.get(1))) {
                nextHumanMove = new Move(nextHumanMovePoints.get(0), nextHumanMovePoints.get(1));
//                gameAnimationTimeline.play();
                animationTimer.start();
            } else {
                nextHumanMovePoints.clear();
            }
        }
    }

    private void getNextMoveFromGui() {
//        gameAnimationTimeline.pause();
        animationTimer.stop();
        nextHumanMovePoints.clear();
    }

    private void playGame() {
        if (makeTimeline()) {
            makeBoard();
            animationTimer.start();
//            gameAnimationTimeline.play();
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
//            gameAnimationTimeline.pause();
            animationTimer.stop();
            pauseButton.setText("Play");

        } else {
//            if(gameAnimationTimeline != null) {
//                gameAnimationTimeline.play();
            if (animationTimer != null) {
                animationTimer.start();
            } else {

                playGame();
            }
            pauseButton.setText("Pause");
        }
    }

}
