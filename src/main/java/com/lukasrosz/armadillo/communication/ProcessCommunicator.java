package com.lukasrosz.armadillo.communication;

import lombok.Getter;
import lombok.NonNull;

import java.io.*;
import java.util.concurrent.*;


public class ProcessCommunicator {

    private Process process;
    private ProcessBuilder processBuilder;
    private BufferedReader reader;
    private BufferedWriter writer;
    private ExecutorService executor;
    @Getter private boolean activated = false;

    public ProcessCommunicator(@NonNull File dir, @NonNull String[] command) {
        processBuilder = new ProcessBuilder(command);
        processBuilder.directory(dir);
    }

    public boolean startProcess() {
        if(activated) return false;
        try {
            process = processBuilder.start();
            activated = true;
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getMessageFromProcess() throws TimeoutException, InterruptedException, ExecutionException {
        String move = null;

        executor = Executors.newCachedThreadPool();

//        Future<String> futureCall = executor.submit(new CallableReader(reader));
        Future<String> futureCall = executor.submit(() -> reader.readLine());

        move = futureCall.get(500, TimeUnit.MILLISECONDS);
        executor.shutdownNow();

        return move;
    }

    public void sendMessageToProcess(String msg) throws IOException {
        writer.write(msg + "\n");
        writer.flush();
    }

    public void killProcess() {
        if(process != null) process.destroy();
        if(process != null) executor.shutdownNow();
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            activated = false;
        }
    }
}
