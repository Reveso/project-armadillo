package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.gamemaker.GameMaker;
import com.lukasrosz.armadillo.player.CallableReader;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import lombok.val;
import sun.misc.IOUtils;

import java.io.*;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        //TODO Get user input on what game does he want
        //TODO Create came by calling GameMaker.whatGamePlayerWantsStaticMethod()
//        System.out.println(System.getProperty("user.dir"));
//        val gameMaker = new GameMaker();
//        gameMaker.newBattleGame(new File(System.getProperty("user.dir")), 16);
        processCommunicationTestMethod();
    }

    private static void processCommunicationTestMethod() {
        //THIS IS USELESS, JUST FOR TESTS
        //DON'T DELETE
        String[] command = {"java", "-jar", "messenger.jar"};
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.directory(new File("messenger"));

        Process process = null;
        try {
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            System.out.println(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }

        writeToProcess(process, "kek");
        String readline = readFromProcess(process);
        System.out.println(readline);

        writeToProcess(process, "kek kek kek");
        readline = readFromProcess(process);
        System.out.println(readline);

        System.out.println("check");
        process.destroy();
        System.out.println("End");
    }


    private static String readFromProcess(Process process) {
        String readline = ":c";

        ExecutorService executor = Executors.newCachedThreadPool();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        Future<String> futureCall = executor.submit(new CallableReader(process));

        try {
            readline = futureCall.get(5000, TimeUnit.MILLISECONDS);

        } catch (TimeoutException e) {
            e.printStackTrace();
            executor.shutdownNow();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            executor.shutdownNow();
        }
        executor.shutdownNow();
        return readline;
    }

    private static void writeToProcess(Process process, String msg) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            writer.write(msg + "\n");
            writer.flush();
            System.out.println("sss");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
