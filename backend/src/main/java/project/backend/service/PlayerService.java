package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.data.Game;
import project.backend.data.Player;
import project.backend.data.Statistic;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.FriendRepository;
import project.backend.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final FriendRepository friendRepository;


    @Autowired
    public PlayerService (PlayerRepository playerRepository, FriendRepository friendRepository) {
        this.playerRepository = playerRepository;
        this.friendRepository = friendRepository;
    }


    public Player getPlayer(String nickname) throws PlayerNotFoundException {
        Optional<Player> player = playerRepository.findById(nickname);
        if (player.isPresent()) return player.get();
        else throw new PlayerNotFoundException();
    }

    @Transactional
    public ResponseEntity<?> getLeaderboard(String modality) {
        try {
            List<Player> leaderboard = switch (modality) {
                case "BLITZ" -> playerRepository.getBlitzLead().orElseThrow(() -> new RuntimeException("Error during db search for leaderboard"));
                case "BULLET" -> playerRepository.getBulletLead().orElseThrow(() -> new RuntimeException("Error during db search for leaderboard"));
                case "RAPID" -> playerRepository.getRapidLead().orElseThrow(() -> new RuntimeException("Error during db search for leaderboard"));
                default -> new ArrayList<Player>();
            };
            if(leaderboard.isEmpty()) {
                return ResponseEntity.noContent().build();
            }else {
                return new ResponseEntity<>(leaderboard, HttpStatus.OK);
            }
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    public ResponseEntity<?> changeFirstName (String username, String firstName) {
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            player.setFirstName(firstName);
            playerRepository.save(player);
            Player playerToReturn = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            return new ResponseEntity<>(playerToReturn,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
    @Transactional
    public ResponseEntity<?> changeLastName (String username, String lastName) {
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            player.setLastName(lastName);
            var playerToReturn=playerRepository.save(player);
            return new ResponseEntity<>(playerToReturn,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> changeNickname (String username, String nickname) {
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            player.setUsername(nickname);
            var playerToReturn=playerRepository.save(player);
            return new ResponseEntity<>(playerToReturn,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DataAccessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> changeEmail (String username, String email) {
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            player.setEmail(email);
            var playerToReturn=playerRepository.save(player);
            return new ResponseEntity<>(playerToReturn,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DataAccessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @Transactional
    public ResponseEntity<?> changeAvatar (String username, String avatar) {
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            player.setAvatar(avatar);
            var playerToReturn=playerRepository.save(player);
            return new ResponseEntity<>(playerToReturn,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (DataAccessException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<?> statisticGamesForModality(String username,Game.TYPE modality){
        try {
            Player player = playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: " + username));
            Statistic stat=Statistic.builder()
                    .modality(modality.name().toLowerCase())
                    .win(playerRepository.getAllWinForThisModality(username, modality))
                    .lose(playerRepository.getAllLossesForThisModality(username, modality))
                    .draw(playerRepository.getAllDrawsForThisModality(username, modality))
                    .build();
            return new ResponseEntity<>(stat,HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> findPlayerByUsername (String username) {
        try {
            Player player= playerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Player non trovato con questo username: "+username));
            return new ResponseEntity<>(player, HttpStatus.OK);
        }catch (UsernameNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



//    public Optional<List<Player>> findFriendsByUsername(String username) {
//        return playerRepository.findFriendsFor(username);
//    }
}
