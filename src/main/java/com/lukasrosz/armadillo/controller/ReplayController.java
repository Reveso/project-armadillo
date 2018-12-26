package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.replay.GameReplay;
import com.lukasrosz.armadillo.replay.ReplayMove;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ReplayController {

    private Scene previousScene;
    private final int FIELD_SIZE = 30;

    @FXML
    private TextField refreshDelayTextField;
    @FXML
    private Button playButton;
    @FXML
    private Button nextMoveButton;
    @FXML
    private Button previousMoveButton;
    @FXML
    private Canvas animationCanvas;

    private GraphicsContext graphicsContext;
    private GameReplay gameReplay;
    private int moveCounter = 0;
    private int animationDelay = 0;
    private boolean stopAnimation = true;

    public void initialize() {
        Platform.runLater(() -> playButton.requestFocus());
        graphicsContext = animationCanvas.getGraphicsContext2D();
    }

    public void setup(GameReplay gameReplay, Scene previousScene) {
        this.gameReplay = gameReplay;
        this.previousScene = previousScene;

        initializeCanvas(gameReplay.getBoardSize());
    }

    private void initializeCanvas(int boardSize) {
        int canvasSize = boardSize*FIELD_SIZE + 2;
        animationCanvas.setHeight(canvasSize);
        animationCanvas.setWidth(canvasSize);

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.fillRect(0, 0, canvasSize, canvasSize);
        for(int i=1; i <= canvasSize; i+= FIELD_SIZE) {
            graphicsContext.strokeLine(i, 0, i, canvasSize);
            graphicsContext.strokeLine(0, i, canvasSize, i);
        }

        gameReplay.getInitiallyOccupiedFields().forEach(point ->
                fillRectOnPos(point.getX(), point.getY(), Color.DARKRED));
    }

    private void fillRectOnPos(int x, int y, Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x*FIELD_SIZE+2, y*FIELD_SIZE+2, FIELD_SIZE - 2, FIELD_SIZE - 2);
    }

    public void onPlayButtonAction() {
        if(playButton.getText().toLowerCase().equals("play")) {
            try {
                animationDelay = Integer.parseInt(refreshDelayTextField.getText());
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
            playButton.setText("Stop");
            setupAnimationThread();
        } else {
            playButton.setText("Play");
            stopAnimation();
        }
    }

    private void setupAnimationThread() {
        startAnimation();

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while(onNextMoveButtonAction()) {
                    Thread.sleep(animationDelay);
                    if(stopAnimation) {
                        return null;
                    }
                }
                return null;
            }
        };

        new Thread(task).start();
    }

    private void startAnimation() {
        stopAnimation = false;
        nextMoveButton.setDisable(true);
        previousMoveButton.setDisable(true);
    }

    private void stopAnimation() {
        stopAnimation = true;
        if(animationDelay <= 1000) {
            try {
                Thread.sleep(animationDelay + 100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nextMoveButton.setDisable(false);
        previousMoveButton.setDisable(false);
        animationDelay = 0;
    }

    private void reset() {
        stopAnimation();
        moveCounter = 0;
        initializeCanvas(gameReplay.getBoardSize());
    }

    public boolean onNextMoveButtonAction() {
        if(!gameReplay.getMoveMap().containsKey(moveCounter)) {
            return false;
        }
        ReplayMove move = gameReplay.getMoveMap().get(moveCounter++);

        Color color;
        if(move.getMovingPlayer().equals(gameReplay.getGameResult().getWinner())) {
            color = Color.YELLOWGREEN;
        } else {
            color = Color.DARKTURQUOISE;
        }

        int x1 = move.getMove().getPoint1().getX();
        int y1 = move.getMove().getPoint1().getY();
        int x2 = move.getMove().getPoint2().getX();
        int y2 = move.getMove().getPoint2().getY();

        fillRectOnPos(x1, y1, color);
        fillRectOnPos(x2, y2, color);

        return true;
    }

    public void onPreviousMoveButtonAction() {
        if(moveCounter <= 0) {
            return;
        }

        ReplayMove move = gameReplay.getMoveMap().get(--moveCounter);

        int x1 = move.getMove().getPoint1().getX();
        int y1 = move.getMove().getPoint1().getY();
        int x2 = move.getMove().getPoint2().getX();
        int y2 = move.getMove().getPoint2().getY();

        Color color = Color.WHITE;
        fillRectOnPos(x1, y1, color);
        fillRectOnPos(x2, y2, color);

    }

    public void onResetButtonAction() {
        String message = "Reset replay?";
        ButtonType result = showAlert("Reset", message);

        if(result.equals(ButtonType.OK)) {
            reset();
        }
    }

    public void onBackButtonMouseClicked() {
        String message = "Do you want to close this replay?";
        ButtonType result = showAlert("Go back", message);

        if(result.equals(ButtonType.OK)) {
            ((Stage) playButton.getScene().getWindow()).setScene(previousScene);
        }
    }

    private ButtonType showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult();
    }
}
