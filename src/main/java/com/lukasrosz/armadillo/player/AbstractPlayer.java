package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.model.MoveResponse;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public abstract class AbstractPlayer {

    protected PlayerDetails playerDetails;

    public abstract MoveResponse askForMove(String freeCells);

    public void killPlayer() {
        System.out.println(playerDetails.getAlias() + " killed");
    }

    public boolean initialize(int boardSize, String occupiedCells) {
        return true;
    }
}
