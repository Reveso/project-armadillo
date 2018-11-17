package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.mainpack.Move;

import java.io.IOException;

public abstract class AbstractPlayer {

    public abstract Move askForMove(String freeCells);

    public abstract void killPlayer();

}
