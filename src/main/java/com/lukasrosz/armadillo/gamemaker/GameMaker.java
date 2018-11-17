package com.lukasrosz.armadillo.gamemaker;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.player.AIPlayer;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanPlayer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.File;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GameMaker {

    public static void newHumanVsAIGame(File aiDir, String cmd, int boardSize) {
        AbstractPlayer player1 = new AIPlayer(aiDir, cmd);
        AbstractPlayer player2 = new HumanPlayer();

        val Game = new Game(player1, player1, new Board(boardSize));
        String result = Game.playGame(); //TODO Some score representation logic (interface scorable or sth, dunno)
    }

    public static void newAiVsAiGame(File aiDir1, String cmd1, File aiDir2, String cmd2, int boardSize){
        AbstractPlayer player1 = new AIPlayer(aiDir1, cmd1);
        AbstractPlayer player2 = new AIPlayer(aiDir2, cmd2);

        val Game = new Game(player1, player1, new Board(boardSize));
        String result = Game.playGame(); //TODO Some score representation logic (interface scorable or sth, dunno)
    }

    public static void newBattleGame(File mainDir) {

    }


}
