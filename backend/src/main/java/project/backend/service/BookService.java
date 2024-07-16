package project.backend.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import project.backend.data.Book;
import project.backend.data.Like;
import project.backend.data.Player;
import project.backend.exceptions.BookNotFoundException;
import project.backend.exceptions.LikeNotFoundException;
import project.backend.repository.BookRepository;
import project.backend.repository.LikeRepository;

import java.util.List;

@Transactional
@AllArgsConstructor
@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LikeRepository likeRepository;

    public ResponseEntity<?> findAll() {
        List<Book> books = bookRepository.findAll();
        if(books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<?> addLike(int bookID, Player player) {
        try {
            var book = this.bookRepository.findById(bookID).orElseThrow(BookNotFoundException::new);
            book.setLike(book.getLike() + 1);
            var bookToReturn = this.bookRepository.save(book);
            Like like= Like.builder()
                    .player(player)
                    .book(book)
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
