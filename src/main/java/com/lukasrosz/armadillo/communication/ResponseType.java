package com.lukasrosz.armadillo.communication;

public enum ResponseType {
    NORMAL, INITIALIZATION_EXCEPTION,
    EXCEPTION, RESPONSE_TIMEOUT, INVALID_MOVE_PROTOCOL,
    INVALID_MOVE, NULL_MOVE
}
