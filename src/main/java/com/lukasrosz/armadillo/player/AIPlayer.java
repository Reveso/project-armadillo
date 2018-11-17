package com.lukasrosz.armadillo.player;

import com.lukasrosz.armadillo.game.Move;
import lombok.val;

import java.io.*;
import java.util.concurrent.*;


public class AIPlayer extends AbstractPlayer {

    private Process process;

    public AIPlayer(File dir, String command) {
        val processBuilder = new ProcessBuilder(command);
        processBuilder.directory(dir);
        try {
            Process process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Move askForMove(String freeCells) {
        if(!sendMessageToProcess(freeCells)) return null;
        if(getMessageFromProcess() == null) return null;

        //TODO Some String - Move Converter
        return new Move();
    }

    private String getMessageFromProcess() {
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
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                process.destroy();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
