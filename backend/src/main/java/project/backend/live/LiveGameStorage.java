package project.backend.live;

import org.springframework.stereotype.Component;
import project.backend.data.Game;
import project.backend.exceptions.LiveGameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LiveGameStorage {

    private final Map<String, LiveGame> liveGames = new ConcurrentHashMap<>();


    public void setLiveGame(LiveGame liveGame) {
        liveGames.put(liveGame.getId(), liveGame);
    }

    public LiveGame getLiveGameByPlayer(String nickname) throws LiveGameNotFoundException {
        List<LiveGame> games =  liveGames.values()
                                         .stream()
                                         .filter(game -> game.getBlackPlayer().equals(nickname) | game.getWhitePlayer().equals(nickname))
                                         .toList();

        if (games.isEmpty()) throw new LiveGameNotFoundException();
        return games.get(0);
    }

    public List<LiveGame> getGamesByModeAndState(Game.TYPE type, LiveGame.GameState state) {
        return liveGames.values()
                        .stream()
                        .filter(g -> g.getType()==type && g.getGameState()==state)
                        .toList();
    }

    public void removeLiveGame(String gameId) {
        liveGames.remove(gameId);
    }


    public void removeEndedGames () {
        liveGames.entrySet().removeIf(g -> g.getValue().getGameState() == LiveGame.GameState.ENDED);
    }
}
