package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.service.FriendService;

@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    @Autowired
    public FriendController (FriendService friendService) {
        this.friendService = friendService;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getFriends(@AuthenticationPrincipal Jwt token) {
        try {
            return ResponseEntity.ok(friendService.getFriends(token.getClaimAsString("preferred_username")));
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/ask")
    public ResponseEntity<?> askRequest(@AuthenticationPrincipal Jwt token, @RequestParam(name = "player") String player) {
        try {
            friendService.askRequest(token.getClaimAsString("preferred_username"), player);
            return ResponseEntity.ok().build();
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/request")
    public ResponseEntity<?> getPendingRequests(@AuthenticationPrincipal Jwt token) {
        try {
            return ResponseEntity.ok(friendService.getPendingRequests(token.getClaimAsString("preferred_username")));
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //used both for deleting friend and deleting friend request
    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@AuthenticationPrincipal Jwt token, @RequestParam(name = "player") String player) {
        try {
            friendService.blockRequest(token.getClaimAsString("preferred_username"), player);
            return ResponseEntity.ok().build();
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/accept")
    public ResponseEntity<?> acceptRequest(@AuthenticationPrincipal Jwt token, @RequestParam(name = "player") String player) {
        try {
            friendService.acceptRequest(token.getClaimAsString("preferred_username"), player);
            return ResponseEntity.ok().build();
        }
        catch(Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
