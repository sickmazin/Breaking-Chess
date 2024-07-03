package project.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import project.backend.live.LiveGameService;

@RestController("/game")
public class GameController {

    LiveGameService liveGameService;

    @Autowired
    public GameController(LiveGameService liveGameService) {
        this.liveGameService = liveGameService;
    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<String> getGame(@PathVariable String id) {
//        return ResponseEntity.ok("Game is running");
//    }


    @GetMapping("/startGame/{nickname}/{mode}")
    public ResponseEntity<String> startGame(@PathVariable String nickname, @PathVariable String mode) {
        try {
            liveGameService.startGame(nickname, mode);
            return ResponseEntity.ok("Game started");
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
            liveGameService.endGame(nickname, true);
            return ResponseEntity.ok("Ask for draw");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/makeMove/{nickname}/{gameId}/{move}")
    public ResponseEntity<String> makeMove(@PathVariable String nickname, @PathVariable String move, @PathVariable String gameId) {
        try {
            liveGameService.makeMove(gameId, move, nickname); //TODO put player string
            return ResponseEntity.ok("Game started");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
