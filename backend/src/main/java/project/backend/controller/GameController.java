package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Game;
import project.backend.exceptions.LiveGameNotFoundException;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.live.LiveGameDTO;
import project.backend.live.LiveGameService;

import java.util.List;
import java.util.Optional;

@RestController()
@RequestMapping("/api/game")
public class GameController {

    LiveGameService liveGameService;

    @Autowired
    public GameController(LiveGameService liveGameService) {
        this.liveGameService = liveGameService;
    }

    @GetMapping("/start")
    public ResponseEntity<?> startGame(
                                       @AuthenticationPrincipal Jwt jwt,
                                       @RequestParam(name = "mode") String mode) {
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.startGame(jwt.getClaimAsString("preferred_username"), mode);
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getGame(@AuthenticationPrincipal Jwt jwt) {
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.getGame(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/abort")
    public ResponseEntity<?> abort(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.abortGame(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok("Aborted game");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/resign")
    public ResponseEntity<?> resign(@AuthenticationPrincipal Jwt jwt) {
        try {
            return ResponseEntity.ok(liveGameService.endGame(jwt.getClaimAsString("preferred_username"), false));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/draw")
    public ResponseEntity<?> drawRequest(@AuthenticationPrincipal Jwt jwt) {
        try {
            Optional<LiveGameDTO> optional =liveGameService.drawRequest(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok(optional);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/deny")
    public ResponseEntity<?> denyDrawRequest(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.denyRequest(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok("Ask for draw");
        } catch (LiveGameNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/move")
    public ResponseEntity<?> makeMove(@AuthenticationPrincipal Jwt jwt,
                                                @RequestParam(name = "move") String move) {
        try {
            LiveGameDTO liveGameDTO = liveGameService.makeMove(jwt.getClaimAsString("preferred_username"), move);
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/games/get")
    public ResponseEntity<?> getGames(@AuthenticationPrincipal Jwt jwt) {
        try {
            return new ResponseEntity<>(liveGameService.getGames(jwt.getClaimAsString("preferred_username")), HttpStatus.OK);
        } catch (PlayerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
