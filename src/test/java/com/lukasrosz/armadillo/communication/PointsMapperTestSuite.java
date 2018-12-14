package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Point;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class PointsMapperTestSuite {
    @Test
    public void getStringAsCellsTest() {
        String test = "{2;1},{3;3}";
        List<Point> points = PointsMapper.getStringAsPoints(test);
//        System.out.println(points);
        List<Point> asserPoints = new ArrayList<>();
        asserPoints.add(new Point(2, 1));
        asserPoints.add(new Point(3, 3));
        assertEquals(asserPoints, points);
    }

    @Test
    public void getFreeCellsAsStringTest() {

        Board boardMock = mock(Board.class);
        List<Point> cells = new ArrayList<>();
        cells.add(new Point(3,1));
        cells.add(new Point(2,1));
        cells.add(new Point(1,1));
        cells.add(new Point(7,1));

        String score = PointsMapper.getPointsAsString(cells);
        assertEquals("{3;1},{2;1},{1;1},{7;1}", score);

    }
}
