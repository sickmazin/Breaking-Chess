package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.backend.data.Book;
import project.backend.data.BookDTO;
import project.backend.data.Like;
import project.backend.data.Player;
import project.backend.exceptions.BookNotFoundException;
import project.backend.exceptions.LikeNotFoundException;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.BookRepository;
import project.backend.repository.LikeRepository;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final LikeRepository likeRepository;
    private final PlayerService playerService;
    private final LikeService likeService;

    @Autowired
    public BookService (BookRepository bookRepository, LikeRepository likeRepository, PlayerService playerService, LikeService likeService) {
        this.bookRepository = bookRepository;
        this.likeRepository = likeRepository;
        this.playerService = playerService;
        this.likeService = likeService;
    }

    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    public ResponseEntity<?> findAll(String username) {
        Player p = null;
        try {
            p = playerService.getPlayer(username);
        } catch (PlayerNotFoundException e) {
            return new ResponseEntity<>("Player not found",HttpStatus.NOT_FOUND);
        }
        List<Book> books = bookRepository.findAll();
        List<Book> likedBooks=this.likeService.getLikes(p);
        List<BookDTO> returnedBooksDTO =new ArrayList<BookDTO>();
        for (Book book : books) {
            BookDTO bookDTO= createBookDTO(book);
            if(likedBooks.contains(book)) bookDTO.setLikedByThisPlayer(true);
            returnedBooksDTO.add(bookDTO);
        }
        if(books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(returnedBooksDTO, HttpStatus.OK);
    }

    private BookDTO createBookDTO (Book book) {
        BookDTO dto= new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setLink(book.getLink());
        dto.setLike(book.getLike());
        dto.setSrcImg(book.getSrcImg());
        return dto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> addLike(int bookID, Player player) {
        try {
            var book = this.bookRepository.findById(bookID).orElseThrow(BookNotFoundException::new);
            book.setLike(book.getLike() + 1);
            var bookToReturn = this.bookRepository.save(book);
            Like like= Like.builder()
                    .player(player)
                    .book(bookToReturn)
                    .build();
            likeRepository.save(like);
            return ResponseEntity.ok(bookToReturn);
        } catch (BookNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> deleteLike(int bookID, Player player) {
        try {
            var book = this.bookRepository.findById(bookID).orElseThrow(BookNotFoundException::new);
            book.setLike(book.getLike() - 1);
            Like like = likeRepository.findByBookAndPlayer(book,player).orElseThrow(LikeNotFoundException::new);
            likeRepository.delete(like);
            var bookToReturn = this.bookRepository.save(book);
            return ResponseEntity.ok(bookToReturn);
        } catch (BookNotFoundException e) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }catch (LikeNotFoundException e) {
            return new ResponseEntity<>("Like not found", HttpStatus.NOT_FOUND);
        }
    }

}
