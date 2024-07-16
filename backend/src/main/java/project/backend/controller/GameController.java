package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Game;
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
    public ResponseEntity<Optional<LiveGameDTO>> startGame(
                                                 @AuthenticationPrincipal Jwt jwt,
                                                 @RequestParam(name = "mode") String mode) {
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.startGame(jwt.getClaimAsString("preferred_username"), mode);
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Optional<LiveGameDTO>> getGame(@AuthenticationPrincipal Jwt jwt) {
        try {
            Optional<LiveGameDTO> liveGameDTO = liveGameService.getGame(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/abort")
    public ResponseEntity<String> abort(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.abortGame(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok("Aborted");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/resign")
    public ResponseEntity<String> resign(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.endGame(jwt.getClaimAsString("preferred_username"), false);
            return ResponseEntity.ok("Resigned");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/draw")
    public ResponseEntity<String> drawRequest(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.drawRequest(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok("Ask for draw");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/deny")
    public ResponseEntity<String> denyDrawRequest(@AuthenticationPrincipal Jwt jwt) {
        try {
            liveGameService.denyRequest(jwt.getClaimAsString("preferred_username"));
            return ResponseEntity.ok("Ask for draw");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/move")
    public ResponseEntity<LiveGameDTO> makeMove(@AuthenticationPrincipal Jwt jwt,
                                                @RequestParam(name = "move") String move) {
        try {
            LiveGameDTO liveGameDTO = liveGameService.makeMove(jwt.getClaimAsString("preferred_username"), move); //TODO put player string
            return ResponseEntity.ok(liveGameDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/games/get")
    public ResponseEntity<List<Game>> getGames(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(liveGameService.getGames(jwt.getClaimAsString("preferred_username")), HttpStatus.OK);
    }
}
