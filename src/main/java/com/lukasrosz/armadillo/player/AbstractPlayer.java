package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.game.Move;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
public abstract class AbstractPlayer {

    @Getter protected PlayerDetails playerDetails;

    public abstract MoveResponse askForMove(String freeCells);

    public void killPlayer() {
        System.out.println(playerDetails.getAlias() + " killed");
    }


}
