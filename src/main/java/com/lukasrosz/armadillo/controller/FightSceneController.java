package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.GameResponse;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    private Iterator<Game> gameIterator;
    private Timeline animationTimeline;
    private double singleProgress;

    public void initialize() {
        fightTitleLabel.setFont(Font.font(null, FontWeight.BOLD, 16));
        new File("referee_files/scoreboard").mkdirs();
        new File("referee_files/logs").mkdirs();

    }

    public void setup(GameConfigDto gameConfigDto) {
        this.gameConfigDto = gameConfigDto;
        gameIterator = gameConfigDto.getGames().iterator();
        singleProgress = 1.0 / gameConfigDto.getGames().size();
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

    private void sumUpGame(Game game) {
        saveGameResult(game.getGameResult());
        Collections.sort(scoreboardList);
        scoreboardTable.refresh();
        fightTitleLabel.setText("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                + game.getGameResult().getLoser().getAlias());
        animationTimeline.stop();
        playGame();
    }

    private Thread assembleGameThread(Game game) {
        Task task = new Task<Void>() {
            @Override
            protected Void call() {
                while (!game.isEnded()) {
                    try {
                        GameResponse gameResponse = game.nextMove();
                        updateProgress(gameResponse.getOccupiedFields(), 1.0);
                    } catch (PlayerInitializationException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println(game.isEnded());
                updateProgress(1.0, 1.0);
                return null;
            }
        };
        roundProgressBar.progressProperty().bind(task.progressProperty());
        return new Thread(task);
    }

    private void playGame() {
        Task<Double> task = new Task<Double>() {
            @Override
            protected Double call() {
                int progress = 0;
                updateProgress(progress, gameConfigDto.getGames().size());
                while(gameIterator.hasNext()) {
                    Game game = gameIterator.next();

                    updateTitle(game.getMovingPlayer().getPlayerDetails().getAlias() + " vs "
                            + game.getWaitingPlayer().getPlayerDetails().getAlias());

                    while (!game.isEnded()) {
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

    private void restartGame() {

    }
    
    @FXML
    private void onPlayButtonAction() {
        if (playButton.getText().toLowerCase().equals("play")) {
            playGame();
            playButton.setText("Restart");
        } else {

        }

    }

    public void onBackButtonMouseClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) backToSettingsButton.getScene().getWindow();
        stage.close();
    }
}
