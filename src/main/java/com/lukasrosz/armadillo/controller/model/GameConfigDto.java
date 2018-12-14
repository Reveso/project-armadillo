package com.lukasrosz.armadillo.controller.model;

import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.scoring.Score;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameConfigDto {

    public GameConfigDto(int boardSize, Set<Score> scores, Set<Game> games) {
        this.scores = scores;
        this.games = games;
        this.boardSize = boardSize;
    }

    private Set<Score> scores = new LinkedHashSet<>();
    private Set<Game> games = new LinkedHashSet<>();
    private int refreshDelay;
    private int boardSize;

}
