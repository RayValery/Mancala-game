package com.test.mancalagame.dal.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pit {
    private Integer id;

    private Integer stones;

    public Pit(Integer id) {
        this.id = id;
        this.stones = Constants.defaultPitStones;
    }

    @JsonIgnore
    public Boolean isEmpty (){
        return this.stones == 0;
    }

    public void clear (){
        this.stones = 0;
    }

    public void addStone() {
        this.stones++;
    }

    public void addStones (Integer stones){
        this.stones+= stones;
    }
}
