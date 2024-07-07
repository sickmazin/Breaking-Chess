package project.backend.service;

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

    public Optional<List<Player>> getLeaderboard(String modality) {
        return switch (modality) {
            case "blitz" -> playerRepository.getBlitzLead();
            case "bullet" -> playerRepository.getBulletLead();
            case "rapid" -> playerRepository.getRapidLead();
            default -> Optional.empty();
        };
    }

//    public Optional<List<Player>> findFriendsByUsername(String username) {
//        return playerRepository.findFriendsFor(username);
//    }
}
