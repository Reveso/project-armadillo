package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.NonNull;
import lombok.val;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

public class AIPlayer extends AbstractPlayer {

    private ProcessCommunicator processCommunicator;

    public AIPlayer(@NonNull File dir, @NonNull PlayerDetails playerDetails) {
        this.processCommunicator = new ProcessCommunicator(dir,
                new String[] {playerDetails.getCmd()});
    }

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();
        moveResponse.setResponseType(ResponseType.NORMAL);
        if (!processCommunicator.isActivated()) {
            if (!processCommunicator.startProcess()) {
                moveResponse.setResponseType(ResponseType.EXCEPTION);
                return moveResponse;
            }
        }

        String stringMove = getMoveFromProcessCommunicator(moveResponse, freeCells);
        if(stringMove == null) return moveResponse;

        Move move = resolveMove(moveResponse,stringMove);
        if(move == null) return moveResponse;

        moveResponse.setMove(move);
        moveResponse.setResponseType(ResponseType.NORMAL);
        return moveResponse;
    }

    private Move resolveMove(MoveResponse moveResponse, String stringMove) {
        Move move = null;
        List<Point> points = Mapper.getStringAsPoints(stringMove);
        if(points != null && points.size() == 2) {
            move = new Move(points.get(0), points.get(1)); //TODO this should be logged
        } else {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return null;
        }
        return move;
    }

    private String getMoveFromProcessCommunicator(MoveResponse moveResponse, String freeCells) {
        String stringMove = null;
        try {
            processCommunicator.sendMessageToProcess(freeCells);
            stringMove = processCommunicator.getMessageFromProcess();
            if(stringMove == null) {
                moveResponse.setResponseType(ResponseType.EXCEPTION);
                return null;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            moveResponse.setResponseType(ResponseType.TIMEOUT);
            return null;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return null;
        }
        return stringMove;
    }

    @Override
    public void killPlayer() {
        processCommunicator.killProcess();
    }
}
