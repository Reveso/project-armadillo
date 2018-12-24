package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.PointsMapper;
import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import com.lukasrosz.armadillo.communication.model.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import lombok.NonNull;
import lombok.val;

import java.io.*;
import java.util.concurrent.*;

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
        Move move = PointsMapper.getStringAsMove(stringMove);
        if(move == null) {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
        }
        return move;
    }

    private String getMoveFromProcessCommunicator(MoveResponse moveResponse, String freeCells) {
        String stringMove;
        try {
            processCommunicator.sendMessageToProcess(freeCells);
            stringMove = processCommunicator.getMessageFromProcess(500);
            System.out.println("STRING MOVE");
            System.out.println(stringMove);
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
            String response = processCommunicator.getMessageFromProcess(2000); //initial timeout longer
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
