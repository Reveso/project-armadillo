package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import lombok.val;

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
        File dir = new File("messenger");
        String[] command = {"java", "-jar", "messenger.jar"};

        val processCommunicator = new ProcessCommunicator(dir, command);
        processCommunicator.startProcess();

        try {
            System.out.println(processCommunicator.getMessageFromProcess());
            processCommunicator.sendMessageToProcess("kek kek kek");
            System.out.println(processCommunicator.getMessageFromProcess());
            processCommunicator.sendMessageToProcess("Lukasz ma racje");
            System.out.println(processCommunicator.getMessageFromProcess());

        } catch (TimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            processCommunicator.killProcess();
        }
    }
}
