package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private MancalaRepository mancalaRepository;

    public Game create(){
        Game game = new Game();
        game.setId("1");        //TODO: make generation
        game = mancalaRepository.addGame(game);
        return game;
    }

    public Game getGame(String gameId){
        return mancalaRepository.getGame(gameId);    //TODO: check presence, add exception
    }
}
