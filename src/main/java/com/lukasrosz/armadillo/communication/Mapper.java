package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Point;

import java.util.ArrayList;
import java.util.List;

public class Mapper {

    private static final String STRING_MATCHER = "(\\{\\d;\\d\\})++(,\\{\\d;\\d\\})*";

    public static String getFreeCellsAsString(Board board) {
        List<Point> freeCells = board.getFreeCells();
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : freeCells) {
            stringBuilder.append("{").append(point.getX()).append(";").append(point.getY()).append("}").append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1,  stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static List<Point> getStringAsCells(String string) {
        if (!string.matches(STRING_MATCHER)) {
            System.err.println("something is wrong in string received");
            return null;
        } else {
            List<Point> cells = new ArrayList<>();
            String[] strings = string.split(",");
            for (String s: strings) {
                cells.add(new Point(Integer.parseInt(String.valueOf(s.charAt(1))), Integer.parseInt(String.valueOf(s.charAt(3)))));
            }
            return cells;
        }
    }

    public static void main(String[] args) {
        String string = new String("{3;4},{8;5}");
        System.out.println(string.matches("(\\{\\d;\\d\\})++(,\\{\\d;\\d\\})*"));
    }
}
