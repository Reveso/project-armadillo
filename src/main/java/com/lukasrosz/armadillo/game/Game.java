package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.model.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanFXPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.*;

import java.util.Objects;
import java.util.Random;

@Getter
public class Game {

    private AbstractPlayer movingPlayer;
    private AbstractPlayer waitingPlayer;
    private Board board;
    private GameResult gameResult;
    private boolean ended = false;
    private boolean started = false;
    private Move lastMove;
    private long fieldsNumber;

    public void reset() {
        int size = board.getSize();
        board = new Board(size);
        gameResult = null;
        ended = false;
        started = false;
        lastMove = null;
    }

    public Game(@NonNull AbstractPlayer movingPlayer, @NonNull AbstractPlayer waitingPlayer, @NonNull Board board) {
        this.movingPlayer = movingPlayer;
        this.waitingPlayer = waitingPlayer;
        this.board = board;
        fieldsNumber = board.getSize() * board.getSize();
    }

    private GameResult checkResponse(MoveResponse response, PlayerDetails potentialWinner,
                                     PlayerDetails potentialLoser) {

        if (response.getMove() == null) {

            if (board.checkIfEndGame()) {
                return new GameResult(potentialWinner, potentialLoser,
                        false, ResponseType.NORMAL);

            } else return new GameResult(potentialWinner, potentialLoser,
                    true, response.getResponseType());

        } else if (!board.setNewMove(response.getMove())) {

            if (board.checkIfEndGame()) {
                return new GameResult(potentialWinner, potentialLoser,
                        false, ResponseType.NORMAL);

            } else return new GameResult(potentialWinner, potentialLoser,
                    true, ResponseType.INVALID_MOVE);

        } else return null;
    }

    private boolean checkIfDisqualified(ResponseType responseType) {
        return !responseType.equals(ResponseType.NORMAL);
    }

    public void finishGame() {
        ended = true;
        movingPlayer.killPlayer();
        waitingPlayer.killPlayer();
    }

    private void pickStartingPlayer() {
        if (waitingPlayer instanceof HumanFXPlayer) {
            return;
        } else if (movingPlayer instanceof HumanFXPlayer) {
            swapPlayers();
            return;
        }
        Random random = new Random();
        if (random.nextInt(2) == 1) {
            swapPlayers();
        }
    }

    private void initializeGame() throws PlayerInitializationException {
        pickStartingPlayer();

        boolean success1 = movingPlayer.initialize(board.getSize(), PointsMapper.getPointsAsString(board.getOccupiedFields()));
        boolean success2 = waitingPlayer.initialize(board.getSize(), PointsMapper.getPointsAsString(board.getOccupiedFields()));

        if (!success1) {
            finishGame();

            gameResult = new GameResult(waitingPlayer.getPlayerDetails(), movingPlayer.getPlayerDetails(),
                    true, ResponseType.INITIALIZATION_EXCEPTION);

            throw new PlayerInitializationException(movingPlayer.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        } else if (!success2) {
            finishGame();

            gameResult = new GameResult(movingPlayer.getPlayerDetails(), waitingPlayer.getPlayerDetails(),
                    true, ResponseType.INITIALIZATION_EXCEPTION);

            throw new PlayerInitializationException(waitingPlayer.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        }
    }

    public GameResponse nextMove() throws PlayerInitializationException {
        if (ended) return new GameResponse(null, 1.0);

        String message;
        if (!started) {
            initializeGame();
            message = "start";
            started = true;
        } else {
            message = PointsMapper.getMoveAsString(lastMove);
        }

        val moveResponse = movingPlayer.askForMove(message);

        gameResult = checkResponse(moveResponse, waitingPlayer.getPlayerDetails(),
                movingPlayer.getPlayerDetails());

        if (gameResult != null) {
            if (!gameResult.getGameFinishType().equals(ResponseType.NORMAL)) {
                swapPlayers();
            }
            finishGame();
            displayEndLog();
            return new GameResponse(null, 1.0);
        }

        swapPlayers();
        lastMove = moveResponse.getMove();

        val gameResponse = new GameResponse();
        gameResponse.setMove(lastMove);
        gameResponse.setOccupiedFields((double) board.getOccupiedFields().size() / fieldsNumber);

        System.out.println(gameResponse.getOccupiedFields());
        System.out.println("===============");

        return gameResponse;
    }

    private void displayEndLog() {
        System.out.println("END GAME: " + board.checkIfEndGame());
        System.out.println("DSQ: " + gameResult.isDisqualified());
        System.out.println("Free fields: " + board.getFreeFields());
        System.out.println("Moved: " + movingPlayer.getPlayerDetails().getAlias());
    }

    private void swapPlayers() {
        AbstractPlayer temp = movingPlayer;
        movingPlayer = waitingPlayer;
        waitingPlayer = temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return (Objects.equals(movingPlayer, game.movingPlayer) &&
                Objects.equals(waitingPlayer, game.waitingPlayer)) || (Objects.equals(movingPlayer, game.waitingPlayer) &&
                Objects.equals(waitingPlayer, game.movingPlayer));
    }

    @Override
    public int hashCode() {
        return 17;
    }

}
