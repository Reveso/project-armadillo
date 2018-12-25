package com.lukasrosz.armadillo.replay;

import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.GameFinishType;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class GameReplay {
    private int moveCounter = 0;

    private int boardSize;
    private List<Point> initiallyOccupiedFields = new ArrayList<>();
    private Map<Integer, ReplayMove> moveMap = new HashMap<>();

    private GameResult gameResult;

    public void addNewMove(ReplayMove replayMove) {
        moveMap.put(moveCounter++, replayMove);
    }

    public boolean containsPlayer(PlayerDetails playerDetails) {
        return gameResult.getLoser().equals(playerDetails)
                || gameResult.getWinner().equals(playerDetails);
    }

    public static GameReplay newReplayFromGame(Game game) {
        GameReplay gameReplay = new GameReplay();
        gameReplay.setBoardSize(game.getBoard().getSize());
//        gameReplay.setPlayer1(game.getMovingPlayer().getPlayerDetails());
//        gameReplay.setPlayer2(game.getWaitingPlayer().getPlayerDetails());
        gameReplay.getInitiallyOccupiedFields().addAll(game.getBoard().getOccupiedFields());

        return gameReplay;
    }

    public String getWinner() {
        return gameResult.getWinner().getAlias() + " ("
                + gameResult.getWinner().getSurname() + ")";
    }

    public String getLoser() {
        return gameResult.getLoser().getAlias() + " ("
                + gameResult.getLoser().getSurname() + ")";
    }

    public String getFinishType() {
        return gameResult.getGameFinishType().toString();
    }

    public boolean getDisqualification() {
        return gameResult.isDisqualified();
    }
}
