package com.lukasrosz.armadillo.game;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Move {
    @Getter @Setter
    private Point point1;
    @Getter @Setter
    private Point point2;
}
