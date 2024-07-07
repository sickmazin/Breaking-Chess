package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Game;
import project.backend.data.Player;
import project.backend.service.GameService;
import project.backend.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/games")
public class GamesController {

    private final GameService gameService;
    private final PlayerService playerService;

    @Autowired
    public GamesController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    //TO TEST
    @PostMapping("/wantToPlay/{modality}")
    public ResponseEntity<?> postGame(@RequestBody Player player, @PathVariable String modality) {
        try {
            Player playerWhoWantToPlay = playerService.findByUsername(player.getUsername()).orElseThrow(() -> new UsernameNotFoundException("PLAYER NOT FOUND"));
            return new ResponseEntity<>(gameService.generateNewGameFromPlayer(playerWhoWantToPlay,modality),HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //TO TEST
    @PostMapping("/guests/wantToPlay/{modality}")
    public ResponseEntity<Game> postGameForGuests(@PathVariable String modality) {
        return new ResponseEntity<>(gameService.generateNewGameForGuest(modality),HttpStatus.OK);
    }

    //TO TEST
    @PostMapping("/{player1_nickname}/wantToPlay/{modality}/against/{player2_nickname}")
    public ResponseEntity<?> postGameVsFriends(@PathVariable String player1_nickname,@PathVariable String player2_nickname, @PathVariable String modality) {
        try {
            Player playerWhoWantToPlay= playerService.findByUsername(player1_nickname).orElseThrow(() -> new UsernameNotFoundException("Player "+player1_nickname+" not found"));
            Player playerFriend= playerService.findByUsername(player2_nickname).orElseThrow(() -> new UsernameNotFoundException("Player "+player2_nickname+" not found"));
            return new ResponseEntity<>(gameService.generateNewGameVsFriends(playerWhoWantToPlay,playerFriend,modality),HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    //TODO
    @GetMapping("/listFrom/{player_nickname}")
    public ResponseEntity<List<Game>> getListGameFromPlayer(@PathVariable String player_nickname) {
        return new ResponseEntity<>(gameService.getListOfGames(player_nickname), HttpStatus.OK);
    }

    //TODO
    @DeleteMapping("/{gameId}")
    public ResponseEntity<String> deleteGame(@PathVariable Long gameId) {
        gameService.deleteGameByID(gameId);
        return new ResponseEntity<>("GAME DELETED!",HttpStatus.OK);
    }


}



