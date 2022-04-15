package com.test.mancalagame.controller;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.service.GameService;
import com.test.mancalagame.service.PlayService;
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
    public ResponseEntity<Game> create(){
        Game game = gameService.create("playerA1", "playerB2");
        return ResponseEntity.ok(game);
    }

    @PostMapping("/test")
    public ResponseEntity<Game> makeMove(@RequestParam(name = "playerId") String playerId,
                                         @RequestParam(name = "pitId") Integer pitId) throws Exception {
        Game gameByPlayerId = gameService.getGameByPlayerId(playerId);
        gameByPlayerId = playService.makeMove(playerId, gameByPlayerId, pitId);
        return ResponseEntity.ok(gameByPlayerId);
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(HttpSession httpSession, @RequestParam(name = "pitId") Integer pitId) throws Exception {      //TODO: fix exception handling
        String playerId = (String) httpSession.getAttribute("playerId");
        Game gameByPlayerId = gameService.getGameByPlayerId(playerId);
        gameByPlayerId = playService.makeMove(playerId, gameByPlayerId, pitId);
        return ResponseEntity.ok(gameByPlayerId);
    }
}
