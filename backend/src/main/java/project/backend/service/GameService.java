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

    //@Transactional
    public Game generateNewGameFromPlayer(Player playerWhoWantToPlay, String modality) {
        return null; //TODO
    }

    //@Transactional
    public Game generateNewGameForGuest(String modality) {
        return null;//TODO
    }

    //@Transactional
    public List<Game> getListOfGames(String playerNickname) {
        //CHECK SE IL PLAYER CHE L'HA RICHIESTO C'è NEL DB
        Player player= playerRepository.findByUsername(playerNickname).orElseThrow( () -> new IllegalArgumentException(" PLAYER NON PRESENTE NEL DB"));
        return gameRepository.findLast20ByPlayer(player.getUsername());
    }

    public Game getGameByID(long id) {
        return gameRepository.getById(id);
    }

    //@Transactional
    public void deleteGameByID(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    //@Transactional
    public Game generateNewGameVsFriends(Player playerWhoWantToPlay, Player playerFriend, String modality) {
        return null;//TODO
    }
}
