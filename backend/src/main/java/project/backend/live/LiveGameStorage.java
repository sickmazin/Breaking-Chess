package project.backend.live;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Component;
import project.backend.data.Game;
import project.backend.exceptions.LiveGameNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LiveGameStorage {

    private final Map<String, LiveGame> liveGames = new HashMap<String, LiveGame>();

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public LiveGame getLiveGame(String gameId) throws LiveGameNotFoundException {
        LiveGame liveGame = liveGames.get(gameId);
        if (liveGame==null) throw new LiveGameNotFoundException();
        return liveGame;
    }

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public void setLiveGame(LiveGame liveGame) {
        liveGames.put(liveGame.getId(), liveGame);
    }

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public LiveGame getLiveGameByPlayer(String nickname) throws LiveGameNotFoundException {
        List<LiveGame> games =  liveGames.values()
                                         .stream()
                                         .filter(game -> game.getBlackPlayer().equals(nickname) | game.getWhitePlayer().equals(nickname))
                                         .toList();

        if (games.isEmpty()) throw new LiveGameNotFoundException();
        return games.get(0);
    }

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public List<LiveGame> getGamesByModeAndState(Game.TYPE type, LiveGame.GameState state) {
        return liveGames.values()
                        .stream()
                        .filter(g -> g.getType()==type && g.getGameState()==state)
                        .toList();
    }

    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    public void removeLiveGame(String gameId) {
        liveGames.remove(gameId);
    }


    public void removeEndedGames () {
        liveGames.entrySet().removeIf(g -> g.getValue().getGameState() == LiveGame.GameState.ENDED);
    }
}
