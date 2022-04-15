package com.test.mancalagame.controller;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.service.GameService;
import com.test.mancalagame.service.PlayService;
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
        Game game = gameService.create();
        return ResponseEntity.ok(game);
    }

    @PostMapping("/move")
    public ResponseEntity<Game> makeMove(@RequestParam(name = "gameId") String gameId, @RequestParam(name = "pitId") Integer pitId)
            throws Exception {      //TODO: fix exception handling
        Game game = gameService.getGame(gameId);
        game = playService.makeMove(game, pitId);
        return ResponseEntity.ok(game);
    }
}
