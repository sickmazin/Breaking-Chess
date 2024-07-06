package project.backend.live;

import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import project.backend.bitboard.Board;
import project.backend.bitboard.Constant;
import project.backend.data.Game;
import project.backend.exceptions.GameException;
import project.backend.exceptions.LiveGameNotFoundException;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.service.GameService;
import project.backend.service.PlayerService;

import java.time.LocalDateTime;
import java.util.List;

//TODO make methods transactions

@Service
public class LiveGameService {
    private LiveGameStorage liveGameStorage;
    private PlayerService playerService;
    private GameService gameService;

    @Autowired
    public LiveGameService(LiveGameStorage liveGameStorage, PlayerService playerService, GameService gameService) {
        this.liveGameStorage = liveGameStorage;
        this.playerService = playerService;
        this.gameService = gameService;
    }

    //TODO da vedere se il lock è corretto

    @Lock(LockModeType.OPTIMISTIC)
    public String makeMove(String gameId, String move, String player) throws GameException, LiveGameNotFoundException {
        LiveGame liveGame = liveGameStorage.getLiveGame(gameId);


        if (player.equals(liveGame.getTurn())) {
            Board board = new Board(liveGame.getFENs().getLast());

            if (board.getMoves().contains(move) && board.makeMove(move)) {
                liveGame.switchTurn();
                return "moved";
            }
            else throw new GameException(GameException.TYPE.INVALID_MOVE);
        } else throw new GameException(GameException.TYPE.INVALID_MOVE);
    }

    public void startGame(String nickname, String mode, String remoteAddr) throws PlayerNotFoundException {
        try {
//            if (!playerService.isGuest(nickname)) {
//                //check for player presence if not guest
//                playerService.getPlayer(nickname);
//            }

            //check if player has already started a game
            liveGameStorage.getLiveGameByPlayer(nickname);

        } catch (LiveGameNotFoundException e) {
            //check if player can connect to already existing NEW game or push new LiveGame in the queue
            List<LiveGame> list = liveGameStorage.getGamesByModeAndState(Game.TYPE.valueOf(mode), GameState.FINDING_OPPONENT);

            if (list.isEmpty()) {
                LiveGame liveGame = new LiveGame(nickname, remoteAddr);
                liveGameStorage.setLiveGame(liveGame);
            }
            else {
                LiveGame liveGame = list.get(0);
                liveGame.setRemainingPlayer(nickname, remoteAddr);
                liveGame.setFENs(List.of(Constant.STARTING_POSITION));
            }
        }
    }

    public void endGame(String nickname, boolean isDraw) throws LiveGameNotFoundException, PlayerNotFoundException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        if (liveGame.getGameState() == GameState.FINDING_OPPONENT) {
            liveGameStorage.removeLiveGame(liveGame.getId());
        }
        else {
            Game game = new Game();
            game.setWhitePlayer(playerService.getPlayer(liveGame.getWhitePlayer()));
            game.setBlackPlayer(playerService.getPlayer(liveGame.getBlackPlayer()));
            game.setType(liveGame.getType());
            if (isDraw) game.setResult(Game.RESULT.DRAW);
            else
                game.setResult(liveGame.getWhitePlayer().equals(nickname)?
                                    Game.RESULT.BLACK :
                                    Game.RESULT.WHITE);
            //TODO set PGN
            game.setDate(LocalDateTime.now());
            gameService.save(game);
        }
    }
    //TODO check if players are !necessary! for livegame instances but only track nicknames
}
