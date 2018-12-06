package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

import java.util.List;
import java.util.Scanner;

public class HumanFXPlayer extends HumanPlayer {
    @Getter
    @Setter
    private Move nextMove;

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();

        moveResponse.setResponseType(ResponseType.NORMAL);
        moveResponse.setMove(nextMove);
        return moveResponse;
    }

}
