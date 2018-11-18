package com.lukasrosz.armadillo.game;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTestSuite {

    Board board;

    @Test
    public void boardTest() {
        board = new Board(9);
        assertNotNull(board);
    }

    @Test
    public void checkIfEndGame() {
        board = new Board(5);
        assertFalse(board.checkIfEndGame());
        board.setFullBoard();
        assertTrue(board.checkIfEndGame());
    }

    @Test
    public void move() {
        board = new Board(5);
        List<Point> pointsOnStart = board.getFreeCells();
        Move move = new Move(new Point(2,3), new Point(3,3));
        if (board.cellOccupied(2,3) && board.cellOccupied(3,3)) {
            assertTrue(board.setNewMove(move));
            assertNotEquals(pointsOnStart, board.getFreeCells());
            assertTrue(board.getOccupiedCells().contains(new Point(3,3)));
            assertTrue(board.getOccupiedCells().contains(new Point(2,3)));
        } else {
            assertEquals(pointsOnStart, board.getFreeCells());
        }

    }


}
