package base;

import base.core.Board;
import base.core.Mapper;
import base.core.Move;
import base.core.Point;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader
                (new InputStreamReader(System.in));
        int boardSize = Integer.parseInt(bufferedReader.readLine());
        Board board = new Board(boardSize);
        System.out.println("ok");

        String occupiedCellsString = bufferedReader.readLine();
        board.updateCells(Mapper.getStringAsPoints(occupiedCellsString));
        System.out.println("ok");

        String firstRead = bufferedReader.readLine();
        if(firstRead.equals("start")) {
            Move sendingMove = findFirstAvailableMove(board.getFreeCells(), board);
            System.out.println(Mapper.getMoveAsString(sendingMove));
        } else {
            List<Point> points = Mapper.getStringAsPoints(firstRead);
            Move receivingMove = new Move(points.get(0), points.get(1));
            board.setNewMove(receivingMove);

            Move sendingMove = findFirstAvailableMove(board.getFreeCells(), board);
            System.out.println(Mapper.getMoveAsString(sendingMove));
        }

        while (true) {
            String moveString = bufferedReader.readLine();
            List<Point> points = Mapper.getStringAsPoints(moveString);
            Move receivingMove = new Move(points.get(0), points.get(1));
            board.setNewMove(receivingMove);

            Move sendingMove = findFirstAvailableMove(board.getFreeCells(), board);
            System.out.println(Mapper.getMoveAsString(sendingMove));
        }
    }

    //Extremely inefficient way to look for a possible move, but I cannot be bothered
    public static Move findFirstAvailableMove(List<Point> points, Board board) {
        for(Point p1 : points) {
            for (Point p2 : points) {
                Move move = new Move(p1, p2);
                if(board.setNewMove(move)) {
                    return move;
                }
            }
        }
        return null;
    }
}
