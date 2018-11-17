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
        String move = scanner.nextLine();
        scanner.close();
        //TODO Some String - Move Converter
        //TODO As this is a human player, we can give
        //TODO him infinitive or so chances to write a correct move
        return new Move();
    }

    @Override
    public void killPlayer() {
        return;
    }
}
