package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.dal.entity.Constants;
import com.test.mancalagame.dal.entity.Pit;
import com.test.mancalagame.exception.ActionNotAllowedException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {
    @Autowired
    private MancalaRepository mancalaRepository;

    public Game makeMove(String playerId, Game game, Integer pitId) throws Exception {
        if (!playerId.equals(game.getPlayerForNextMove())) {
            throw new ActionNotAllowedException("Action is not allowed. Wrong player move.");
        }
        if (pitId == Constants.rightPitHouseId || pitId == Constants.leftPitHouseId) {
            throw new ActionNotAllowedException("Action not allowed. House pit selected.");
        }
        if (Objects.equals(game.getPlayerForNextMove(), game.getPlayerA()) && pitId > Constants.rightPitHouseId ||
                Objects.equals(game.getPlayerForNextMove(), game.getPlayerB()) && pitId < Constants.rightPitHouseId) {
            throw new ActionNotAllowedException("Action not allowed. Wrong pit selected.");     //TODO: Exception handling
        }

        Pit selectedPit = game.getPit(pitId);  //TODO: fix "throws" section
        int stones = selectedPit.getStones();
        if (stones == Constants.emptyStone) {
            throw new ActionNotAllowedException("Action not allowed. Empty pit selected.");
        }
        selectedPit.setStones(Constants.emptyStone);

        game.setCurrentPitIndex(pitId);

        for (int i = 0; i < stones - 1; i++) {
            moveRight(game, false);
        }
        moveRight(game, true);

        int currentPitIndex = game.getCurrentPitIndex();
        if (currentPitIndex != Constants.rightPitHouseId && currentPitIndex != Constants.leftPitHouseId) {
            changePlayer(game);
        }
        //TODO: update game in db - here?
        updateGame(game);
        return game;
    }

    private void moveRight(Game game, Boolean lastStone) throws Exception {
        int currentPitIndex = game.getCurrentPitIndex() % Constants.totalPits + 1;
        if ((currentPitIndex == Constants.rightPitHouseId && Objects.equals(game.getPlayerForNextMove(), game.getPlayerB())) ||
                (currentPitIndex == Constants.leftPitHouseId && Objects.equals(game.getPlayerForNextMove(), game.getPlayerA()))){
            currentPitIndex = currentPitIndex % Constants.totalPits + 1;
        }

        game.setCurrentPitIndex(currentPitIndex);

        Pit targetPit = game.getPit(currentPitIndex);
        if (!lastStone || currentPitIndex == Constants.rightPitHouseId || currentPitIndex == Constants.leftPitHouseId) {
            targetPit.addStone();
            return;
        }

        Pit oppositePit = game.getPit(Constants.totalPits - currentPitIndex);
        if (targetPit.isEmpty() && !oppositePit.isEmpty() && pitIsOwnedByCurrentPlayer(currentPitIndex, game)) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer pitHouseIndex = currentPitIndex < Constants.rightPitHouseId ? Constants.rightPitHouseId : Constants.leftPitHouseId;
            Pit pitHouse = game.getPit(pitHouseIndex);
            pitHouse.addStones(oppositeStones + 1);
            return;
        }
        targetPit.addStone();
    }

    public void changePlayer(Game game) {
        if (game.getPlayerForNextMove().equals(game.getPlayerA())) {
            game.setPlayerForNextMove(game.getPlayerB());
            return;
        }
        game.setPlayerForNextMove(game.getPlayerA());
    }

    private Boolean pitIsOwnedByCurrentPlayer(int pitIndex, Game game){
        return pitIndex < Constants.rightPitHouseId && Objects.equals(game.getPlayerForNextMove(), game.getPlayerA()) ||
                pitIndex > Constants.rightPitHouseId && Objects.equals(game.getPlayerForNextMove(), game.getPlayerB());
    }

    private Game updateGame(Game game){
        return mancalaRepository.updateGame(game);
    }
}
