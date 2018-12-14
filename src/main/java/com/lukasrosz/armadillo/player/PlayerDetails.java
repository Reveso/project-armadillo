package com.lukasrosz.armadillo.player;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Setter
@Getter
public class PlayerDetails implements Comparable<PlayerDetails> {

    private String alias;
    private String name;
    private String surname;
    private String cmd;

    @Override
    public int compareTo(PlayerDetails o) {
        if (o.getAlias().compareTo(this.alias) == 0) {
            if (o.getName().compareTo(this.getName()) == 0) {
                return o.getSurname().compareTo(this.getSurname());
            } else return o.getName().compareTo(this.getName());
        } else return o.getAlias().compareTo(this.getAlias());
    }

}
