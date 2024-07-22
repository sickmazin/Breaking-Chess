package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.data.Game;
import project.backend.data.Player;
import project.backend.repository.GameRepository;
import project.backend.repository.PlayerRepository;
import java.util.List;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;

    public void save(Game game) {
        gameRepository.save(game);
    }
}
