package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.backend.data.Friend;
import project.backend.data.FriendID;
import project.backend.data.Player;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<Friend, FriendID> {

    @Query("SELECT f.sender FROM Friend f WHERE f.receiver.username = :username AND f.requestState = 'sent' ORDER BY f.sender.username")
    List<Player> getPendingRequests(String username);

    @Query("SELECT p FROM Friend f, Player p " +
           "WHERE ((f.receiver.username = p.username AND f.sender.username = :username) OR " +
                    "(f.receiver.username = :username AND f.sender.username = p.username)) " +
                    "AND f.requestState = 'accepted'"+
           "ORDER BY p.username")
    List<Player> getFriends(String username);

    @Query("SELECT f FROM Friend f WHERE (f.sender.username = :username AND f.receiver.username = :player) OR (f.receiver.username = :username AND f.sender.username = :player)")
    Optional<Friend> findRow (String username, String player);
}
