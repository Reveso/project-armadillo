package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import lombok.NonNull;
import lombok.val;

import java.io.*;
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
        if (!processCommunicator.isActivated()) {
            if (!processCommunicator.startProcess()) {
                moveResponse.setResponseType(ResponseType.EXCEPTION);
                return moveResponse;
            }
        }

        try {
            processCommunicator.sendMessageToProcess(freeCells);
            String move = processCommunicator.getMessageFromProcess();
            if(move == null) {
                moveResponse.setResponseType(ResponseType.EXCEPTION);
                return moveResponse;
            }
        } catch (TimeoutException e) {
            e.printStackTrace();
            moveResponse.setResponseType(ResponseType.TIMEOUT);
            return moveResponse;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return moveResponse;
        }

        //TODO Some String - Move Converter, EXCEPTION response if parse fails
        //TODO and moveResponse.setMove(move) also here
        moveResponse.setResponseType(ResponseType.NORMAL);
        return moveResponse;
    }

    @Override
    public void killPlayer() {
        processCommunicator.killProcess();
    }
}
