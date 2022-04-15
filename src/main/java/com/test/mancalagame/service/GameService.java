package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private MancalaRepository mancalaRepository;

    public Game create(String playerA, String playerB){
        Game game = new Game(playerA, playerB);
        game.setPlayerForNextMove(playerA);
        game = mancalaRepository.addGame(game);
        return game;
    }

    public Game getGameByPlayerId(String playerId){
        return mancalaRepository.getGame(playerId);
    }

}
