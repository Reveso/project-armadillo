package com.lukasrosz.armadillo.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board {

//    in board true means its occupied
    private boolean[][] board;
    private int size;
    private List<Point> freeCells;
    private List<Point> occupiedCells;
    private boolean oddSize;

    private enum LimesValues {
        TOP_SIDE, BOTTOM_SIDE, RIGHT_SIDE, LEFT_SIDE, Unexceptable ;
    }
//    UNEXCEPTABLE

    public void init() {
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                freeCells.add(new Point(i,j));
            }
        }
        generateRandomPoints();
    }

    private void generateRandomPoints() {
        int pointsToGenerate = (int) (size * size * 0.1);
        while (pointsToGenerate > 0) {
            Point randomPoint = Point.generatePoint(0, size - 1);
            if (freeCells.contains(randomPoint)) {
                freeCells.remove(randomPoint);
                pointsToGenerate--;
                if (!occupiedCells.contains(randomPoint)) {
                    occupiedCells.add(randomPoint);
                }
            }
        }
    }

    public Board(int size) /*throws Exception */{
        /*if (size % 2 == 0 || size < 13 || size > 51) throw new Exception("not good number");*/
        this.size = size;
        board = new boolean[size][size];
        freeCells = new ArrayList<>();
        occupiedCells = new ArrayList<>();
        init();
    }

    private void ifSizeIsOddFlag() {
        if (size % 2 == 1)
            oddSize = true;
    }

    private boolean isOnEdge(Point point) {
        if (point.getX() == 0 || point.getX() == size - 1 ||
                point.getY() == 0 || point.getY() == size - 1) {
            return  true;
        } else {
            return false;
        }
    }

    private boolean CellIsOccupied(int x, int y) {
        if (board[x][y] == true) {
            return true;
        } else {
            return false;
        }
    }

    private LimesValues checkLimesOnCell(int x, int y) {
        if (x == size - 1) {
            return LimesValues.RIGHT_SIDE;
        } else if (x == 0) {
            return LimesValues.LEFT_SIDE;
        } else if (y == size - 1) {
            return LimesValues.TOP_SIDE;
        } else if (y == 0) {
            return LimesValues.BOTTOM_SIDE;
        }
        return LimesValues.Unexceptable;
    }

    private boolean checkIfRightOccupied(int x, int y) {
        if (checkLimesOnCell(x, y).equals(LimesValues.RIGHT_SIDE)) {
            return CellIsOccupied(0, y);
        } else {
            return CellIsOccupied(x + 1, y);
        }
    }

    private boolean checkIfLeftOccupied(int x, int y) {
        if (checkLimesOnCell(x, y).equals(LimesValues.LEFT_SIDE)) {
            return CellIsOccupied(size - 1, y);
        } else {
            return CellIsOccupied(x - 1, y);
        }
    }

    private boolean checkIfTopOccupied(int x, int y) {
        if (checkLimesOnCell(x, y).equals(LimesValues.TOP_SIDE)) {
            return CellIsOccupied(x, 0);
        } else {
            return CellIsOccupied(x, y + 1);
        }
    }

    private boolean checkIfBottomOccupied(int x, int y) {
        if (checkLimesOnCell(x, y).equals(LimesValues.BOTTOM_SIDE)) {
            return CellIsOccupied(x, size - 1);
        } else {
            return CellIsOccupied(x, y - 1);
//            return CellIsOccupied(x, 0);
        }
    }

    /*private boolean checkCellNeighboursIfOccupied(int x, int y) {
        return (checkIfBottomOccupied(x, y) && checkIfTopOccupied(x, y)
        && checkIfLeftOccupied(x, y) && checkIfRightOccupied(x, y));
    }*/

    private boolean checkCellNeighboursIfOccupied(int x, int y) {
        return cellOccupied(convertToValidMove(new Point(x + 1, y)))
                && cellOccupied(convertToValidMove(new Point(x - 1, y)))
                && cellOccupied(convertToValidMove(new Point(x, y + 1)))
                && cellOccupied(convertToValidMove(new Point(x, y - 1)));
    }

    private Point convertToValidMove(Point point) {
        int x = point.getX();
        int y = point.getY();

        if (x >= size) {
            x = 0;
        }
        if (y >= size) {
            y = 0;
        }
        if (x < 0) {
            x = size - 1;
        }
        if (y < 0) {
            y = size - 1;
        }
        return new Point(x,y);
    }

    private boolean checkNeighbouringCellsIfOccupiedIfCellIsNotOnEdge(Point point) {
        int x = point.getX();
        int y = point.getY();
        return (CellIsOccupied(x, y - 1) && CellIsOccupied(x, y + 1)
                && CellIsOccupied(x - 1, y) && CellIsOccupied(x + 1, y));
    }

    private boolean checkIfOutOfRange(int x, int y) {
        if (x < 0 || x > size -1 || y < 0 || y > size -1 ) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkIfNeiNeighbours(Point p1, Point p2) {
        if(Math.abs(p1.getX() - p2.getX()) == 1 && Math.abs(p1.getY() - p2.getY()) == 0 ||
                Math.abs(p1.getX() - p2.getX()) == 0 && Math.abs(p1.getY() - p2.getY()) == 1) {
            return true;
        } else return false;
    }

    public boolean setNewMove(Move move) {
        if (occupiedCells.contains(move.getPoint1()) || occupiedCells.contains(move.getPoint2())) {
            return false;
        } else {
            if (checkIfNeiNeighbours(move.getPoint1(), move.getPoint2())) {
                updateCells(move.getPoint1(), move.getPoint2());
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean cellOccupied(int x, int y) {
        if (board[x][y] == false) {
            return false;
        } else {
            return true;
        }
    }

    public boolean cellOccupied(Point point) {
        if (board[point.getX()][point.getY()] == false) {
            return false;
        } else {
            return true;
        }
    }

    public List<Point> getFreeCells() {
        return freeCells;
    }

    public List<Point> getOccupiedCells() {
        return occupiedCells;
    }

    private void updateCells(Point... points) {
        for (Point point: points) {
            board[point.getX()][point.getY()] = true;
            freeCells.remove(point);
            occupiedCells.add(point);
        }
    }

    public boolean checkIfEndGame() {
        if (freeCells == null) {
            return true;
        } else {
            for (Point point: freeCells) {
                if( checkCellNeighboursIfOccupied(point.getX(), point.getY()) == false) {
                    return false;
                }
            }
            return true;
        }
    }

    public String getFreeCellsAsString() {
        List<Point> freeCells = getFreeCells();
        StringBuilder stringBuilder = new StringBuilder();
        for (Point point : freeCells) {
            stringBuilder.append("{").append(point.getX()).append(";").append(point.getY()).append("}").append(",");
        }
        stringBuilder.delete(stringBuilder.length() - 1,  stringBuilder.length() - 1);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public void setFullBoard() {

        occupiedCells = new ArrayList<>();
        freeCells = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = true;
                occupiedCells.add(new Point(i,j));
            }
        }
    }

}
