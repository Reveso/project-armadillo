package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.val;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PointsMapper {


    public static String getPointsAsString(List<Point> points) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : points) {
            stringBuilder.append("{").append(point.getX()).append(";").append(point.getY()).append("}").append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static Point getStringAsPoint(String point) {
        point = point.replaceAll("[{]", "")
                .replaceAll("[}]", "");
        String[] xy = point.split(";");
        return new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
    }

    public static List<Point> getStringAsPoints(String stringPoints) {
        try {
            List<Point> points = new ArrayList<>();
            String[] fullPoints = stringPoints.split(",");
            for (String fullPoint : fullPoints) {
                fullPoint = fullPoint.replaceAll("[{]", "")
                        .replaceAll("[}]", "");
                String[] xy = fullPoint.split(";");

                points.add(new Point(Integer.parseInt(xy[0]), Integer.parseInt(xy[1])));
            }
            return points;
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    public static Move getStringAsMove(String stringPoints) {
        List<Point> points = getStringAsPoints(stringPoints);
        if(points != null && points.size() == 2) {
            return new Move(points.get(0), points.get(1));
        } else {
            return null;
        }
    }

    public static String getMoveAsString(Move move) {
        val moveAsList = new LinkedList<Point>();
        moveAsList.add(move.getPoint1());
        moveAsList.add(move.getPoint2());
        return getPointsAsString(moveAsList);
    }
}
