package project.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Game;
import project.backend.service.PlayerService;

@RestController
@RequestMapping("/api/options")
@AllArgsConstructor
public class OptionsController {
    private final PlayerService playerService;

    @PutMapping("/changeFirstName")
    public ResponseEntity<?> changeFirstName(@RequestParam String firstName,@AuthenticationPrincipal Jwt token) {
        return playerService.changeFirstName(token.getClaimAsString("preferred_username"),firstName);
    }
    @PutMapping("/changeLastName")
    public ResponseEntity<?> changeLastName(@RequestParam String lastName,@AuthenticationPrincipal Jwt token) {
        return playerService.changeLastName(token.getClaimAsString("preferred_username"),lastName);
    }
    @PutMapping("/changeNickname")
    public ResponseEntity<?> changeNickname(@RequestParam String nickname,@AuthenticationPrincipal Jwt token) {
        return playerService.changeNickname(token.getClaimAsString("preferred_username"),nickname);
    }
    @PutMapping("/changeEmail")
    public ResponseEntity<?> changeEmail(@RequestParam String email,@AuthenticationPrincipal Jwt token) {
        return playerService.changeEmail(token.getClaimAsString("preferred_username"),email);
    }
    @PutMapping("/changeAvatar")
    public ResponseEntity<?> changeAvatar(@RequestParam String avatar,@AuthenticationPrincipal Jwt token) {
        return playerService.changeAvatar(token.getClaimAsString("preferred_username"),avatar);
    }

    @GetMapping(value = "/statistic")
    public ResponseEntity<?> statistic( @RequestParam(name = "player") String player,  @RequestParam(name = "mode") String mode) {
        return playerService.statisticGamesForModality(player, Game.TYPE.valueOf(mode));
    }
}
