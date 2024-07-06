package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.data.Game;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query( "SELECT g " +
            "FROM Game g,Player p " +
            "WHERE (g.whitePlayer.username=p.username  OR g.blackPlayer.username = p.username)" +
            "AND p.username LIKE ?1" +
            "ORDER BY g.date DESC LIMIT 20")
    List<Game> findLast20ByPlayer(String nickname);
}