package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.*;
import com.lukasrosz.armadillo.player.HumanFXPlayer;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//rather deprecated module, but still can be use for 1v1 with algorithm
public class SoloGameController {

    @Getter
    @Setter
    private GameConfigDto gameConfigDto;
    @FXML
    public Button backToSettingsButton;
    @FXML
    private GridPane boardGridPane;
    @FXML
    private Button playButton;

    private AnimationTimer animationTimer;
    private Map<Point, Pane> fieldMap = new HashMap<>();
    private Game game;

    //for human moves
    private List<Point> nextHumanMovePoints = new ArrayList<>();
    private Move nextHumanMove;
    private Pane selectedPane;

    public void initialize() {
        boardGridPane.getStylesheets().add("/css/styles.css");
    }

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        game = gameConfigDto.getGames().iterator().next();
        makeBoard();
    }

    private void makeBoard() {
        boardGridPane.getChildren().clear();
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

        pane.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
        pane.getStyleClass().add(paneClass);
        pane.setId("{" + x + ";" + y + "}");
        pane.setOnMouseClicked(this::onPaneClicked);

        return pane;
    }

    private void setFieldOccupied(Point point, String cssClass) {
        fieldMap.get(point).getStyleClass().add(cssClass);
        fieldMap.get(point).setDisable(true);
    }

    private void setupNewGame(Game game) {
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
            animationTimer.stop();
        }
    }

    private void playRound(Game game) throws PlayerInitializationException {
        String cssClass;

        if (game.getMovingPlayer() instanceof HumanFXPlayer) {
            ((HumanFXPlayer) game.getMovingPlayer()).setNextMove(nextHumanMove);
            cssClass = "pane-occupied-by-human-player";
        } else {
            cssClass = "pane-occupied-by-ai-player";
        }

        if(!game.isStarted()) {
            cssClass = "pane-occupied-by-ai-player";
        }

        GameResponse move = game.nextMove();
        System.out.println(move);
        if (move != null) {

            setFieldOccupied(move.getMove().getPoint1(), cssClass);
            setFieldOccupied(move.getMove().getPoint2(), cssClass);
        }

        if (game.getMovingPlayer() instanceof HumanFXPlayer && !game.isEnded()) {
            getNextMoveFromGui();
        }
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
                animationTimer.start();
            } else {
                nextHumanMovePoints.clear();
            }
        }
    }

    private void getNextMoveFromGui() {
        animationTimer.stop();
        nextHumanMovePoints.clear();
    }

    @FXML
    private void onPlayButtonMouseClicked() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                setupTimelineEvent(game);
            }
        };
        makeBoard();
        animationTimer.start();
        playButton.setDisable(true);
    }

    public void onBackButtonMouseClicked() {
        Stage stage = (Stage) backToSettingsButton.getScene().getWindow();
        stage.close();
    }
}
