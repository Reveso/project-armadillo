package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.communication.MoveResponse;
import com.lukasrosz.armadillo.communication.ResponseType;
import lombok.NonNull;
import lombok.val;

import java.io.*;
import java.util.concurrent.*;

public class AIPlayer extends AbstractPlayer {

    private ProcessBuilder processBuilder;
    private Process process;
    private boolean activated = false;

    private BufferedReader reader;
    private BufferedWriter writer;

    public AIPlayer(@NonNull File dir, @NonNull PlayerDetails playerDetails) {
        this.playerDetails = playerDetails;
        this.processBuilder = new ProcessBuilder(playerDetails.getAlias());
        processBuilder.directory(dir);
    }

    private boolean activatePlayer() {
        if (activated) return true;
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
        if (!activated) if (!activatePlayer()) {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return moveResponse;
        }
        if (!sendMessageToProcess(freeCells)) {
            moveResponse.setResponseType(ResponseType.EXCEPTION);
            return moveResponse;
        }
        if (getMessageFromProcess(moveResponse) == null) {
            return moveResponse;
        }

        //TODO Some String - Move Converter, EXCEPTION response if parse fails
        //TODO and moveResponse.setMove(move) also here
        moveResponse.setResponseType(ResponseType.NORMAL);
        return moveResponse;
    }

    private String getMessageFromProcess(MoveResponse moveResponse) {
        String move = null;

        ExecutorService executor = Executors.newCachedThreadPool();
        Future<String> futureCall = executor.submit(new CallableReader(process));

        try {
            move = futureCall.get(500, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            System.out.println("Too much time for a response"); //TODO This should be logged
            killPlayer();
            moveResponse.setResponseType(ResponseType.TIMEOUT);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            killPlayer();
            moveResponse.setResponseType(ResponseType.EXCEPTION);
        } finally {
            executor.shutdownNow();
        }
        return move;
    }

    private boolean sendMessageToProcess(String freeCells) {
        try {
            val writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write(freeCells + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void killPlayer() {
        if (process.isAlive()) {
            process.destroy();
            activated = false;
            try {
                reader.close();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                reader = null;
                writer = null;
            }
        }
    }
}
