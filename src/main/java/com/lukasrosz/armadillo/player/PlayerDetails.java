package com.lukasrosz.armadillo.player;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class PlayerDetails implements Comparable<PlayerDetails> {
    @Setter @Getter
    private String alias;
    @Setter @Getter
    private String name;
    @Setter @Getter
    private String surname;
    @Setter @Getter
    private String cmd;

    @Override
    public int compareTo(PlayerDetails o) {
        if(o.getAlias().compareTo(this.alias) == 0) {
            if(o.getName().compareTo(this.getName()) == 0) {
                return o.getSurname().compareTo(this.getSurname());
            } else return o.getName().compareTo(this.getName());
        } else return o.getAlias().compareTo(this.getAlias());
    }

}
