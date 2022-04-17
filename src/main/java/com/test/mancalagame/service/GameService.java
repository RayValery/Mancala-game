package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaMongoRepository;
import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private MancalaMongoRepository mancalaMongoRepository;

    public Game getGameByPlayerId(String playerId){
        return mancalaMongoRepository.getGameByPlayerId(playerId);     //TODO: check presence
    }

    public Game joinAvailableGameOrCreateNew(String playerId){
        Game availableGame = mancalaMongoRepository.getGameByPlayerId("EMPTY_PLAYER");      //TODO: const
        if (availableGame!=null) {
            availableGame.setPlayerB(playerId);
            availableGame = mancalaMongoRepository.save(availableGame);
            return availableGame;
        }
        Game newGame = new Game(playerId, "EMPTY_PLAYER");
        newGame.setCurrentPlayer(playerId);
        newGame = mancalaMongoRepository.insert(newGame);
        return newGame;
    }
}
