package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;

    public GameResult playGame() {
        GameResult gameResult;
        while(true) {
            //TODO board.getFreeCellsAsString() - if null -> player2 won
            //TODO and getFreeCellsAsString should be coming from another converting class
            MoveResponse moveResponse = player1.askForMove(Mapper.getFreeCellsAsString(board.getFreeCells()));

            if(moveResponse.getMove() == null) {
                gameResult = new GameResult(player2.getPlayerDetails(), player1.getPlayerDetails(),
                        moveResponse.getResponseType().equals(ResponseType.TIMEOUT));
                break;
            }

            board.setNewMove(moveResponse.getMove());
//            board.updateCells();

            //TODO board.getFreeCellsAsString() - if null -> player1 won
            //TODO and getFreeCellsAsString should be coming from another converting class
            moveResponse = player2.askForMove(Mapper.getFreeCellsAsString(board.getFreeCells()));
            if(moveResponse.getMove() == null) {
                gameResult = new GameResult(player2.getPlayerDetails(), player1.getPlayerDetails(),
                        moveResponse.getResponseType().equals(ResponseType.TIMEOUT));
                break;
            }

            board.setNewMove(moveResponse.getMove());
//            board.updateCells();
        }
        finishGame();
        return gameResult;
    }

    private void finishGame() {
        player1.killPlayer();
        player2.killPlayer();
    }


}
