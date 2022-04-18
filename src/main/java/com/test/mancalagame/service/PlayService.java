package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaMongoRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.dal.entity.Constants;
import com.test.mancalagame.dal.entity.Pit;
import com.test.mancalagame.exception.ActionNotAllowedException;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.model.mapper.GameMapper;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {

    @Autowired
    private MancalaMongoRepository mancalaMongoRepository;

    @Autowired
    private GameMapper gameMapper;

    public GameModel makeMove(String playerId, GameModel gameModel, Integer pitId) {
        Game game = gameMapper.mapEntity(gameModel);
        if (!playerId.equals(game.getCurrentPlayer())) {
            throw new ActionNotAllowedException("Action is not allowed. Wrong player move.");
        }
        if (pitId == Constants.rightPitHouseId || pitId == Constants.leftPitHouseId) {
            throw new ActionNotAllowedException("Action not allowed. House pit selected.");
        }
        if (Objects.equals(game.getCurrentPlayer(), game.getPlayerA()) && pitId > Constants.rightPitHouseId ||
                Objects.equals(game.getCurrentPlayer(), game.getPlayerB()) && pitId < Constants.rightPitHouseId) {
            throw new ActionNotAllowedException("Action not allowed. Wrong pit selected.");
        }
        Pit selectedPit = game.getPit(pitId);
        int stones = selectedPit.getStones();
        if (stones == 0) {
            throw new ActionNotAllowedException("Action not allowed. Empty pit selected.");
        }
        selectedPit.clear();
        game.setCurrentPitIndex(pitId);
        for (int i = 0; i < stones - 1; i++) {
            moveRight(game);
        }
        moveLastStone(game);
        if (isGameEnded(game)) {
            addRemainingStones(game);
            game = updateGame(game);
            return gameMapper.mapModel(game);
        }
        int currentPitIndex = game.getCurrentPitIndex();
        if (currentPitIndex != Constants.rightPitHouseId && currentPitIndex != Constants.leftPitHouseId) {
            changePlayer(game);
        }
        game = updateGame(game);
        return gameMapper.mapModel(game);
    }

    private void moveRight(Game game) {
        int targetPitIndex = getTargetPitIndex(game);
        game.setCurrentPitIndex(targetPitIndex);
        Pit targetPit = game.getPit(targetPitIndex);
        targetPit.addStone();
    }

    private void moveLastStone(Game game){
        int targetPitIndex = getTargetPitIndex(game);
        game.setCurrentPitIndex(targetPitIndex);
        Pit targetPit = game.getPit(targetPitIndex);
        if (targetPitIndex == Constants.rightPitHouseId || targetPitIndex == Constants.leftPitHouseId) {
            targetPit.addStone();
            return;
        }
        Pit oppositePit = game.getPit(Constants.totalPits - targetPitIndex);
        if (targetPit.isEmpty() && !oppositePit.isEmpty() && pitIsOwnedByCurrentPlayer(targetPitIndex, game)) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer pitHouseIndex = targetPitIndex < Constants.rightPitHouseId ? Constants.rightPitHouseId : Constants.leftPitHouseId;
            Pit pitHouse = game.getPit(pitHouseIndex);
            pitHouse.addStones(oppositeStones + 1);
            return;
        }
        targetPit.addStone();
    }

    private int getTargetPitIndex(Game game) {
        int targetPitIndex = game.getCurrentPitIndex() % Constants.totalPits + 1;
        if ((targetPitIndex == Constants.rightPitHouseId && Objects.equals(game.getCurrentPlayer(), game.getPlayerB())) ||
                (targetPitIndex == Constants.leftPitHouseId && Objects.equals(game.getCurrentPlayer(), game.getPlayerA()))) {
            targetPitIndex = targetPitIndex % Constants.totalPits + 1;
        }
        return targetPitIndex;
    }

    private Boolean isGameEnded(Game game){
        int totalPlayerA = 0;
        for (int i = 1; i < Constants.rightPitHouseId; i++){
            totalPlayerA += game.getPit(i).getStones();
        }

        int totalPlayerB = 0;
        for (int i = Constants.rightPitHouseId + 1; i < Constants.leftPitHouseId; i++){
            totalPlayerB += game.getPit(i).getStones();
        }

        return ( totalPlayerA==0 || totalPlayerB==0);
    }

    private void addRemainingStones(Game game){
        int totalPlayerA = 0;
        for (int i = 1; i < Constants.rightPitHouseId; i++){
            totalPlayerA += game.getPit(i).getStones();
            game.getPit(i).clear();
        }
        game.getPit(Constants.rightPitHouseId).addStones(totalPlayerA);

        int totalPlayerB = 0;
        for (int i = Constants.rightPitHouseId + 1; i < Constants.leftPitHouseId; i++){
            totalPlayerB += game.getPit(i).getStones();
            game.getPit(i).clear();
        }
        game.getPit(Constants.leftPitHouseId).addStones(totalPlayerB);
    }

    private void changePlayer(Game game) {
        if (game.getCurrentPlayer().equals(game.getPlayerA())) {
            game.setCurrentPlayer(game.getPlayerB());
            return;
        }
        game.setCurrentPlayer(game.getPlayerA());
    }

    private Boolean pitIsOwnedByCurrentPlayer(int pitIndex, Game game){
        return (pitIndex < Constants.rightPitHouseId && Objects.equals(game.getCurrentPlayer(), game.getPlayerA())) ||
                (pitIndex > Constants.rightPitHouseId && Objects.equals(game.getCurrentPlayer(), game.getPlayerB()));
    }

    private Game updateGame(Game game){
        return mancalaMongoRepository.save(game);
    }
}
