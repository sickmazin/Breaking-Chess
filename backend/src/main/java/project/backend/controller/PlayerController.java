package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.data.Player;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {
    private PlayerService playerService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping("/get/{username}")
    public ResponseEntity<?> getPlayer(@PathVariable(name = "username") String username) {
        try {
            return ResponseEntity.ok(playerService.getPlayer(username));
        } catch (PlayerNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
