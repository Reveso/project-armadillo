package com.lukasrosz.armadillo.replay;

import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.game.Point;
import com.lukasrosz.armadillo.player.PlayerDetails;
import com.lukasrosz.armadillo.scoring.GameResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@XmlRootElement
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
