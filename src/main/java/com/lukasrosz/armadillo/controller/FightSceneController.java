package com.lukasrosz.armadillo.controller;

import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
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
    private ProgressBar gameProgressBar;

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
        populateScoreboard();
        singleProgress = 1.0 / gameConfigDto.getGames().size();
        System.out.println(singleProgress);
        System.out.println(gameConfigDto.getGames().size());
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
            bf.println("Alias, Surname, Victories, Defeats, Disqualifications");
            for (Score score : scoreboardList) {
                bf.println(score.getAlias() + ", " + score.getSurname() + ", " + score.getVictories()
                        + ", " + score.getDefeats() + ", " + score.getDisqualifications());
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
//        animationTimeline = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                setupTimelineEvent(game);
//            }
//        };

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
                Move move = game.nextMove();
                System.out.println(move);
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

//        for (Game game : gameConfigDto.getGames()) {
//            System.out.println(game);
//            while (!game.isEnded()) {
//                try {
//                    Move move = game.nextMove();
//                    System.out.println(move);
//                } catch (Exception e) {
//                    System.out.println(e);
//                }
////                Thread.sleep(1000);
//            }
//            sumUpGame(game);
//            gameProgressBar.setProgress(gameProgressBar.getProgress() + 0.25);
//            System.out.println("=========================================");
//        }
//        saveScoreboardToFile();
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
