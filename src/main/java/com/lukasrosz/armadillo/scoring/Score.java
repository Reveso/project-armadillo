package com.lukasrosz.armadillo.scoring;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class Score implements Comparable<Score>{
    @Getter @Setter
    private int victories;
    @Getter @Setter
    private int defeats;
    @Getter @Setter
    private int disqualifications;

    public void incrementVictories() {
        this.victories = victories++;
    }

    public void incrementDefeats() {
        this.defeats = defeats++;
    }

    public void incrementDisqualifications() {
        this.defeats = defeats++;
    }

    @Override
    public int compareTo(Score o) {
        if(o.getVictories() - this.getVictories() == 0) {
            if(o.getDefeats() - this.getDefeats() == 0) {
                return o.getDisqualifications() - this.getDisqualifications();
            } return o.getDefeats() - this.getDefeats();
        } return o.getVictories() - this.getVictories();
    }
}
