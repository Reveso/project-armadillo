package com.lukasrosz.armadillo.communication;

import com.lukasrosz.armadillo.game.Move;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class MoveResponse {
    @Getter @Setter private Move move;
    @Getter @Setter private ResponseType responseType;
}
