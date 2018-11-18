package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Point;

import java.util.List;

public class Mapper {

    private Board board;

    public String getFreeCellsAsString() {
        List<Point> freeCells = board.getFreeCells();
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : freeCells) {
            stringBuilder.append("{").append(point.getX()).append(";").append(point.getY()).append("}").append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1,  stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
