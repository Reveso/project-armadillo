package com.lukasrosz.armadillo.gamemaker;

import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.player.AIPlayer;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Scoreboard;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@NoArgsConstructor()
public class GameMaker {

    public Scoreboard newHumanVsAIGame(File aiDir, int boardSize) {
        val player1 = new AIPlayer(aiDir, populatePlayerDetails(aiDir));
        val player2 = new HumanPlayer();

        val scoreboard = new Scoreboard(new HashMap<>());
        val Game = new Game(player1, player2, new Board(boardSize));
        GameResult result = Game.playGame();

        scoreboard.saveResults(result);
        return scoreboard;
    }

    public Scoreboard newAiVsAiGame(File aiDir1, File aiDir2, int boardSize){
        val player1 = new AIPlayer(aiDir1, populatePlayerDetails(aiDir1));
        val player2 = new AIPlayer(aiDir2, populatePlayerDetails(aiDir2));

        val scoreboard = new Scoreboard(new HashMap<>());
        val Game = new Game(player1, player2, new Board(boardSize));
        GameResult result = Game.playGame();

        scoreboard.saveResults(result);
        return scoreboard;
    }

    public Scoreboard newBattleGame(File mainDir, int boardSize) {
        Set<AbstractPlayer> players = populatePlayersSet(mainDir);

        val scoreboard = new Scoreboard(new HashMap<>());
        for(AbstractPlayer player1 : players) {
            for(AbstractPlayer player2 : players) {
                if(player1.equals(player2)) continue;
                val Game = new Game(player1, player2, new Board(boardSize));
                GameResult result = Game.playGame();
                scoreboard.saveResults(result);
            }
        }
        return scoreboard;
    }

    private Set<AbstractPlayer> populatePlayersSet(File mainDir) {
        final Set<AbstractPlayer> players = new TreeSet<>();
        for(String relativeDirName : mainDir.list()) {
            File dir = new File(mainDir.getAbsolutePath() + "/" + relativeDirName);
            val playerDetails = populatePlayerDetails(dir);
            if(playerDetails == null) continue;
            players.add(new AIPlayer(dir, playerDetails));
        }
        return players;
    }

    private PlayerDetails populatePlayerDetails(File dir) {
        val playerDetails = new PlayerDetails();
        try (val scanner = new Scanner(new File(dir.getAbsolutePath()))) {
            playerDetails.setAlias(scanner.nextLine());
            playerDetails.setName(scanner.next());
            playerDetails.setSurname(scanner.next());
            playerDetails.setCmd(scanner.nextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return playerDetails;
    }
}
