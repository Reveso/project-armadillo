package com.lukasrosz.armadillo.game;

import com.lukasrosz.armadillo.player.AbstractPlayer;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;

    public String playGame() {
        while(endOfGame()) {
            Move move = player1.askForMove(board.getFreeCellsAsString());
            if(move == null) {
                return "someVariable"; //TODO player1 loses
            }
            board.setNewMove(move);
//            board.updateCells();
            move = player2.askForMove(board.getFreeCellsAsString());
            if(move == null) {
                return "someVariable"; //TODO player2 loses
            }
            board.setNewMove(move);
//            board.updateCells();
        }
        return null;
    }

    private boolean endOfGame() {
        return false;
    }


}
