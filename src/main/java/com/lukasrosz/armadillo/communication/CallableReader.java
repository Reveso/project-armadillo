package com.lukasrosz.armadillo.communication;

import lombok.AllArgsConstructor;
import lombok.val;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.util.concurrent.Callable;

@AllArgsConstructor
@Deprecated
public class CallableReader implements Callable<String> {

    private BufferedReader reader;

    @Override
    public String call() throws Exception {
        return reader.readLine();
    }
}
