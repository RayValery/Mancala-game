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

    private PlayerTurn playerTurn;

    @JsonIgnore
    private int currentPitIndex;

    public Game() {
        this (Constants.defaultPitStones);
    }

    public Game(int pitStones) {                                     //TODO: not needed ? simplify to only 6 stones by default
        this.pits = Arrays.asList(
                new Pit(Constants.firstPitPlayerA, pitStones),
                new Pit(Constants.secondPitPlayerA, pitStones),
                new Pit(Constants.thirdPitPlayerA, pitStones),
                new Pit(Constants.forthPitPlayerA, pitStones),
                new Pit(Constants.fifthPitPlayerA, pitStones),
                new Pit(Constants.sixthPitPlayerA, pitStones),
                new PitHouse(Constants.rightPitHouseId),
                new Pit(Constants.firstPitPlayerB, pitStones),
                new Pit(Constants.secondPitPlayerB, pitStones),
                new Pit(Constants.thirdPitPlayerB, pitStones),
                new Pit(Constants.forthPitPlayerB, pitStones),
                new Pit(Constants.fifthPitPlayerB, pitStones),
                new Pit(Constants.sixthPitPlayerB, pitStones),
                new PitHouse(Constants.leftPitHouseId));
    }

    public Game(String id, Integer pitStones) {
        this (pitStones);
        this.id = id;
    }

    // returns the corresponding pit of particular index
    public Pit getPit(Integer pitIndex) throws Exception {
        try {
            return this.pits.get(pitIndex-1);
        }catch (Exception e){
            throw  new Exception("Invalid pitIndex:"+ pitIndex +" has given!");
        }
    }

}
