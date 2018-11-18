package com.lukasrosz.armadillo.game;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class BoardTestSuite {

    Board board;

    @Test
    public void boardTest() {
        board = new Board(9);
        assertNotNull(board);
    }

    @Test
    public void checkIfOutOfRangeTest() {

    }

}
