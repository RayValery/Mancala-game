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

    public Game joinAvailableGame(String playerId){
        Game availableGame = mancalaRepository.getAvailableToJoinGame();
        availableGame.setPlayerB(playerId);
        availableGame = mancalaRepository.updateGame(availableGame);
        return availableGame;
    }

//    public Game joinAvailableGameOrCreateNew(String playerId){
//        Game availableGame = mancalaRepository.getAvailableToJoinGame();
//        if (availableGame!=null) {
//            availableGame.setPlayerB(playerId);
//            availableGame = mancalaRepository.updateGame(availableGame);
//            return availableGame;
//        }
//        Game newGame = new Game(playerId);
//        newGame.setPlayerForNextMove(playerId);
//        newGame = mancalaRepository.addGame(newGame);
//        return newGame;
//    }
}
