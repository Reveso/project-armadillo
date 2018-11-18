package com.lukasrosz.armadillo.game;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Move {
    @Getter @Setter
    private Point point1;
    @Getter @Setter
    private Point point2;
}
