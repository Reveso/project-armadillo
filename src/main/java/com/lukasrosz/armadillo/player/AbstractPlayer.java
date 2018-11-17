package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.game.Move;

public abstract class AbstractPlayer {

    public abstract Move askForMove(String freeCells);

    public abstract void killPlayer();

}
