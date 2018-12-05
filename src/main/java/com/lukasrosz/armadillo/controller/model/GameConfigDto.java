package com.lukasrosz.armadillo.controller.model;

import com.lukasrosz.armadillo.game.Game;
import com.lukasrosz.armadillo.scoring.Score;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameConfigDto {
    private Set<Score> scores = new LinkedHashSet<>();
    private Set<Game> games = new LinkedHashSet<>();
}
