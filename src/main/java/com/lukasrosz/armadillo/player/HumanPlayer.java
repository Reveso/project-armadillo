package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.game.Move;
import lombok.NoArgsConstructor;
import lombok.val;

import java.util.Scanner;

@NoArgsConstructor
public class HumanPlayer extends AbstractPlayer {

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

    @Override
    public void killPlayer() {
    }
}
