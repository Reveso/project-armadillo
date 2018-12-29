package com.lukasrosz.armadillo.gamemaker;

import com.lukasrosz.armadillo.controller.model.GameConfigDto;
import com.lukasrosz.armadillo.game.Board;
import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.player.*;
import com.lukasrosz.armadillo.scoring.Score;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import java.io.File;
import java.io.IOException;
import java.util.*;

@NoArgsConstructor()
public class GameMaker {

    public GameConfigDto newTournament(@NonNull File mainDir, int boardSize) {
        Set<AbstractPlayer> players = populatePlayersSet(mainDir);
        Set<Game> games = new LinkedHashSet<>();
        Set<Score> scores = new LinkedHashSet<>();

        for (AbstractPlayer player1 : players) {
            for (AbstractPlayer player2 : players) {
                if (player1.equals(player2)) continue;
                val game = new Game(player1, player2, new Board(boardSize));
                games.add(game);
            }
            scores.add(new Score(player1.getPlayerDetails()));
        }
        return new GameConfigDto(boardSize, scores, games);
    }

    public GameConfigDto newSoloGame(File playerDir1, File playerDir2, int boardSize) {
        try {
            AbstractPlayer player1 = playerDir1 == null ? new HumanFXPlayer() :
                    new AIPlayer(playerDir1, populatePlayerDetails(playerDir1));
            AbstractPlayer player2 = playerDir2 == null ? new HumanFXPlayer() :
                    new AIPlayer(playerDir2, populatePlayerDetails(playerDir2));
            val game = new Game(player1, player2, new Board(boardSize));
            return singleGameConfig(player1, player2, game, boardSize);
        } catch (Exception e) {
            System.err.println(e);
            return new GameConfigDto();
        }
    }

    private Set<AbstractPlayer> populatePlayersSet(@NonNull File mainDir) {
        final Set<AbstractPlayer> players = new LinkedHashSet<>();
        for (String relativeDirName : mainDir.list()) {
            File dir = new File(mainDir.getAbsolutePath() + "/" + relativeDirName);
            val playerDetails = populatePlayerDetails(dir);
            if (playerDetails == null) continue;
            players.add(new AIPlayer(dir, playerDetails));
        }
        return players;
    }

    private PlayerDetails populatePlayerDetails(@NonNull File dir) {
        val playerDetails = new PlayerDetails();
        try (val scanner = new Scanner(new File(dir.getAbsolutePath() + "/info.txt"))) {
            playerDetails.setAlias(scanner.nextLine());
            val fullName = scanner.nextLine();
            playerDetails.setName(fullName.split(" ")[0]);

            val surname = new StringBuilder(" ");
            Arrays.stream(fullName.split(" ")).skip(1)
                    .forEach(s -> surname.append(s + " "));
            playerDetails.setSurname(surname.toString().trim());

            playerDetails.setCmd(scanner.nextLine());
            System.out.println(playerDetails.getCmd());

            replacePlayerDetailsSemicolons(playerDetails);
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
        return playerDetails;
    }

    private void replacePlayerDetailsSemicolons(PlayerDetails playerDetails) {
        playerDetails.setAlias(replaceSemicolons(playerDetails.getAlias()));
        playerDetails.setName(replaceSemicolons(playerDetails.getName()));
        playerDetails.setSurname(replaceSemicolons(playerDetails.getSurname()));
    }

    private String replaceSemicolons(String string) {
        return string.replaceAll(";", ".");
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
