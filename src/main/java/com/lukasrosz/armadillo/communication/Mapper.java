package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Mapper {

    private static final String STRING_MATCHER = "(\\{\\d;\\d\\})++(,\\{\\d;\\d\\})*";

    public static String getPointsAsString(List<Point> points) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : points) {
            stringBuilder.append("{").append(point.getX()).append(";").append(point.getY()).append("}").append(",");
        }
//        stringBuilder.delete(stringBuilder.length() ,  stringBuilder.length());
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
//        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public static Point getStringAsPoint(String point) {
        point = point.replaceAll("[{]", "")
                .replaceAll("[}]", "");
        String[] xy = point.split(";");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

    public static List<Point> getStringAsPoints(String stringPoints) {
//        if (!stringPoints.matches(STRING_MATCHER)) {
//            System.err.println("something is wrong in string received");
//            return null;
//        } else {
//            List<Point> points = new ArrayList<>();
//            String[] strings = stringPoints.split(",");
//            for (String s: strings) {
//                points.add(new Point(Integer.parseInt(String.valueOf(s.charAt(1))), Integer.parseInt(String.valueOf(s.charAt(3)))));
//            }
//            return points;
//        }
        List<Point> points = new ArrayList<>();
        String[] fullPoints = stringPoints.split(",");
        for (String fullPoint : fullPoints) {
            fullPoint = fullPoint.replaceAll("[{]", "")
                    .replaceAll("[}]", "");
            String[] xy = fullPoint.split(";");
            points.add(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
        }
        return points;
    }

    public static String getMoveAsString(Move move) {
        val moveAsList = new LinkedList<Point>();
        moveAsList.add(move.getPoint1());
        moveAsList.add(move.getPoint2());
        return getPointsAsString(moveAsList);
    }
}
