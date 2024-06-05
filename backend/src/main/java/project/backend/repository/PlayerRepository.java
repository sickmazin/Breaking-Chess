package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.data.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, String> {
}
