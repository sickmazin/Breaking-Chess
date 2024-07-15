package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.backend.data.Game;
import project.backend.data.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
    Optional<Player> findByUsername(String username);

    @Query("SELECT p FROM Player p ORDER BY p.blitzPoints DESC LIMIT 20")
    Optional<List<Player>> getBlitzLead();

    @Query("SELECT p FROM Player p ORDER BY p.bulletPoints DESC LIMIT 20")
    Optional<List<Player>> getBulletLead();

    @Query("SELECT p FROM Player p ORDER BY p.rapidPoints DESC LIMIT 20")
    Optional<List<Player>> getRapidLead();

    @Query("SELECT COUNT(g) FROM Game g WHERE (g.whitePlayer.username = :username AND g.result = 'WHITE' AND g.type = :type) OR (g.blackPlayer.username = :username AND g.result = 'BLACK' AND g.type = :type)")
    int getAllWinForThisModality(String username,Game.TYPE type);

    @Query("SELECT COUNT(g) FROM Game g WHERE (g.whitePlayer.username = :username AND g.result = 'BLACK' AND g.type = :type) OR (g.blackPlayer.username = :username AND g.result = 'WHITE' AND g.type = :type)")
    int getAllLossesForThisModality( String username, Game.TYPE type);

    @Query("SELECT COUNT(g) FROM Game g WHERE (g.whitePlayer.username = :username OR g.blackPlayer.username = :username) AND g.result = 'DRAW' AND g.type = :type")
    int getAllDrawsForThisModality(String username,Game.TYPE type);

//    @Query("SELECT p. FROM Player p,Pla")
//    Optional<List<Player>> findFriendsFor(String username);
}
