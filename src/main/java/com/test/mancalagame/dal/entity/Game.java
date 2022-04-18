package com.test.mancalagame.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.test.mancalagame.exception.ActionNotAllowedException;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
@Data
@NoArgsConstructor
public class Game {
    @Id
    private String id;

    private List<Pit> pits;

    private String playerA;

    private String playerB;

    private String currentPlayer;

    @JsonIgnore
    private int currentPitIndex;

    public Game(String playerA, String playerB){
        this.setPlayerA(playerA);
        this.setPlayerB(playerB);
        this.setPits(initializePits());
    }

    public Pit getPit(Integer pitIndex) {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw new ActionNotAllowedException("Action not allowed. Wrong pit index");
        }
    }

    private List<Pit> initializePits(){
        return Arrays.asList(
                new Pit(Constants.firstPitPlayerA),
                new Pit(Constants.secondPitPlayerA),
                new Pit(Constants.thirdPitPlayerA),
                new Pit(Constants.forthPitPlayerA),
                new Pit(Constants.fifthPitPlayerA),
                new Pit(Constants.sixthPitPlayerA),
                new PitHouse(Constants.rightPitHouseId),
                new Pit(Constants.firstPitPlayerB),
                new Pit(Constants.secondPitPlayerB),
                new Pit(Constants.thirdPitPlayerB),
                new Pit(Constants.forthPitPlayerB),
                new Pit(Constants.fifthPitPlayerB),
                new Pit(Constants.sixthPitPlayerB),
                new PitHouse(Constants.leftPitHouseId));
    }
}
