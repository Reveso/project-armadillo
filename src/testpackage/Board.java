package testpackage;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private boolean[][] board;
    private int size;
    private List<Point> freeCells;
    private List<Point> occupiedCells;
    public void init() {
        for(int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                freeCells.add(new Point(i,j));
            }
        }
    }



    public Board(int size) {
        this.size = size;
        board = new boolean[size][size];
        freeCells = new ArrayList<>();
        init();
    }

    public boolean setNewMove(Move move) {
        if (occupiedCells.contains(move.getPoint1()) || occupiedCells.contains(move.getPoint2())) {
            return false;
        } else {
            updateCells(move.getPoint1(), move.getPoint2());
            return true;
        }
    }

    public boolean cellOccupied(int x, int y) {
        if (board[x][y] == false) {
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
        for (int i = 0; i < freeCells.size() - 1; i++) {
            for ( int j = i + 1; j < freeCells.size(); j++) {
                if (Math.abs(freeCells.get(i).getX() - freeCells.get(j).getX()) <= 1
                || Math.abs(freeCells.get(i).getY() - freeCells.get(j).getY()) <= 1) {
                    return true;
                }
            }
        } return false;
    }

    public String getFreeCellsAsString(){
        //TODO
        return null;
    }

}
