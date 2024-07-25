package project.backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import project.backend.data.Book;
import project.backend.data.Player;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.service.BookService;
import project.backend.service.LikeService;
import project.backend.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final BookService bookService;
    private final PlayerService playerService;
    private final LikeService likeService;

    @Autowired
    public HomeController (BookService bookService, PlayerService playerService, LikeService likeService) {
        this.bookService = bookService;
        this.playerService = playerService;
        this.likeService = likeService;
    }


    // QUESTO METODO DOVREBBE ESSER IMPLEMENTATO PER ESSER DISPONIBILE ALL'ADMIN CHE DA FRONTEND POSSA AGGIUNGERE UN NUOVO LIBRO.
    // MA è UNA NUOVA FEATURE IN ARRIVO COMING SOON...
//    @PostMapping("/book")
//    public ResponseEntity<?> createBook(){
//        return bookService.createBook();
//    }


    //TO TEST
    @GetMapping("/books")
    public ResponseEntity<?> books(@AuthenticationPrincipal Jwt jwt) {
        return bookService.findAll(jwt.getClaimAsString("preferred_username"));
    }

    // TESTED
    @GetMapping(value = "/leaderboard/{modality}")
    public ResponseEntity<?> leaderboard(@PathVariable("modality") String modality) {
        return playerService.getLeaderboard(modality);
    }

    // TESTED
    @PutMapping(value = "/books/like/{bookID}")
    public ResponseEntity<?> addLike (@PathVariable @Valid int bookID, @AuthenticationPrincipal Jwt jwt) {
        var remainingRetries = 100;
        while (remainingRetries > 0) {
            try {
                Player p = playerService.getPlayer(jwt.getClaimAsString("preferred_username"));
                return  this.bookService.addLike(bookID, p);
            } catch (ObjectOptimisticLockingFailureException e) {
                remainingRetries--;
                if (remainingRetries == 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            } catch (PlayerNotFoundException e) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    @DeleteMapping(value = "/books/like/{bookID}")
    public ResponseEntity<?> deleteLike (@PathVariable @Valid int  bookID, @AuthenticationPrincipal Jwt jwt) {
        var remainingRetries = 100;
        while (remainingRetries > 0) {
            try {
                Player p = playerService.getPlayer(jwt.getClaimAsString("preferred_username"));
                return this.bookService.deleteLike(bookID, p);
            } catch (ObjectOptimisticLockingFailureException e) {
                remainingRetries--;
                if (remainingRetries == 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            } catch (PlayerNotFoundException e) {
                return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
            }
        }
        return ResponseEntity.internalServerError().build();
    }

    // GET LIKE DEL PLAYER
    @GetMapping(value = "/books/likes")
    public ResponseEntity<?> likes(@AuthenticationPrincipal Jwt jwt) {
        try {
            Player p = playerService.getPlayer(jwt.getClaimAsString("preferred_username"));
            List<Book> likedBooks=this.likeService.getLikes(p);
            return new ResponseEntity<>(likedBooks,HttpStatus.OK);
        } catch (PlayerNotFoundException e) {
            return new ResponseEntity<>("Player not found", HttpStatus.NOT_FOUND);
        }
    }

}


