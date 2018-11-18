package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MapperTestSuite {
    @Test
    public void getStringAsCellsTest() {
        String test = new String("{2;1},{3;3}");
        List<Point> points = Mapper.getStringAsCells(test);
//        System.out.println(points);
        assertEquals("[Point{x=2, y=1}, Point{x=3, y=3}]", points.toString());
    }

    @Test
    public void getFreeCellsAsStringTest() {

        Board boardMock = mock(Board.class);
        List<Point> cells = new ArrayList<>();
        cells.add(new Point(3,1));
        cells.add(new Point(2,1));
        cells.add(new Point(1,1));
        cells.add(new Point(7,1));
        when(boardMock.getFreeCells()).thenReturn(cells);

        String score = Mapper.getFreeCellsAsString(boardMock);
        assertEquals("{3;1},{2;1},{1;1},{7;1}", score);

    }
}
