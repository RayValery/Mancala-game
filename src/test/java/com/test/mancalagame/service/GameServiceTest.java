package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaMongoRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.exception.ActionNotAllowedException;
import com.test.mancalagame.exception.NoAvailableGameException;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.model.mapper.GameMapper;
import com.test.mancalagame.model.mapper.PitMapper;
import java.util.ArrayList;
import java.util.Optional;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameServiceTest {

    private GameService gameService;
    private MancalaMongoRepository repository;
    private GameMapper gameMapper;

    @Before
    public void init() {
        repository = mock(MancalaMongoRepository.class);
        gameMapper = new GameMapper(new PitMapper());
        gameService = new GameService(repository, gameMapper);
    }

    private static Game createGameEntity(){
        Game gameEntity = new Game();
        gameEntity.setId("id");
        gameEntity.setPlayerA("playerAId");
        gameEntity.setPlayerB("playerBId");
        gameEntity.setCurrentPlayer("playerAId");
        gameEntity.setPits(new ArrayList<>());
        return gameEntity;
    }

    private static GameModel createGameModel(){
        GameModel gameModel = new GameModel();
        gameModel.setId("id");
        gameModel.setPlayerA("playerAId");
        gameModel.setPlayerB("playerBId");
        gameModel.setCurrentPlayer("playerAId");
        gameModel.setPits(new ArrayList<>());
        return gameModel;
    }

    @Test
    public void getGameByPlayerIdIfGameExsists() {
        String playerAId = "playerAId";

        when(repository.getGameByPlayerId(anyString())).thenReturn(Optional.of(createGameEntity()));

        GameModel gameModelExpected = createGameModel();
        GameModel gameModelActual = gameService.getGameByPlayerId(playerAId);

        assertEquals(gameModelExpected, gameModelActual);
        verify(repository, times(1)).getGameByPlayerId(playerAId);
    }

    @Test(expected = ActionNotAllowedException.class)
    public void getGameByPlayerIdIfPlayerIdNull() {
        String playerId = null;

        gameService.getGameByPlayerId(playerId);

        verify(repository, times(0)).getGameByPlayerId(playerId);
    }

    @Test(expected = NoAvailableGameException.class)
    public void getGameByPlayerIdIfNoGameFound() {
        String playerId = "notExistingPlayerId";

        gameService.getGameByPlayerId(playerId);

        verify(repository, times(1)).getGameByPlayerId(playerId);
    }

    @Test
    public void joinGameIfGameWasAlreadyCreated() {
        String playerBId = "playerBId";
        String emptyPlayer = "EMPTY_PLAYER";
        Game gameEntityWithOnePlayer = createGameEntity();
        gameEntityWithOnePlayer.setPlayerB(emptyPlayer);
        Game gameEntityToSave = gameEntityWithOnePlayer;
        gameEntityToSave.setPlayerB(playerBId);

        when(repository.getGameByPlayerId(anyString())).thenReturn(Optional.of(gameEntityWithOnePlayer));
        when(repository.save(any())).thenReturn(gameEntityToSave);

        GameModel gameModelExpected = createGameModel();
        GameModel gameModelActual = gameService.joinGame(playerBId);

        assertEquals(gameModelExpected, gameModelActual);
        verify(repository, times(1)).getGameByPlayerId(emptyPlayer);
        verify(repository, times(1)).save(gameEntityToSave);
    }

    @Test
    public void joinGameIfGameWasNotCreated() {
        String playerAId = "playerAId";
        String emptyPlayer = "EMPTY_PLAYER";
        Game gameEntityWithOnePlayer = new Game(playerAId, emptyPlayer);
        gameEntityWithOnePlayer.setCurrentPlayer(playerAId);

        when(repository.getGameByPlayerId(anyString())).thenReturn(Optional.empty());
        when(repository.insert(any(Game.class))).thenReturn(gameEntityWithOnePlayer);

        GameModel gameModelExpected = gameMapper.mapModel(gameEntityWithOnePlayer);
        GameModel gameModelActual = gameService.joinGame(playerAId);

        assertEquals(gameModelExpected, gameModelActual);
        verify(repository, times(1)).getGameByPlayerId(emptyPlayer);
        verify(repository, times(1)).insert(gameEntityWithOnePlayer);
    }
}