package com.test.mancalagame.model.mapper;

import com.test.mancalagame.dal.entity.Pit;
import com.test.mancalagame.model.PitModel;
import org.springframework.stereotype.Component;

@Component
public class PitMapper {
    public PitModel mapModel(Pit entity){
        PitModel model = new PitModel();
        model.setId(entity.getId());
        model.setStones(entity.getStones());
        return model;
    }

    public Pit mapEntity(PitModel model){
        Pit entity = new Pit();
        entity.setId(model.getId());
        entity.setStones(model.getStones());
        return entity;
    }
}
