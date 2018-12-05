package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.communication.ProcessCommunicator;
import lombok.val;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public class CommunicationTest {

    @Test(expected = TimeoutException.class)
    public void processCommunicationTimeout() throws Exception {
        File dir = new File("messenger");
        String[] command = {"java", "-jar", "messenger.jar"};

        val processCommunicator = new ProcessCommunicator(dir, command);

        try {
        processCommunicator.sendMessageToProcess("timeout");
        processCommunicator.getMessageFromProcess(500);
        } finally {
            System.out.println(":v");
            processCommunicator.killProcess();
        }
    }

    @Test
    public void processCommunicationNormal() throws Exception {
        File dir = new File("messenger");
        String[] command = {"java", "-jar", "messenger.jar"};

        val processCommunicator = new ProcessCommunicator(dir, command);
        String result = "";
        String message = "move (1;0),(2;0)";

        try {
            processCommunicator.sendMessageToProcess(message);
            result = processCommunicator.getMessageFromProcess(500);

            Assert.assertEquals(message.split(" ")[1]
                    .replaceAll("[(]", "{")
                    .replaceAll("[)]", "}"), result);
        } finally {
            processCommunicator.killProcess();
        }
    }
}
