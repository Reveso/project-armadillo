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
    private String nextMove;

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();
        Move move = null;

        List<Point> points = Mapper.getStringAsPoints(nextMove);
        if (points != null && points.size() == 2) {
            move = new Move(points.get(0), points.get(1)); //TODO this should be logged
        }

        moveResponse.setResponseType(ResponseType.NORMAL);
        moveResponse.setMove(move);
        return moveResponse;
    }

}
