package project.backend.live;

import org.springframework.stereotype.Component;
import project.chessbackend.data.Game;
import project.chessbackend.exception.LiveGameNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LiveGameStorage {

    //Lock su singolo LiveGame

    private final Map<String, LiveGame> liveGames = new HashMap<String, LiveGame>();

    public LiveGame getLiveGame(String gameId) throws LiveGameNotFoundException {
        LiveGame liveGame = liveGames.get(gameId);
        if (liveGame==null) throw new LiveGameNotFoundException();
        return liveGame;
    }

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

    public List<LiveGame> getGamesByModeAndState(Game.TYPE type, GameState findingOpponent) {
        return liveGames.values()
                        .stream()
                        .filter(g -> g.getType()==type && g.getGameState()==findingOpponent)
                        .toList();
    }

    public void removeLiveGame(String gameId) {
        liveGames.remove(gameId);
    }


//    public LiveGame getLiveGameByPlayer(String nickname) throws LiveGameNotFoundException {
//        LiveGame liveGame = liveGames.get(gameId);
//        if (liveGame==null) throw new LiveGameNotFoundException();
//        return liveGame;
//    }
}
