package com.lukasrosz.armadillo.communication.model;

import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.game.Move;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MoveResponse {
    private Move move;
    private ResponseType responseType;
}
