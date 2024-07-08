package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.data.Game;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query( "SELECT g " +
            "FROM Game g " +
            "WHERE (g.whitePlayer.username = :username OR g.blackPlayer.username = :username)" +
            "ORDER BY g.date DESC LIMIT 20")
    List<Game> findLast20ByPlayer(String username);
}