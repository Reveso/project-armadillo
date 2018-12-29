package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.GameResponse;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import lombok.val;

import java.io.File;

public class RawBattleMain {

    public static void main(String[] args) throws Exception {
        val gameMaker = new GameMaker();
        GameConfigDto gameConfigDto = gameMaker.newTournament(new File("ai_test_directory"), 45);

        for (Game game : gameConfigDto.getGames()) {
            System.out.println(game);
            while (!game.isEnded()) {
                try {
                    GameResponse move = game.nextMove();
                    System.out.println(move.getMove());
                } catch (Exception e) {
                    System.out.println(e);
                }
//                Thread.sleep(1000);
            }
            System.out.println(game.isEnded());
            System.out.println("=============================================================");
        }
    }
}
