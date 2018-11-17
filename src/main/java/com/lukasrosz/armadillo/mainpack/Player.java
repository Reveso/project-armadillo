package com.lukasrosz.armadillo.mainpack;

public abstract class Player {

    public abstract Move askForMove(String freeCells);

    public abstract void killPlayer();

}
