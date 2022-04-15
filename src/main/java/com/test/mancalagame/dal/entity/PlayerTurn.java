package com.test.mancalagame.dal.entity;

import lombok.Getter;

@Getter
public enum PlayerTurn {

    PlayerA ("A"),
    PlayerB ("B");

    private String turn;

    PlayerTurn(String turn) {
        this.turn = turn;
    }

}
