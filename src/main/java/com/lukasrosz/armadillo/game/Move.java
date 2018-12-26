package com.lukasrosz.armadillo.game;

import lombok.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Move {

    private Point point1;
    private Point point2;
}
