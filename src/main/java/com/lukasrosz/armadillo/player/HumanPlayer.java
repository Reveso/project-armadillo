package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.game.Move;
import lombok.val;

import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer {

    public HumanPlayer() {
        playerDetails = new PlayerDetails("Hooman", "Homo", "Sapiens", "");
    }

    @Override
    public Move askForMove(String freeCells) {
        System.out.println(freeCells);
        System.out.println("Make a move");
        val scanner = new Scanner(System.in);

        boolean exit = false;
        do {
            String move = scanner.nextLine();
//            if() { //TODO check if move is correct with parser or sth
//                exit = true;
//            }
        } while(!exit);
        scanner.close();

        return new Move();
    }

}
