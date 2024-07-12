package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.data.Book;
import project.backend.data.Like;
import project.backend.data.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Integer> {
    @Query("select l from Like l where l.book= :book and l.player = :player")
    Optional<Like> findByBookAndPlayer(Book book, Player player);

    @Query("select l.book from Like l where l.player = :player")
    List<Book> findAllBookLikedByPlayer (Player player);
}
