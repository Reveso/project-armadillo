package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.AllArgsConstructor;
import lombok.NonNull;

public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;

    public Game(@NonNull AbstractPlayer player1, @NonNull AbstractPlayer player2, @NonNull Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public GameResult playGame() {
        GameResult gameResult = gameLoop();
        finishGame();
        return gameResult;
    }

    private GameResult gameLoop() {
        GameResult gameResult = null;
        while (true) {
            gameResult = checkIfEndGame(player2, player1);
            if (gameResult != null) return gameResult;

            MoveResponse moveResponse = player1.askForMove(Mapper.getPointsAsString(board.getFreeCells()));

            gameResult = checkResponse(moveResponse, player2, player1);
            if (gameResult != null) return gameResult;

            gameResult = checkIfEndGame(player1, player2);
            if (gameResult != null) return gameResult;

            moveResponse = player2.askForMove(Mapper.getPointsAsString(board.getFreeCells()));

            gameResult = checkResponse(moveResponse, player1, player2);
            if (gameResult != null) return gameResult;
        }
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
        if(responseType.equals(ResponseType.EXCEPTION) ||
                responseType.equals(ResponseType.TIMEOUT)) {
            return true;
        } else {
            return false;
        }
    }

    private void finishGame() {
        player1.killPlayer();
        player2.killPlayer();
    }

}
