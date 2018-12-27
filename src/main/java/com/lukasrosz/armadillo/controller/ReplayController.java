package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.replay.GameReplay;
import com.lukasrosz.armadillo.replay.ReplayMove;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;

public class ReplayController {

    private Scene previousScene;
    private String previousTitle;
    private int fieldSize = 30;
    private final Color BOARD_COLOR = Color.WHITE;
    private final Color WINNER_COLOR = Color.GREEN;
    private final Color LOSER_COLOR = Color.MEDIUMBLUE;

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
    @FXML
    private Label player1AliasLabel;
    @FXML
    private Label player2AliasLabel;
    @FXML
    private Label player1ColorLabel;
    @FXML
    private Label player2ColorLabel;

    private GraphicsContext graphicsContext;
    private GameReplay gameReplay;
    private int moveCounter = 0;
    private int animationDelay = 0;
    private boolean stopAnimation = true;

    public void initialize() {
        Platform.runLater(() -> playButton.requestFocus());
        graphicsContext = animationCanvas.getGraphicsContext2D();

        player1AliasLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        player2AliasLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        player1ColorLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        player2ColorLabel.setFont(Font.font(null, FontWeight.BOLD, 16));

        player1ColorLabel.setTextFill(WINNER_COLOR);
        player2ColorLabel.setTextFill(LOSER_COLOR);

        player1ColorLabel.setText("green");
        player2ColorLabel.setText("blue");
    }

    public void setup(GameReplay gameReplay, Scene previousScene, String previousTitle, double screenHeight) {
        this.gameReplay = gameReplay;
        this.previousScene = previousScene;
        this.previousTitle = previousTitle;
        player1AliasLabel.setText(gameReplay.getWinner() + ": ");
        player2AliasLabel.setText(gameReplay.getLoser() + ": ");

        int boardSize = fieldSize * gameReplay.getBoardSize();
        double maxWindowHeight = 0.8 * screenHeight;

        while(boardSize > maxWindowHeight) {
            boardSize = --fieldSize * gameReplay.getBoardSize();
            System.out.println(fieldSize);
        }

        initializeCanvas(gameReplay.getBoardSize());
    }

    private void initializeCanvas(int boardSize) {
        int canvasSize = boardSize* fieldSize + 2;
        animationCanvas.setHeight(canvasSize);
        animationCanvas.setWidth(canvasSize);

        graphicsContext.setFill(BOARD_COLOR);
        graphicsContext.fillRect(0, 0, canvasSize, canvasSize);
        for(int i=1; i <= canvasSize; i+= fieldSize) {
            graphicsContext.strokeLine(i, 0, i, canvasSize);
            graphicsContext.strokeLine(0, i, canvasSize, i);
        }

        gameReplay.getInitiallyOccupiedFields().forEach(point ->
                fillFieldOnPos(point.getX(), point.getY(), Color.DARKRED));
    }

    private void fillFieldOnPos(int x, int y, Color color) {
        graphicsContext.setFill(color);
        graphicsContext.fillRect(x* fieldSize +2, y* fieldSize +2, fieldSize - 2, fieldSize - 2);
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
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            color = WINNER_COLOR;
        } else {
            color = LOSER_COLOR;
        }

        int x1 = move.getMove().getPoint1().getX();
        int y1 = move.getMove().getPoint1().getY();
        int x2 = move.getMove().getPoint2().getX();
        int y2 = move.getMove().getPoint2().getY();

        fillFieldOnPos(x1, y1, color);
        fillFieldOnPos(x2, y2, color);

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

        Color color = BOARD_COLOR;
        fillFieldOnPos(x1, y1, color);
        fillFieldOnPos(x2, y2, color);

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
            Stage stage = (Stage) playButton.getScene().getWindow();
            stage.setScene(previousScene);
            stage.setTitle(previousTitle);
        }
    }

    public void onInfoButtonAction() throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/replay-info.fxml"));
        Parent stageRoot = fxmlLoader.load();
        ReplayInfoController replayInfoController = fxmlLoader.getController();

        stage.setTitle(gameReplay.getGameResult().getWinner().getAlias() + " vs "
                + gameReplay.getGameResult().getLoser().getAlias());

        replayInfoController.setup(gameReplay);
        stage.setScene(new Scene(stageRoot));
        stage.show();

    }

    private ButtonType showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult();
    }
}
