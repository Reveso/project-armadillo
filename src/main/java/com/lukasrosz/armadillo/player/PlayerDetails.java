package com.lukasrosz.armadillo.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PlayerDetails {
    @Setter @Getter
    private String alias;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private String surname;
    @Setter @Getter
    private String cmd;
}
