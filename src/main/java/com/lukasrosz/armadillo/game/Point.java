package com.lukasrosz.armadillo.game;

import java.util.Random;

public class Point {
    private int x;
    private int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point generate(int min, int max) {
        Random random = new Random();
        return new Point(random.nextInt(max - min + 1) + min, random.nextInt(max - min + 1) + min);
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
}
