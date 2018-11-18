package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.val;

import java.io.*;
import java.util.concurrent.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AIPlayer extends AbstractPlayer {

    private ProcessBuilder processBuilder;
    private Process process;
    private boolean activated = false;

    public AIPlayer(@NonNull File dir, @NonNull PlayerDetails playerDetails) {
        this.playerDetails = playerDetails;
        this.processBuilder = new ProcessBuilder(playerDetails.getAlias());
        processBuilder.directory(dir);
    }

    private boolean activatePlayer() {
        try {
            process = processBuilder.start();
            activated = true;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public MoveResponse askForMove(String freeCells) {
        val moveResponse = new MoveResponse();
        if(!activated) if(!activatePlayer()) {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return moveResponse;
        }
        if(!sendMessageToProcess(freeCells)) {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return moveResponse;
        }
        if(getMessageFromProcess(moveResponse) == null) {
            return moveResponse;
        }

        //TODO Some String - Move Converter, EXCEPTION response if parse fails
        //TODO and moveResponse.setMove(move) also here
        moveResponse.setResponseType(ResponseType.NORMAL);
        return moveResponse;
    }

    private String getMessageFromProcess(MoveResponse moveResponse) {
        String move = null;
        try(val reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            ExecutorService executor = Executors.newCachedThreadPool();
            Future<String> futureCall = executor.submit(new CallableReader(reader));

            try {
                move = futureCall.get(500, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e){
                e.printStackTrace();
                System.out.println("Too much time for a response"); //TODO This should be logged
                process.destroy();
                moveResponse.setResponseType(ResponseType.TIMEOUT);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                process.destroy();
                moveResponse.setResponseType(ResponseType.EXCEPTION);
            }
        } catch (IOException e) {
            e.printStackTrace();
            moveResponse.setResponseType(ResponseType.EXCEPTION);
        }
        return move;
    }

    private boolean sendMessageToProcess(String freeCells) {
        try(val writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(freeCells);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void killPlayer() {
        process.destroy();
    }
}
