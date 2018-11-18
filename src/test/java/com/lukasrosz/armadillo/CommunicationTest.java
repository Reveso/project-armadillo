package com.lukasrosz.armadillo;

import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import lombok.val;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CommunicationTest {

    @org.junit.Test(expected = TimeoutException.class)
    public void processCommunicationTimeout() throws TimeoutException {
        File dir = new File("messenger");
        String[] command = {"java", "-jar", "messenger.jar"};

        val processCommunicator = new ProcessCommunicator(dir, command);
        processCommunicator.startProcess();
        try {
            processCommunicator.sendMessageToProcess("timeout");
            processCommunicator.getMessageFromProcess();
        } catch (TimeoutException e) {
            throw e;
        } catch (InterruptedException | ExecutionException | IOException e){
            e.printStackTrace();
        } finally {
            processCommunicator.killProcess();
        }
    }

    @org.junit.Test
    public void processCommunicationNormal() {
        File dir = new File("messenger");
        String[] command = {"java", "-jar", "messenger.jar"};

        val processCommunicator = new ProcessCommunicator(dir, command);
        processCommunicator.startProcess();
        String result = "";
        String message = "move (1;0),(2;0)";
        try {
            processCommunicator.sendMessageToProcess(message);
            result = processCommunicator.getMessageFromProcess();
        } catch (TimeoutException | InterruptedException | ExecutionException | IOException e) {
            e.printStackTrace();
        } finally {
            processCommunicator.killProcess();
        }
        Assert.assertEquals(message.split(" ")[1]
                .replaceAll("[(]", "{")
                .replaceAll("[)]", "}"), result);
    }
}
