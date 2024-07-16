package project.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.data.Game;
import project.backend.live.LiveGameDTO;
import project.backend.live.LiveGameService;
import project.backend.service.GameService;
import project.backend.service.PlayerService;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/games")
public class GameController {

    private final LiveGameService liveGameService;
    private final GameService gameService;
    private final PlayerService playerService;
    @Autowired
    public GameController(LiveGameService liveGameService,GameService gameService, PlayerService playerService) {
        this.liveGameService = liveGameService;
        this.gameService = gameService;
        this.playerService = playerService;
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<String> getGame(@PathVariable String id) {
//        return ResponseEntity.ok("Game is running");
//    }


    @GetMapping("/start/{nickname}/{mode}")
    public ResponseEntity<Optional<LiveGameDTO>> startGame(
                                                 @PathVariable String nickname,
                                                 @PathVariable String mode) {
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.startGame(nickname, mode);
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get/{nickname}")
    public ResponseEntity<Optional<LiveGameDTO>> getGame(@PathVariable String nickname) {
        System.out.println("sono qui "+nickname);
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.getGame(nickname);
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/resign/{nickname}")
    public ResponseEntity<String> resign(@PathVariable String nickname) {
        try {
            liveGameService.endGame(nickname, false);
            return ResponseEntity.ok("Resigned");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/draw/{nickname}")
    public ResponseEntity<String> draw(@PathVariable String nickname) {
        try {
            //TODO gestire la rischiesta di patta
            liveGameService.endGame(nickname, true);
            return ResponseEntity.ok("Ask for draw");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    //TODO
    @GetMapping("/listOfGame")
    public ResponseEntity<List<Game>> getListGameFromPlayer(@AuthenticationPrincipal Jwt token) {
        return new ResponseEntity<>(gameService.getListOfGames(token.getClaimAsString("preferred_username")), HttpStatus.OK);
    }

    @GetMapping("/move/{nickname}/{gameId}/{move}")
    public ResponseEntity<LiveGameDTO> makeMove(@PathVariable String nickname, @PathVariable String move, @PathVariable String gameId) {
        try {
            LiveGameDTO liveGameDTO = liveGameService.makeMove(gameId, move, nickname); //TODO put player string
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
