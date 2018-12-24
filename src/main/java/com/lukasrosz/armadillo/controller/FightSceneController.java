package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.GameResponse;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class FightSceneController {

    public static Logger logger;
    private FileHandler fileHandler;

    @Getter
    @Setter
    private GameConfigDto gameConfigDto;
    @FXML
    public Button backToSettingsButton;
    @FXML
    private TableView<Score> scoreboardTable;
    @FXML
    private Label fightTitleLabel;
    @FXML
    private Button playButton;
    @FXML
    private ProgressIndicator gameProgressBar;
    @FXML
    private ProgressIndicator roundProgressBar;

    @Getter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
    private boolean stopGame = false;

    public void initialize() {
        fightTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        new File("referee_files/scoreboard").mkdirs();
        new File("referee_files/logs").mkdirs();

    }

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        populateScoreboard();
    }

    private void populateScoreboard() {
        scoreboardList.addAll(gameConfigDto.getScores());
        Collections.sort(scoreboardList);
    }

    private String getShortDate() {
        DateFormat df = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }

    private void saveScoreboardToFile() {
        String shortDate = getShortDate();
        String filename = shortDate + "_scoreboard.txt";

        try (PrintStream bf = new PrintStream("referee_files/scoreboard/" + filename)) {
            bf.println("Alias; Name; Surname; Victories; Defeats; Disqualifications");
            for (Score score : scoreboardList) {
                bf.println(
                        score.getAlias()
                                + "; " + score.getName()
                                + "; " + score.getSurname()
                                + "; " + score.getVictories()
                                + "; " + score.getDefeats()
                                + "; " + score.getDisqualifications());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playGame() {
        Task<Double> task = new Task<Double>() {
            @Override
            protected Double call() {
                int progress = 0;
                updateProgress(progress, gameConfigDto.getGames().size());
                for (Game game : gameConfigDto.getGames()) {

                    updateTitle(game.getMovingPlayer().getPlayerDetails().getAlias() + " vs "
                            + game.getWaitingPlayer().getPlayerDetails().getAlias());

                    while (!game.isEnded()) {
                        if(stopGame) {
                            game.finishGame();
                            updateProgress(0.0, 1.0);
                            updateValue(0.0);
                            return null;
                        }

                        try {
                            GameResponse gameResponse = game.nextMove();
                            //TODO null check might be safer
                            updateValue(gameResponse.getOccupiedFields());

                        } catch (PlayerInitializationException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("============================================================================");
                    System.out.println(game.isEnded());
                    updateValue(1.0);

                    saveGameResult(game.getGameResult());
                    Collections.sort(scoreboardList);
                    scoreboardTable.refresh();
                    updateTitle("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                            + game.getGameResult().getLoser().getAlias());

                    updateProgress(++progress, gameConfigDto.getGames().size());
                }
                saveScoreboardToFile();
                return null;
            }
        };
        roundProgressBar.progressProperty().bind(task.valueProperty());
        gameProgressBar.progressProperty().bind(task.progressProperty());
        fightTitleLabel.textProperty().bind(task.titleProperty());

        new Thread(task).start();
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

    private void reset() {
        stopGame = true;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        gameConfigDto.getGames().forEach(Game::reset);
        scoreboardList.forEach(Score::reset);
        scoreboardTable.refresh();
        System.out.println("================================   RESET   ========================================");
    }
    @FXML
    private void onPlayButtonAction() {
        if (playButton.getText().toLowerCase().equals("play")) {
            stopGame = false;
            playGame();
            playButton.setText("Restart");
        } else {
            String message = "Do you want to reset the game and lose all the progress?";
            ButtonType result = showAlert("Game Reset", message);

            if(result.equals(ButtonType.OK)) {
                reset();
                playButton.setText("Play");
            }
        }
    }

    private ButtonType showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
        return alert.getResult();
    }

    public void onBackButtonMouseClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) backToSettingsButton.getScene().getWindow();
        stage.close();
    }
}
