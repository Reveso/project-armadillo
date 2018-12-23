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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FightStageController {

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
    private GridPane boardGridPane;
    @FXML
    private Button pauseButton;

    @Getter
    private final ObservableList<Score> scoreboardList = FXCollections.observableArrayList();
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
        new File("referee_files/scoreboard").mkdirs();
        new File("referee_files/logs").mkdirs();

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
            saveScoreboardToFile();
            return false;
        }
    }

    private String getShortDate() {
        DateFormat df = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
    private void saveScoreboardToFile() {
        String shortDate = getShortDate();
        String filename = shortDate + "_scoreboard.txt";

        try (PrintStream  bf = new PrintStream("referee_files/scoreboard/" + filename)) {
            bf.println("Alias, Surname, Victories, Defeats, Disqualifications");
            for (Score score : scoreboardList) {
                bf.println(score.getAlias() + ", " + score.getSurname() + ", " + score.getVictories()
                        + ", " + score.getDefeats() + ", " + score.getDisqualifications());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeTimeline(Game game) {
        logger = Logger.getLogger("GameLog");
        try {
            fileHandler = new FileHandler("referee_files/logs/" + "LOG_" + getShortDate() + "_"
                    + game.getMovingPlayer().getPlayerDetails().getAlias() + "_"
                    + game.getWaitingPlayer().getPlayerDetails().getAlias() + ".txt");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            e.printStackTrace();
        }

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                setupTimelineEvent(game);
            }
        };
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
//        logger.info(game.getWaitingPlayer().getPlayerDetails().getAlias());
//        logger.info(PointsMapper.getMoveAsString(move));

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

    private void playGame() {
        if (makeTimeline()) {
            makeBoard();
            animationTimer.start();
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
            animationTimer.stop();
            pauseButton.setText("Play");

        } else {
            if (animationTimer != null) {
                animationTimer.start();
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
