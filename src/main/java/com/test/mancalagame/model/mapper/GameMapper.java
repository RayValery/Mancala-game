package com.test.mancalagame.model.mapper;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.dal.entity.Pit;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.model.PitModel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {
    @Autowired
    private PitMapper pitMapper;

    public GameModel mapModel(Game entity) {
        GameModel model = new GameModel();
        model.setId(entity.getId());
        List<PitModel> pits = entity.getPits().stream()
                .map(pitMapper::mapModel).collect(Collectors.toList());
        model.setPits(pits);
        model.setPlayerA(entity.getPlayerA());
        model.setPlayerB(entity.getPlayerB());
        model.setCurrentPlayer(entity.getCurrentPlayer());
        return model;
    }

    public Game mapEntity(GameModel model){
        Game entity = new Game();
        entity.setId(model.getId());
        entity.setPlayerA(model.getPlayerA());
        entity.setPlayerB(model.getPlayerB());
        entity.setCurrentPlayer(model.getCurrentPlayer());
        List<Pit> pits = model.getPits().stream()
                .map(pitMapper::mapEntity).collect(Collectors.toList());
        entity.setPits(pits);
        return entity;
    }
}
