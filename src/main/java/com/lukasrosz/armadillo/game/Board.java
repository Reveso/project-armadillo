package com.lukasrosz.armadillo.game;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Board {

    private int size;
    private List<Point> freeFields;
    private List<Point> occupiedFields;

    private void init() {
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                freeFields.add(new Point(i,j));
            }
        }
        generateRandomPoints();
    }

    private void generateRandomPoints() {
        int pointsToGenerate = (int) (size * size * 0.1);
        while (pointsToGenerate > 0) {
            Point randomPoint = Point.generatePoint(0, size - 1);
            if (freeFields.contains(randomPoint)) {
                freeFields.remove(randomPoint);
                pointsToGenerate--;
                if (!occupiedFields.contains(randomPoint)) {
                    occupiedFields.add(randomPoint);
                }
            }
        }
    }

    public Board(int size) /*throws Exception */{
        /*if (size % 2 == 0 || size < 13 || size > 51) throw new Exception("not good number");*/
        this.size = size;
        freeFields = new LinkedList<>();
        occupiedFields = new LinkedList<>();
        init();
    }

    public static boolean checkIfNeighbours(int boardSize, Point p1, Point p2) {
        if(Math.abs(p1.getX() - p2.getX()) == 1 && Math.abs(p1.getY() - p2.getY()) == 0 ||
                Math.abs(p1.getX() - p2.getX()) == 0 && Math.abs(p1.getY() - p2.getY()) == 1) {
            return true;
        } else if((Math.abs(p1.getX() - p2.getX()) == boardSize-1 && Math.abs(p1.getY() - p2.getY()) == 0) ||
                (Math.abs(p1.getX() - p2.getX()) == 0 && Math.abs(p1.getY() - p2.getY()) == boardSize-1)){
            return true;
        } else return false;
    }

    public boolean setNewMove(Move move) {
        if (occupiedFields.contains(move.getPoint1()) || occupiedFields.contains(move.getPoint2())) {
            return false;
        } else {
            if (Board.checkIfNeighbours(this.size, move.getPoint1(), move.getPoint2())) {
                setFieldsOccupied(move.getPoint1(), move.getPoint2());
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean cellOccupied(Point point) {
        return occupiedFields.contains(point);
    }

    private void setFieldsOccupied(Point... points) {
        for (Point point: points) {
            freeFields.remove(point);
            occupiedFields.add(point);
        }
    }

    public boolean checkIfEndGame() {
        if (freeFields == null) {
            return true;
        } else {
            for(Point p1 : freeFields) {
                for(Point p2 : freeFields) {
                    if(Board.checkIfNeighbours(this.size, p1, p2)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public void setFullBoard() {
        occupiedFields = new LinkedList<>();
        freeFields = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                occupiedFields.add(new Point(i,j));
            }
        }
    }
}
