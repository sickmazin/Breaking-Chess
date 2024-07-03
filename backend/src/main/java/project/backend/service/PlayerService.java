package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.backend.data.Player;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.PlayerRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    //private final HashSet<String> guests;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, HashSet<String> guests) {
        //this.guests = guests;
        this.playerRepository = playerRepository;
    }

    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }

    public Player getPlayer(String nickname) throws PlayerNotFoundException {
        Optional<Player> player = playerRepository.findById(nickname);
        if (player.isPresent()) return player.get();

        else throw new PlayerNotFoundException();
    }

    /* public boolean isGuest(String nickname) {
        return guests.contains(nickname);
    } */
}
