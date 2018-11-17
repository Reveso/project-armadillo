package com.lukasrosz.armadillo.mainpack;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;

import java.util.concurrent.Callable;

@AllArgsConstructor
public class CallableReader implements Callable<String> {

    private BufferedReader reader;

    @Override
    public String call() throws Exception {
        return reader.readLine();
    }
}
