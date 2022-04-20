package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaMongoRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.exception.ActionNotAllowedException;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.model.mapper.GameMapper;
import com.test.mancalagame.model.mapper.PitMapper;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class PlayServiceTest {

    private PlayService playService;
    private MancalaMongoRepository repository;
    private GameMapper gameMapper;

    @Before
    public void init() {
        repository = mock(MancalaMongoRepository.class);
        gameMapper = new GameMapper(new PitMapper());
        playService = new PlayService(repository, gameMapper);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void makeMoveIfWrongPlayerMove() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerAId);
        GameModel gameModel = gameMapper.mapModel(game);

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action is not allowed. Wrong player move.");

        playService.makeMove(playerBId, gameModel, 1);
    }

    @Test
    public void makeMoveIfRightHousePitSelected() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerAId);
        GameModel gameModel = gameMapper.mapModel(game);
        int rightHousePitId = 7;

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action not allowed. House pit selected.");

        playService.makeMove(playerAId, gameModel, rightHousePitId);
    }

    @Test
    public void makeMoveIfLeftHousePitSelected() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerAId);
        GameModel gameModel = gameMapper.mapModel(game);
        int leftHousePitId = 14;

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action not allowed. House pit selected.");

        playService.makeMove(playerAId, gameModel, leftHousePitId);
    }

    @Test
    public void makeMoveIfPlayerBPitSelectedAndCurrentPlayerIsPlayerA() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerAId);
        GameModel gameModel = gameMapper.mapModel(game);
        int playerBPitId = 8;

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action not allowed. Wrong pit selected.");

        playService.makeMove(playerAId, gameModel, playerBPitId);
    }

    @Test
    public void makeMoveIfPlayerAPitSelectedAndCurrentPlayerIsPlayerB() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerBId);
        GameModel gameModel = gameMapper.mapModel(game);
        int playerAPitId = 5;

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action not allowed. Wrong pit selected.");

        playService.makeMove(playerBId, gameModel, playerAPitId);
    }

    @Test
    public void makeMoveIfSelectedPitIsEmpty() {
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        int pitId = 1;
        Game game = new Game(playerAId, playerBId);
        game.setCurrentPlayer(playerAId);
        game.getPit(pitId).clear();
        GameModel gameModel = gameMapper.mapModel(game);

        expectedException.expect(ActionNotAllowedException.class);
        expectedException.expectMessage("Action not allowed. Empty pit selected.");

        playService.makeMove(playerAId, gameModel, pitId);
    }

    @Test
    public void makeMove(){
        int pitId = 2;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        GameModel gameModelStartState = gameMapper.mapModel(createGame(playerAId, playerBId));
        Game gameExpectedState = createGame(playerAId, playerBId);
        for(int i = pitId + 1; i <= pitId + gameExpectedState.getPit(pitId).getStones(); i++){
            gameExpectedState.getPit(i).addStone();
        }
        gameExpectedState.getPit(pitId).clear();
        gameExpectedState.setCurrentPlayer(playerBId);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerAId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveWhenLastStoneCameToRightHouseAndCurrentPlayerIsPlayerA(){
        int pitId = 1;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        GameModel gameModelStartState = gameMapper.mapModel(createGame(playerAId, playerBId));
        Game gameExpectedState = createGame(playerAId, playerBId);
        for(int i = pitId + 1; i <= pitId + gameExpectedState.getPit(pitId).getStones(); i++){
            gameExpectedState.getPit(i).addStone();
        }
        gameExpectedState.getPit(pitId).clear();
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerAId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveWhenLastStoneCameToLeftHouseAndCurrentPlayerIsPlayerB(){
        int pitId = 8;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game gameStartState = createGame(playerAId, playerBId);
        gameStartState.setCurrentPlayer(playerBId);
        GameModel gameModelStartState = gameMapper.mapModel(gameStartState);

        Game gameExpectedState = createGame(playerAId, playerBId);
        for(int i = pitId + 1; i <= pitId + gameExpectedState.getPit(pitId).getStones(); i++){
            gameExpectedState.getPit(i).addStone();
        }
        gameExpectedState.getPit(pitId).clear();
        gameExpectedState.setCurrentPlayer(playerBId);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerBId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveWhenLastStoneCameToRightHouseAndCurrentPlayerIsPlayerB(){
        int pitId = 13;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game gameStartState = createGame(playerAId, playerBId);
        gameStartState.setCurrentPlayer(playerBId);
        gameStartState.getPit(13).setStones(8);
        GameModel gameModelStartState = gameMapper.mapModel(gameStartState);

        Game gameExpectedState = createGame(playerAId, playerBId);
        gameExpectedState.getPit(pitId).clear();
        gameExpectedState.getPit(14).addStone();
        for (int i = 1; i <= 6; i++){
            gameExpectedState.getPit(i).addStone();
        }
        gameExpectedState.getPit(8).addStone();
        gameExpectedState.setCurrentPlayer(playerAId);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerBId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveWhenLastStoneCameToLeftHouseAndCurrentPlayerIsPlayerA(){
        int pitId = 6;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game gameStartState = createGame(playerAId, playerBId);
        gameStartState.getPit(6).setStones(8);
        GameModel gameModelStartState = gameMapper.mapModel(gameStartState);

        Game gameExpectedState = createGame(playerAId, playerBId);
        gameExpectedState.getPit(pitId).clear();
        gameExpectedState.getPit(7).addStone();
        for (int i = 8; i <= 13; i++){
            gameExpectedState.getPit(i).addStone();
        }
        gameExpectedState.getPit(1).addStone();
        gameExpectedState.setCurrentPlayer(playerBId);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerAId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveWhenLastStoneCameToEmptyPitAndOppositePitIsNotEmpty(){
        int pitId = 1;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game gameStartState = createGame(playerAId, playerBId);
        gameStartState.getPit(1).setStones(1);
        gameStartState.getPit(2).setStones(0);
        gameStartState.getPit(12).setStones(1);
        GameModel gameModelStartState = gameMapper.mapModel(gameStartState);

        Game gameExpectedState = createGame(playerAId, playerBId);
        gameExpectedState.getPit(pitId).clear();
        gameExpectedState.getPit(2).clear();
        gameExpectedState.getPit(12).clear();
        gameExpectedState.getPit(7).addStones(2);
        gameExpectedState.setCurrentPlayer(playerBId);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerAId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void makeMoveEndGame(){
        int pitId = 6;
        String playerAId = "playerAId";
        String playerBId = "playerBId";
        Game gameStartState = createGame(playerAId, playerBId);
        for(int i = 1; i <= 5; i++){
            gameStartState.getPit(i).clear();
        }
        gameStartState.getPit(6).setStones(1);
        GameModel gameModelStartState = gameMapper.mapModel(gameStartState);

        Game gameExpectedState = createGame(playerAId, playerBId);
        for(int i = 1; i <= 6; i++){
            gameExpectedState.getPit(i).clear();
        }
        for(int i = 8; i <= 13; i++){
            gameExpectedState.getPit(i).clear();
        }
        gameExpectedState.getPit(14).addStones(36);
        gameExpectedState.getPit(7).setStones(1);
        GameModel gameModelExpectedState = gameMapper.mapModel(gameExpectedState);

        when(repository.save(any())).thenAnswer((Answer<Game>) invocation -> {
            Object[] args = invocation.getArguments();
            return (Game) args[0];
        });

        GameModel gameActualState = playService.makeMove(playerAId, gameModelStartState, pitId);

        assertEquals(gameModelExpectedState, gameActualState);

        verify(repository, times(1)).save(any());
    }

    private Game createGame(String playerA, String playerB){
        Game game = new Game(playerA, playerB);
        game.setCurrentPlayer(playerA);
        return game;
    }
}