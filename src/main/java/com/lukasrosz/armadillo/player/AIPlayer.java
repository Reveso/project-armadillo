package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.Mapper;
import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.game.Point;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.val;

import java.io.*;
import java.util.List;
import java.util.concurrent.*;

@EqualsAndHashCode
public class AIPlayer extends AbstractPlayer {

    private ProcessCommunicator processCommunicator;

    public AIPlayer(@NonNull File dir, @NonNull PlayerDetails playerDetails) {
        this.processCommunicator = new ProcessCommunicator(dir,
                playerDetails.getCmd().split(" "));
        this.playerDetails = playerDetails;
    }

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();

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
            stringMove = processCommunicator.getMessageFromProcess(500);
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
    public boolean initialize(int boardSize, String occupiedCells) {
        try {
            processCommunicator.sendMessageToProcess(String.valueOf(boardSize));
            String response = processCommunicator.getMessageFromProcess(2000); //TODO initial timeout longer?
            if(!response.toLowerCase().equals("ok")) return false;

            processCommunicator.sendMessageToProcess(occupiedCells);
            response = processCommunicator.getMessageFromProcess(2000);
            if(!response.toLowerCase().equals("ok")) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void killPlayer() {
        processCommunicator.killProcess();
    }
}
