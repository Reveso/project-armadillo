package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.model.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import lombok.val;

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

        Move move;
        boolean exit = false;
        do {
            String stringMove = scanner.nextLine();

            move = PointsMapper.getStringAsMove(stringMove);
            if(move != null) {
                exit = true;
            }

        } while(!exit);
        scanner.close();

        moveResponse.setResponseType(ResponseType.NORMAL);
        moveResponse.setMove(move);
        return moveResponse;
    }



}
