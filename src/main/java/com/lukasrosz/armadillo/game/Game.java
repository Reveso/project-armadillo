package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.sun.org.apache.regexp.internal.RE;
import lombok.*;

@EqualsAndHashCode
public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;
    @Getter
    private GameResult gameResult;
    @Getter
    private boolean gameEnded;

    public Game(@NonNull AbstractPlayer player1, @NonNull AbstractPlayer player2, @NonNull Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        gameEnded = false;
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
        gameEnded = true;
        player1.killPlayer();
        player2.killPlayer();
    }

    public Move nextMove() {
        if (gameEnded) return null;
        val moveResponse = player1.askForMove(Mapper.getPointsAsString(board.getFreeCells()));

        gameResult = checkResponse(moveResponse, player1, player2);
        if(gameResult != null) {
            finishGame();
            return null;
        }

        gameResult = checkIfEndGame(player1, player2);
        if(gameResult != null) {
            finishGame();
            return null;
        }

        swapPlayers();
        return moveResponse.getMove();
    }

    private void swapPlayers() {
        AbstractPlayer temp = player1;
        player1 = player2;
        player2 = temp;
    }

}
