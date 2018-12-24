package com.lukasrosz.armadillo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameResponse {
    private Move move;
    private double occupiedFields;
}
