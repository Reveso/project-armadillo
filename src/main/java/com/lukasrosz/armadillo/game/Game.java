package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.model.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanFXPlayer;
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
        fieldsNumber = board.getSize()*board.getSize();
    }

    private GameResult checkResponse(MoveResponse response, AbstractPlayer potentialWinner,
                                 AbstractPlayer potentialLoser) {
        if (response.getMove() == null || !board.setNewMove(response.getMove())) {
            return new GameResult(potentialWinner.getPlayerDetails(),
                    potentialLoser.getPlayerDetails(),
                    checkIfDisqualified(ResponseType.EXCEPTION),
                    GameFinishType.INVALID_MOVE);
            //TODO Different game finish types depending on situation
        } else return null;
    }

    private GameResult checkIfEndGame(AbstractPlayer winner, AbstractPlayer loser) {
        if(board.checkIfEndGame()) {
            return new GameResult(winner.getPlayerDetails(),
                    loser.getPlayerDetails(), false, GameFinishType.NORMAL);
        } else return null;
    }

    private boolean checkIfDisqualified(ResponseType responseType) {
        return responseType.equals(ResponseType.EXCEPTION) ||
                responseType.equals(ResponseType.TIMEOUT);
    }

    public void finishGame() {
        ended = true;
        movingPlayer.killPlayer();
        waitingPlayer.killPlayer();
    }

    private void pickStartingPlayer() {
        if(waitingPlayer instanceof HumanFXPlayer) {
            return;
        } else if (movingPlayer instanceof HumanFXPlayer) {
            swapPlayers();
            return;
        }
        Random random = new Random();
        if(random.nextInt(2) == 1) {
            swapPlayers();
        }
    }

    private void initializeGame() throws PlayerInitializationException {
        pickStartingPlayer();

        boolean success1 = movingPlayer.initialize(board.getSize(), PointsMapper.getPointsAsString(board.getOccupiedFields()));
        boolean success2 = waitingPlayer.initialize(board.getSize(), PointsMapper.getPointsAsString(board.getOccupiedFields()));

        if(!success1) {
            finishGame();

            gameResult = new GameResult(waitingPlayer.getPlayerDetails(), movingPlayer.getPlayerDetails(),
                    true, GameFinishType.INITIALIZATION_EXCEPTION);

            throw new PlayerInitializationException(movingPlayer.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        } else if (!success2) {
            finishGame();

            gameResult = new GameResult(movingPlayer.getPlayerDetails(), waitingPlayer.getPlayerDetails(),
                    true, GameFinishType.INITIALIZATION_EXCEPTION);

            throw new PlayerInitializationException(waitingPlayer.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        }
    }

    public GameResponse nextMove() throws PlayerInitializationException {
        String message;
        if(!started) {
            initializeGame();
            message = "start";
            started = true;
        } else {
            message = PointsMapper.getMoveAsString(lastMove);
        }

        if (ended) return null;

        val moveResponse = movingPlayer.askForMove(message);

        gameResult = checkResponse(moveResponse, waitingPlayer, movingPlayer);

        //TODO UNSAFE
        if(gameResult != null) {
            finishGame();
            System.out.println("END GAME: " + board.checkIfEndGame());
            System.out.println("DSQ: " + gameResult.isDisqualified());
            System.out.println("Free fields: " + board.getFreeFields());
            System.out.println("Moved: " + movingPlayer.getPlayerDetails().getAlias());
            return new GameResponse(null,1.0);
        }

        gameResult = checkIfEndGame(movingPlayer, waitingPlayer);
        if(gameResult != null) {
            finishGame();
            lastMove = moveResponse.getMove();
            System.out.println("END GAME: " + board.checkIfEndGame());
            System.out.println("DSQ: " + gameResult.isDisqualified());
            System.out.println("Free fields: " + board.getFreeFields());
            System.out.println("Moved: " + movingPlayer.getPlayerDetails().getAlias());
            return new GameResponse(moveResponse.getMove(), 1.0);
        }

        swapPlayers();
        lastMove = moveResponse.getMove();
        val gameResponse = new GameResponse();
        gameResponse.setMove(lastMove);

        System.out.println("===============");
        gameResponse.setOccupiedFields((double)board.getOccupiedFields().size() / fieldsNumber);
        System.out.println(gameResponse.getOccupiedFields());

        return gameResponse;
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
