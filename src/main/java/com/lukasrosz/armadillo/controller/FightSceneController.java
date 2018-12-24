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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private Button pauseButton;
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
//        scoreboardTable.refresh();
        fightTitleLabel.setText("Winner: " + game.getGameResult().getWinner().getAlias() + " Looser: "
                + game.getGameResult().getLoser().getAlias());
        animationTimeline.stop();
        playGame();
    }

    private void setupNewGame(Game game) {
        fightTitleLabel.setText(game.getMovingPlayer().getPlayerDetails().getAlias() + " vs "
                + game.getWaitingPlayer().getPlayerDetails().getAlias());
    }


    private boolean makeTimeline() {
        if (gameIterator.hasNext()) {
            makeTimeline(gameIterator.next());
            return true;
        } else {
            pauseButton.setDisable(true);
            saveScoreboardToFile();
            return false;
        }
    }

    private void makeTimeline(Game game) {
        animationTimeline = new Timeline(
                new KeyFrame(
                        Duration.millis(1),
                        event -> {
                            setupTimelineEvent(game);

                        })
        );
        animationTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void setupTimelineEvent(Game game) {
        if (!game.isStarted()) {
            setupNewGame(game);
        }

       if (!game.isEnded()) {
            try {
//                Move move = game.nextMove();
//                System.out.println(move);
                GameResponse gameResponse = game.nextMove();
//                System.out.println(gameResponse.getMove());
                if(gameResponse != null) {
                    if(gameResponse.getOccupiedFields() >= 1) {
                        roundProgressBar.setProgress(1.0);
                    } if(gameResponse.getOccupiedFields() > 0.75) {
                        roundProgressBar.setProgress(0.75);
                    } if(gameResponse.getOccupiedFields() > 0.5) {
                        roundProgressBar.setProgress(0.5);
                    } if(gameResponse.getOccupiedFields() > 0.25) {
                        roundProgressBar.setProgress(0.25);
                    }

                } else {
                    roundProgressBar.setProgress(1.0);
                }
            } catch (PlayerInitializationException e) {
                System.err.println(e);
            }
        } else {
            sumUpGame(game);
            gameProgressBar.setProgress(gameProgressBar.getProgress() + singleProgress);
        }
    }


    private void playGame() {
        if (makeTimeline()) {
            animationTimeline.play();
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
            animationTimeline.stop();
            pauseButton.setText("Play");

        } else {
            if (animationTimeline != null) {
                animationTimeline.play();
            } else {

                playGame();
            }
            pauseButton.setText("Pause");
        }
    }
    public void onBackButtonMouseClicked(MouseEvent mouseEvent) {
        Stage stage = (Stage) backToSettingsButton.getScene().getWindow();
        stage.close();
    }
}
