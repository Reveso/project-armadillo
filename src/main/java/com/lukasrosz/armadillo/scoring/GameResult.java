package com.lukasrosz.armadillo.scoring;

import com.lukasrosz.armadillo.communication.ResponseType;
import com.lukasrosz.armadillo.player.PlayerDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameResult {
    private PlayerDetails winner;
    private PlayerDetails loser;
    private boolean disqualified;
    private ResponseType gameFinishType;
}
