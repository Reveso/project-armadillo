package com.lukasrosz.armadillo.scoring;

import com.lukasrosz.armadillo.player.PlayerDetails;
import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public class Scoreboard {
    private final Map<PlayerDetails, Score> scoreboardMap;

    public void saveResults(GameResult gameResult) {
        scoreboardMap.get(gameResult.getWinner()).incrementVictories();
        scoreboardMap.get(gameResult.getLoser()).incrementDefeats();
        if(gameResult.isDisqualified()) {
            scoreboardMap.get(gameResult.getLoser()).incrementDisqualifications();
        }
    }

    public Score addPlayer(PlayerDetails playerDetails) {
        return scoreboardMap.put(playerDetails, new Score());
    }

    public Score removePlayer(PlayerDetails playerDetails) {
        return scoreboardMap.remove(playerDetails);
    }

    public void printScoreboard() {
        MapUtil.sortByValue(scoreboardMap);
        for (PlayerDetails pd : scoreboardMap.keySet()) {
            System.out.println(pd + "\t" + scoreboardMap.get(pd).getVictories() + "\t"
                    + scoreboardMap.get(pd).getDefeats() + "\t"
                    + scoreboardMap.get(pd).getDisqualifications());
        }
    }
}
