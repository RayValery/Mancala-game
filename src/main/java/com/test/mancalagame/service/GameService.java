package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaMongoRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.exception.ActionNotAllowedException;
import com.test.mancalagame.exception.NoAvailableGameException;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.model.mapper.GameMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameService {

    private static final String EMPTY_PLAYER = "EMPTY_PLAYER";

    private final MancalaMongoRepository mancalaMongoRepository;
    private final GameMapper gameMapper;

    public GameModel getGameByPlayerId(String playerId) {
        if (playerId == null) {
            throw new ActionNotAllowedException("Player is not identified.");
        }
        Game game = mancalaMongoRepository.getGameByPlayerId(playerId).orElseThrow(() ->
                new NoAvailableGameException("No game found for player " + playerId));
        return gameMapper.mapModel(game);
    }

    public GameModel joinGame(String playerId) {
        Optional<Game> availableGame = mancalaMongoRepository.getGameByPlayerId(EMPTY_PLAYER);
        if (availableGame.isPresent()) {
            Game game = availableGame.get();
            game.setPlayerB(playerId);
            game = mancalaMongoRepository.save(game);
            return gameMapper.mapModel(game);
        }
        Game newGame = new Game(playerId, EMPTY_PLAYER);
        newGame.setCurrentPlayer(playerId);
        newGame = mancalaMongoRepository.insert(newGame);
        return gameMapper.mapModel(newGame);
    }
}
