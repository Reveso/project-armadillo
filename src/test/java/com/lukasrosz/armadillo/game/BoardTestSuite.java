package com.lukasrosz.armadillo.game;

import lombok.val;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTestSuite {

    private Board board;

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
        List<Point> pointsOnStart = board.getFreeFields();
        val p1 = new Point(2, 3);
        val p2 = new Point(3, 3);

        Move move = new Move(p1, p2);
        if (board.cellOccupied(p1) && board.cellOccupied(p2)) {
            assertTrue(board.setNewMove(move));
            assertNotEquals(pointsOnStart, board.getFreeFields());
            assertTrue(board.getOccupiedFields().contains(new Point(3,3)));
            assertTrue(board.getOccupiedFields().contains(new Point(2,3)));
        } else {
            assertEquals(pointsOnStart, board.getFreeFields());
        }

    }


}
