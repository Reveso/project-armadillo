package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.model.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import lombok.Getter;
import lombok.Setter;
import lombok.val;


@Getter
@Setter
public class HumanFXPlayer extends HumanPlayer {

    private Move nextMove;

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();

        moveResponse.setResponseType(ResponseType.NORMAL);
        moveResponse.setMove(nextMove);
        return moveResponse;
    }

}
