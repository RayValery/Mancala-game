package com.test.mancalagame.service;

import com.test.mancalagame.dal.MancalaRepository;
import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.dal.entity.Constants;
import com.test.mancalagame.dal.entity.Pit;
import com.test.mancalagame.dal.entity.PlayerTurn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayService {
    @Autowired
    private MancalaRepository mancalaRepository;

    public Game makeMove(Game game, Integer pitId) throws Exception {
        // No movement on House pits
        if (pitId == Constants.rightPitHouseId || pitId == Constants.leftPitHouseId) {
            return game;
        }

        // we set the player turn for the first move of the game based on the pit id
        if (game.getPlayerTurn() == null) {
            if (pitId < Constants.rightPitHouseId) {
                game.setPlayerTurn(PlayerTurn.PlayerA);
            } else {
                game.setPlayerTurn(PlayerTurn.PlayerB);
            }
        }

        // we need to check if request comes from the right player otherwise we do not move the game. In other words,
        // we keep the turn for the correct player
        if (game.getPlayerTurn() == PlayerTurn.PlayerA && pitId > Constants.rightPitHouseId ||
                game.getPlayerTurn() == PlayerTurn.PlayerB && pitId < Constants.rightPitHouseId) {
            return game;
        }

        Pit selectedPit = game.getPit(pitId);

        int stones = selectedPit.getStones();

        // No movement for empty Pits
        if (stones == Constants.emptyStone) {
            return game;
        }

        selectedPit.setStones(Constants.emptyStone);

        // keep the pit index, used for moving the stones in right pits
        game.setCurrentPitIndex(pitId);

        // simply move all stones except the last one
        for (int i = 0; i < stones - 1; i++) {
            moveRight(game,false);
        }

        // simply move the last stone
        moveRight(game,true);

        int currentPitIndex = game.getCurrentPitIndex();

        // we switch the turn if the last sow was not on any of pit houses (left or right)
        if (currentPitIndex != Constants.rightPitHouseId && currentPitIndex != Constants.leftPitHouseId) {
            game.setPlayerTurn(nextTurn(game.getPlayerTurn()));
        }

        //TODO: update game in db - here?
        updateGame(game);

        return game;
    }

    // move the game one pit to the right
    private void moveRight(Game game, Boolean lastStone) throws Exception {
        int currentPitIndex = game.getCurrentPitIndex() % Constants.totalPits + 1;

        PlayerTurn playerTurn = game.getPlayerTurn();

        if ((currentPitIndex == Constants.rightPitHouseId && playerTurn == PlayerTurn.PlayerB) ||
                (currentPitIndex == Constants.leftPitHouseId && playerTurn == PlayerTurn.PlayerA)){
            currentPitIndex = currentPitIndex % Constants.totalPits + 1;
        }

        game.setCurrentPitIndex(currentPitIndex);

        Pit targetPit = game.getPit(currentPitIndex);
        if (!lastStone || currentPitIndex == Constants.rightPitHouseId || currentPitIndex == Constants.leftPitHouseId) {
            targetPit.addStone();
            return;
        }

        // It's the last stone and we need to check the opposite player's pit status
        Pit oppositePit = game.getPit(Constants.totalPits - currentPitIndex);

        // we are moving the last stone and the current player's pit is empty but the opposite pit is not empty, therefore,
        // we collect the opposite's Pit stones plus the last stone and add them to the House Pit of current player and
        // make the opposite Pit empty
        if (targetPit.isEmpty() && !oppositePit.isEmpty()) {
            Integer oppositeStones = oppositePit.getStones();
            oppositePit.clear();
            Integer pitHouseIndex = currentPitIndex < Constants.rightPitHouseId ? Constants.rightPitHouseId : Constants.leftPitHouseId;
            Pit pitHouse = game.getPit(pitHouseIndex);
            pitHouse.addStones(oppositeStones + 1);
            return;
        }

        targetPit.addStone();
    }

    public PlayerTurn nextTurn(PlayerTurn currentTurn) {
        if (currentTurn == PlayerTurn.PlayerA) {
            return PlayerTurn.PlayerB;
        }
        return PlayerTurn.PlayerA;
    }

    private Game updateGame(Game game){
        return mancalaRepository.updateGame(game);
    }
}
