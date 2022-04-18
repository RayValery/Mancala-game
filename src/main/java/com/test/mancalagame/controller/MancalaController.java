package com.test.mancalagame.controller;

import com.test.mancalagame.dal.entity.Game;
import com.test.mancalagame.model.GameModel;
import com.test.mancalagame.service.GameService;
import com.test.mancalagame.service.PlayService;
import java.util.UUID;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/join")
    public ResponseEntity<GameModel> joinGame(HttpSession httpSession) {
        String playerId = UUID.randomUUID().toString();
        GameModel game = gameService.joinGame(playerId);
        httpSession.setAttribute("playerId", playerId);
        return ResponseEntity.ok(game);
    }

    @PutMapping("/move")
    public ResponseEntity<GameModel> makeMove(HttpSession httpSession, @RequestParam(name = "pitId") Integer pitId) {
        String playerId = (String) httpSession.getAttribute("playerId");
        GameModel gameByPlayerId = gameService.getGameByPlayerId(playerId);
        gameByPlayerId = playService.makeMove(playerId, gameByPlayerId, pitId);
        return ResponseEntity.ok(gameByPlayerId);
    }

    @GetMapping("/game")
    public ResponseEntity<GameModel> getGameState(HttpSession httpSession){
        String playerId = (String) httpSession.getAttribute("playerId");
        GameModel currentGameState = gameService.getGameByPlayerId(playerId);
        return ResponseEntity.ok(currentGameState);
    }
}
