package com.test.mancalagame.dal;

import com.test.mancalagame.dal.entity.Game;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MancalaRepository {        //TODO: use database

    private List<Game> games = new ArrayList<>();

    public Game addGame(Game game){
        games.add(game);
        return game;
    }

    public Game updateGame(Game game){
        int gameIndex = games.indexOf(getGame(game.getId()));
        game = games.set(gameIndex, game);
        return game;
    }

    public Game getGame(String gameId){
        return games.stream().filter(game -> game.getId().equals(gameId)).findFirst().get();    //TODO: check presence
    }
}
