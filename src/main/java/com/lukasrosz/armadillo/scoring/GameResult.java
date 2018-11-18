package com.lukasrosz.armadillo.scoring;

import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
public class GameResult {
    @Getter @Setter private PlayerDetails winner;
    @Getter @Setter private PlayerDetails loser;
    @Getter @Setter private boolean disqualified;
}
