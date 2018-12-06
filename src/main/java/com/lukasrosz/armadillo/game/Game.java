package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.communication.exception.PlayerInitializationException;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanFXPlayer;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.*;

import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

@Getter
public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;
    private GameResult gameResult;
    private boolean ended = false;
    private boolean started = false;
    private Move lastMove;

    public Game(@NonNull AbstractPlayer player1, @NonNull AbstractPlayer player2, @NonNull Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    private GameResult checkResponse(MoveResponse response, AbstractPlayer potentialWinner,
                                 AbstractPlayer potentialLoser) {
        if (response.getMove() == null || !board.setNewMove(response.getMove())) {
            return new GameResult(potentialWinner.getPlayerDetails(),
                    potentialLoser.getPlayerDetails(),
                    checkIfDisqualified(response.getResponseType()));
        } else return null;
    }

    private GameResult checkIfEndGame(AbstractPlayer winner, AbstractPlayer loser) {
        if(board.checkIfEndGame()) {
            return new GameResult(winner.getPlayerDetails(),
                    loser.getPlayerDetails(), false);
        } else return null;
    }

    private boolean checkIfDisqualified(ResponseType responseType) {
        return responseType.equals(ResponseType.EXCEPTION) ||
                responseType.equals(ResponseType.TIMEOUT);
    }

    private void finishGame() {
        ended = true;
        player1.killPlayer();
        player2.killPlayer();
    }

    private void pickStartingPlayer() {
        if(player2 instanceof HumanFXPlayer) {
            return;
        } else if (player1 instanceof HumanFXPlayer) {
            swapPlayers();
        }
        Random random = new Random();
        if(random.nextInt(2) == 1) {
            swapPlayers();
        }
    }

    private void initializeGame() throws PlayerInitializationException {
        pickStartingPlayer();

        boolean success1 = player1.initialize(board.getSize(), Mapper.getPointsAsString(board.getOccupiedCells()));
        boolean success2 = player2.initialize(board.getSize(), Mapper.getPointsAsString(board.getOccupiedCells()));
        if(!success1) {
            finishGame();
            throw new PlayerInitializationException(player1.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        } else if (!success2) {
            finishGame();
            throw new PlayerInitializationException(player2.getPlayerDetails().getAlias()
                    + " was not properly initialized");
        }
    }

    public Move nextMove() throws PlayerInitializationException {
        String message;
        if(!started) {
            initializeGame();
            message = "start";
            started = true;
        } else {
            message = Mapper.getMoveAsString(lastMove);
        }

        if (ended) return null;

        val moveResponse = player1.askForMove(message);

        gameResult = checkResponse(moveResponse, player2, player1);
        if(gameResult != null) {
            finishGame();
            return null;
        }

        gameResult = checkIfEndGame(player1, player2);
        if(gameResult != null) {
            finishGame();
            return moveResponse.getMove();
        }

        swapPlayers();
        lastMove = moveResponse.getMove();
        return moveResponse.getMove();
    }

    private void swapPlayers() {
        AbstractPlayer temp = player1;
        player1 = player2;
        player2 = temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return (Objects.equals(player1, game.player1) &&
                Objects.equals(player2, game.player2)) || (Objects.equals(player1, game.player2) &&
                Objects.equals(player2, game.player1));
    }

    @Override
    public int hashCode() {
        return 17;
    }
}
