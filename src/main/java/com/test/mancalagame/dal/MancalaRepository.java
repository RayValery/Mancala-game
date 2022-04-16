package com.test.mancalagame.dal;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.exception.NoAvailableGameException;
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

    public Game getAvailableToJoinGame(){
        Game game = gamesByPlayers.get("EMPTY_PLAYER");
        if (game==null){                                            //TODO: check here or in service ??
            throw new NoAvailableGameException("No game available to join.");
        }
        return game;
    }
}
