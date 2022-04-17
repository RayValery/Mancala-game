package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private MancalaRepository mancalaRepository;

    public Game getGameByPlayerId(String playerId){
        return mancalaRepository.getGame(playerId);     //TODO: check presence
    }

    public Game joinAvailableGameOrCreateNew(String playerId){
        Game availableGame = mancalaRepository.getAvailableToJoinGame();
        if (availableGame!=null) {
            availableGame.setPlayerB(playerId);
            availableGame = mancalaRepository.updateGame(availableGame);
            return availableGame;
        }
        Game newGame = new Game(playerId, "EMPTY_PLAYER");
        newGame.setPlayerForNextMove(playerId);
        newGame = mancalaRepository.addGame(newGame);
        return newGame;
    }
}
