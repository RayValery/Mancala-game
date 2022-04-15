package com.test.mancalagame.dal;

import com.test.mancalagame.dal.entity.Game;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MancalaRepository {        //TODO: use database

    private Map<String, Game> gamesByPlayers = new HashMap<>();

    public Game addGame(Game game){
        gamesByPlayers.put(game.getPlayerA(), game);
        gamesByPlayers.put(game.getPlayerB(), game);
        return game;
    }

    public Game updateGame(Game game){
        gamesByPlayers.put(game.getPlayerA(), game);
        gamesByPlayers.put(game.getPlayerB(), game);
        return game;
    }

    public Game getGame(String playerId){
        return gamesByPlayers.get(playerId);  //TODO: check presence
    }
}
