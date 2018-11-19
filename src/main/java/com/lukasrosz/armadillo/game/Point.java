package com.lukasrosz.armadillo.game;

import lombok.*;

import java.util.Random;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Point {
    @Getter @Setter
    private int x;
    @Getter @Setter
    private int y;

    public static Point generatePoint(int minIndex, int maxIndex) {
        Random random = new Random();
        return new Point(random.nextInt(maxIndex - minIndex + 1) + minIndex, random.nextInt(maxIndex - minIndex + 1) + minIndex);
    }
}
