package com.lukasrosz.armadillo.game;

import java.util.Random;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point generatePoint(int minIndex, int maxIndex) {
        Random random = new Random();
        return new Point(random.nextInt(maxIndex - minIndex + 1) + minIndex, random.nextInt(maxIndex - minIndex + 1) + minIndex);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
