package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.data.Player;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.PlayerRepository;
import java.util.HashSet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import project.backend.data.Player;
import project.backend.repository.PlayerRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;


    public Optional<Player> findByUsername(String username) {
        return  playerRepository.findByUsername(username);
    }
    
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayer(String nickname) throws PlayerNotFoundException {
        Optional<Player> player = playerRepository.findById(nickname);
        if (player.isPresent()) return player.get();
        else throw new PlayerNotFoundException();
    }

    public Optional<List<Player>> getLeaderboard(String modality) {
        return switch (modality) {
            case "BLITZ" -> playerRepository.getBlitzLead();
            case "BULLET" -> playerRepository.getBulletLead();
            case "RAPID" -> playerRepository.getRapidLead();
            default -> Optional.empty();
        };
    }

//    public Optional<List<Player>> findFriendsByUsername(String username) {
//        return playerRepository.findFriendsFor(username);
//    }
}
