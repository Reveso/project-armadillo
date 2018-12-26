package com.lukasrosz.armadillo.replay;

import com.lukasrosz.armadillo.game.Move;
import com.lukasrosz.armadillo.player.AbstractPlayer;
import com.lukasrosz.armadillo.player.PlayerDetails;
import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReplayMove {

    private PlayerDetails movingPlayer;
    private Move move;
}
