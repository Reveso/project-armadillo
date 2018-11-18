package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.val;

import java.util.List;
import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer {
    private static int count = 1;
    public HumanPlayer() {
        playerDetails = new PlayerDetails("Hooman" + count++, "Homo", "Sapiens", "");
    }

    @Override
    public MoveResponse askForMove(String freeCells) {
        System.out.println(freeCells);
        System.out.println("Make a move");
        val moveResponse = new MoveResponse();
        val scanner = new Scanner(System.in);

        Move move = null;
        boolean exit = false;
        do {
            String stringMove = scanner.nextLine();
            List<Point> points = Mapper.getStringAsPoints(stringMove);
            if(points != null && points.size() == 2) {
                move = new Move(points.get(0), points.get(1)); //TODO this should be logged
                exit = true;
            }

        } while(!exit);
        scanner.close();

        moveResponse.setResponseType(ResponseType.NORMAL);
        moveResponse.setMove(move);
        return moveResponse;
    }

}
