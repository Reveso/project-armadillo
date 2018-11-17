package com.lukasrosz.armadillo.mainpack;

import com.lukasrosz.armadillo.player.AbstractPlayer;

public class Game {

    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private Board board;
    private Move move;

    public Game(AbstractPlayer player1, AbstractPlayer player2, Board board) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
    }

    public void playGame() {
        while(endOfGame()) {
            move = player1.askForMove(board.getFreeCellsAsString()); //TODO If .askForMove return null player loses
            board.setNewMove(move);
//            board.updateCells();
            move = player2.askForMove(board.getFreeCellsAsString());
            board.setNewMove(move);
//            board.updateCells();
        }
    }

    private boolean endOfGame() {
        return false;
    }


}
