package com.lukasrosz.armadillo.game;

import lombok.*;

import java.util.Random;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Point {

    private int x;
    private int y;

    public static Point generatePoint(int minIndex, int maxIndex) {
        Random random = new Random();
        return new Point(random.nextInt(maxIndex - minIndex + 1) + minIndex, random.nextInt(maxIndex - minIndex + 1) + minIndex);
    }
}
