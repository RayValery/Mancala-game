package com.test.mancalagame.model;

import java.util.List;
import lombok.Data;

@Data
public class GameModel {
    private String id;

    private List<PitModel> pits;

    private String playerA;

    private String playerB;

    private String currentPlayer;
}
