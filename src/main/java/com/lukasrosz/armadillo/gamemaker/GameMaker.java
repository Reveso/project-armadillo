package com.lukasrosz.armadillo.gamemaker;

import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.player.AIPlayer;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.HumanPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.GameResult;
import com.lukasrosz.armadillo.scoring.Score;
import com.lukasrosz.armadillo.scoring.Scoreboard;
import lombok.NoArgsConstructor;
import lombok.val;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@NoArgsConstructor()
public class GameMaker {

    public GameConfigDto newHumanVsAIGame(File aiDir, int boardSize) {
        val player1 = new AIPlayer(aiDir, populatePlayerDetails(aiDir));
        val player2 = new HumanPlayer();
        val game = new Game(player1, player2, new Board(boardSize));
        return singleGameConfig(player1, player2, game, boardSize);
    }

    public GameConfigDto newAiVsAiGame(File aiDir1, File aiDir2, int boardSize){
        val player1 = new AIPlayer(aiDir1, populatePlayerDetails(aiDir1));
        val player2 = new AIPlayer(aiDir2, populatePlayerDetails(aiDir2));
        val game = new Game(player1, player2, new Board(boardSize));
        return singleGameConfig(player1, player2, game, boardSize);
    }

    public GameConfigDto newBattleGame(File mainDir, int boardSize) {
        Set<AbstractPlayer> players = populatePlayersSet(mainDir);
        Set<Game> games = new LinkedHashSet<>();
        Set<Score> scores = new LinkedHashSet<>();

        //TODO all players now will have 2 games with each other, we want to avoid that
        for(AbstractPlayer player1 : players) {
            for(AbstractPlayer player2 : players) {
                if(player1.equals(player2)) continue;
                val game = new Game(player1, player2, new Board(boardSize));
                games.add(game);
            }
            scores.add(new Score(player1.getPlayerDetails()));
        }
        return new GameConfigDto(boardSize, scores, games);
    }

    private Set<AbstractPlayer> populatePlayersSet(File mainDir) {
        final Set<AbstractPlayer> players = new LinkedHashSet<>();
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
        try (val scanner = new Scanner(new File(dir.getAbsolutePath() + "/info.txt"))) {
            playerDetails.setAlias(scanner.nextLine());
            val fullName = scanner.nextLine();
            playerDetails.setName(fullName.split(" ")[0]);
            playerDetails.setSurname(fullName.split(" ")[1]);
            playerDetails.setCmd(scanner.nextLine());
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
        return playerDetails;
    }

    private GameConfigDto singleGameConfig(AbstractPlayer player1, AbstractPlayer player2,
                                           Game game, int boardSize) {
        val gameConfigDto = new GameConfigDto();
        gameConfigDto.getGames().add(game);
        gameConfigDto.getScores().add(new Score(player1.getPlayerDetails()));
        gameConfigDto.getScores().add(new Score(player2.getPlayerDetails()));
        gameConfigDto.setBoardSize(boardSize);
        return gameConfigDto;
    }
}
