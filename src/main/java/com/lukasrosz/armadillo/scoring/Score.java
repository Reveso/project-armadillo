package com.lukasrosz.armadillo.scoring;

import com.lukasrosz.armadillo.player.PlayerDetails;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Score implements Comparable<Score>{
    private PlayerDetails playerDetails;
    private int victories = 0;
    private int defeats = 0;
    private int disqualifications = 0;

    public Score(PlayerDetails playerDetails) {
        this.playerDetails = playerDetails;
    }

    public void incrementVictories() {
        victories++;
    }

    public void incrementDefeats() {
        defeats++;
    }

    public void incrementDisqualifications() {
        disqualifications++;
    }

    public String getAlias() {
        return playerDetails.getAlias();
    }

    public String getSurname() {
        return playerDetails.getSurname();
    }

    public String getName() {
        return playerDetails.getName();
    }

    public void reset() {
        setDefeats(0);
        setDisqualifications(0);
        setVictories(0);
    }

    @Override
    public int compareTo(Score o) {
        if(this.getPlayerDetails().equals(o.getPlayerDetails())) {
            return 0;
        }

        if(this.getVictories() - o.getVictories() == 0) {
            if(o.getDefeats() - this.getDefeats() == 0) {
                return this.getDisqualifications() - o.getDisqualifications();
            } return this.getDefeats() - o.getDefeats();
        } return o.getVictories() - this.getVictories();
    }
}
