package project.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.backend.data.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

}
