package project.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.backend.data.Friend;
import project.backend.data.FriendID;
import project.backend.data.Player;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.FriendRepository;

import java.util.List;
import java.util.Optional;

@Service
public class FriendService {

    private final FriendRepository friendRepository;
    private final PlayerService playerService;

    @Autowired
    public FriendService (FriendRepository friendRepository,  PlayerService playerService) {
        this.friendRepository = friendRepository;
        this.playerService = playerService;
    }

    @Transactional
    public List<Player> getFriends (String preferredUsername) {
        return friendRepository.getFriends(preferredUsername);
    }

    @Transactional
    public void blockRequest (String username, String player) {
        Optional<Friend> optional = friendRepository.findRow(username, player);
        optional.ifPresent(friendRepository::delete);
    }

    @Transactional
    public void acceptRequest (String username, String player) {
        Optional<Friend> optional = friendRepository.findRow(username, player);
        optional.ifPresent(req -> {
            if (req.getRequestState() != Friend.RequestState.accepted)
                req.setRequestState(Friend.RequestState.accepted);
        });
    }

    @Transactional
    public List<Player> getPendingRequests (String preferredUsername) {
        return friendRepository.getPendingRequests(preferredUsername);
    }

    @Transactional
    public void askRequest (String sender, String receiver) throws PlayerNotFoundException {
        Optional<Friend> optional = friendRepository.findRow(sender, receiver);
        if (optional.isPresent()) {
            Friend req = optional.get();
            if (req.getRequestState() != Friend.RequestState.accepted)
                req.setRequestState(Friend.RequestState.accepted);
        }
        else {
            friendRepository.save(
                    Friend.builder()
                            .friendID(new FriendID(sender,receiver))
                            .sender(playerService.getPlayer(sender))
                            .receiver(playerService.getPlayer(receiver))
                            .requestState(Friend.RequestState.sent)
                            .build()
            );
        }
    }
}
