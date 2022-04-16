package com.test.mancalagame.controller;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.exception.ActionNotAllowedException;
import com.test.mancalagame.service.GameService;
import com.test.mancalagame.service.PlayService;
import java.rmi.server.UID;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mancala")
public class MancalaController {
    @Autowired
    private GameService gameService;

    @Autowired
    private PlayService playService;

    @PostMapping
    public ResponseEntity<Game> create(HttpSession httpSession){
        String playerId = new UID().toString();
        Game game = gameService.create(playerId, "EMPTY_PLAYER");   //TODO: only one game now in the map
        httpSession.setAttribute("playerId", playerId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/join")
    public ResponseEntity<Game> joinGame(HttpSession httpSession) {
        String playerId = UUID.randomUUID().toString();
        Game game = gameService.joinAvailableGame(playerId);
        httpSession.setAttribute("playerId", playerId);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/move")   //TODO: PUT  ??
    public ResponseEntity<Game> makeMove(HttpSession httpSession, @RequestParam(name = "pitId") Integer pitId) throws Exception {      //TODO: fix exception handling
        String playerId = (String) httpSession.getAttribute("playerId");
        if (playerId==null) {
            throw new ActionNotAllowedException("Player is not identified.");
        }
        Game gameByPlayerId = gameService.getGameByPlayerId(playerId);
        gameByPlayerId = playService.makeMove(playerId, gameByPlayerId, pitId);
        return ResponseEntity.ok(gameByPlayerId);
    }
}
