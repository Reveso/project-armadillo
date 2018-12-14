package com.lukasrosz.armadillo.communication;

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
