package project.backend.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Player;
import project.backend.data.PlayerRegistrationRepresentation;
import project.backend.security.keycloak.KeycloakUserServiceImpl;
import project.backend.service.PlayerService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final KeycloakUserServiceImpl keycloakUserService;
    private final PlayerService playerService;

    @GetMapping("/ciao")
    public ResponseEntity<String> ciao() {
        return ResponseEntity.ok("ciao");
    }


    @Autowired
    public AuthenticationController(KeycloakUserServiceImpl keycloakUserService, PlayerService playerService) {
        this.keycloakUserService = keycloakUserService;
        this.playerService = playerService;
    }

    //TESTED
    @PostMapping("/register")
    public ResponseEntity<?> createAccount(@RequestBody PlayerRegistrationRepresentation player){
        return keycloakUserService.createUser(player);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@AuthenticationPrincipal Jwt jwt){
        try {
            String username = jwt.getClaimAsString("preferred_username");
            if(username.isBlank() || username.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Player player= playerService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: "+username));
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
