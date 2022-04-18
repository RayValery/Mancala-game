package com.test.mancalagame.dal;

import com.test.mancalagame.dal.entity.Game;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MancalaMongoRepository extends MongoRepository<Game, String> {
    @Query("{'$or': [{'playerA': :#{#playerId}}, {'playerB': :#{#playerId}} ]}")
    Optional<Game> getGameByPlayerId(@Param("playerId") String playerId);
}
