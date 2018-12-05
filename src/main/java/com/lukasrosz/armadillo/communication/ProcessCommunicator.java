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
    @Getter
    private boolean activated = false;

    public ProcessCommunicator(@NonNull File dir, @NonNull String[] command) {
        processBuilder = new ProcessBuilder(command);
        processBuilder.directory(dir);
    }

    private void startProcess() throws IOException {
        if (activated) return;
        process = processBuilder.start();
        reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        activated = true;
    }

    public String getMessageFromProcess(int timeout) throws TimeoutException, InterruptedException,
            ExecutionException, IOException {
        startProcess();

        String move = null;

        executor = Executors.newCachedThreadPool();

//        Future<String> futureCall = executor.submit(new CallableReader(reader));
        Future<String> futureCall = executor.submit(() -> reader.readLine());

        move = futureCall.get(timeout, TimeUnit.MILLISECONDS);
        executor.shutdownNow();

        return move;
    }

    public void sendMessageToProcess(String msg) throws IOException {
        startProcess();

        msg = msg.contains("\n") ? msg : msg + "\n";
        writer.write(msg);
        writer.flush();
    }

    public void killProcess() {
        if(!activated) return;
        performCleanup();
        activated = false;
    }

    private void performCleanup() {
        if (process != null) process.destroy();
        if (process != null) executor.shutdownNow();
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
