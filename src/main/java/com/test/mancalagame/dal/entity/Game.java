package com.test.mancalagame.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "games")
@Data
public class Game {
    @Id
    private String id;

    private List<Pit> pits;

    private String playerA;

    private String playerB;

    private String playerForNextMove;

    @JsonIgnore
    private int currentPitIndex;

    public Game(String playerA, String playerB){
        this.setPlayerA(playerA);
        this.setPlayerB(playerB);
        this.setPits(initializePits());
    }

    // returns the corresponding pit of particular index
    public Pit getPit(Integer pitIndex) throws Exception {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw  new Exception("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

    private List<Pit> initializePits(){             //TODO: looks ugly - remove defaultPitStones from Constants, fix Pit creation
        return Arrays.asList(
                new Pit(Constants.firstPitPlayerA, Constants.defaultPitStones),
                new Pit(Constants.secondPitPlayerA, Constants.defaultPitStones),
                new Pit(Constants.thirdPitPlayerA, Constants.defaultPitStones),
                new Pit(Constants.forthPitPlayerA, Constants.defaultPitStones),
                new Pit(Constants.fifthPitPlayerA, Constants.defaultPitStones),
                new Pit(Constants.sixthPitPlayerA, Constants.defaultPitStones),
                new PitHouse(Constants.rightPitHouseId),
                new Pit(Constants.firstPitPlayerB, Constants.defaultPitStones),
                new Pit(Constants.secondPitPlayerB, Constants.defaultPitStones),
                new Pit(Constants.thirdPitPlayerB, Constants.defaultPitStones),
                new Pit(Constants.forthPitPlayerB, Constants.defaultPitStones),
                new Pit(Constants.fifthPitPlayerB, Constants.defaultPitStones),
                new Pit(Constants.sixthPitPlayerB, Constants.defaultPitStones),
                new PitHouse(Constants.leftPitHouseId));
    }
}
