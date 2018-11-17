package com.lukasrosz.armadillo;


import com.lukasrosz.armadillo.gamemaker.GameMaker;
import com.lukasrosz.armadillo.player.AIPlayer;
import com.lukasrosz.armadillo.player.AbstractPlayer;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        //TODO Get user input on what game does he want
        //TODO Create came by calling GameMaker.whatGamePlayerWantsStaticMethod()
        System.out.println(System.getProperty("user.dir"));
        GameMaker.newBattleGame(new File(System.getProperty("user.dir")), 16);
    }

//    private void processCommunicationTestMethod() {
//        THIS IS USELESS, JUST FOR TESTS
//        DON'T DELETE
//        String[] command = {"java", "-jar", "messenger.jar"};
//        ProcessBuilder processBuilder = new ProcessBuilder(command);
//        processBuilder.directory(new File("messenger"));
//
//
//        try {
//            Process process = processBuilder.start();
//
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
//            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            System.out.println(reader.readLine());
//
//            writer.write("kek kek kek");
//            writer.flush();
//            writer.close();
//
//            String readline = "";
//            ExecutorService executor = Executors.newCachedThreadPool();
//            Future<String> futureCall = executor.submit(new CallableReader(reader));
//            try {
//                readline = futureCall.get(500, TimeUnit.MILLISECONDS);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            executor.shutdownNow();
//
//            System.out.println("check");
//            System.out.println(readline);
//            process.destroy();
////            reader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("End");
//    }
}
