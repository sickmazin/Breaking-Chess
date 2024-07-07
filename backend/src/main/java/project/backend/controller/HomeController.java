package project.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.backend.data.Book;
import project.backend.data.Player;
import project.backend.service.BookService;
import project.backend.service.PlayerService;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final BookService bookService;
    private final PlayerService playerService;

    @Autowired
    public HomeController(BookService bookService, PlayerService playerService) {
        this.bookService = bookService;
        this.playerService = playerService;
    }

    //TO TEST
    @GetMapping("/books")
    public ResponseEntity<?> books() {
        List<Book> books = bookService.findAll();
        if(books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    //TO TEST
    @GetMapping(value = "/leaderboard/{modality}")
    public ResponseEntity<?> leaderboard(@PathVariable("modality") String modality) {
        try {
            List<Player> leaderboard = playerService.getLeaderboard(modality).orElseThrow(() -> new RuntimeException("Error during db search for leaderboard"));
            if(leaderboard.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return new ResponseEntity<>(leaderboard, HttpStatus.OK);
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //TO TEST
//    @GetMapping(value = "/friends")
//    public ResponseEntity<?> friends(@AuthenticationPrincipal Principal principal) {
//        List<Player> friends = playerService.findFriendsByUsername(principal.getName()).orElse(null);
//        if (friends==null) return ResponseEntity.noContent().build();
//        return ResponseEntity.ok().body(friends);
//    }
}
