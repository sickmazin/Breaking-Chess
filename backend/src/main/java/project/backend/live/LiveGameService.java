package project.backend.live;

import jakarta.persistence.LockModeType;
import org.keycloak.common.util.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import project.backend.bitboard.Board;
import project.backend.bitboard.Constant;
import project.backend.data.Game;
import project.backend.exceptions.CannotAbortException;
import project.backend.exceptions.GameException;
import project.backend.exceptions.LiveGameNotFoundException;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.service.GameService;
import project.backend.service.PlayerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

        //testing
        LiveGame liveGame = new LiveGame("first", Game.TYPE.valueOf("BULLET"));
        liveGame.setRemainingPlayer("second");
        liveGame.setFENs(List.of(Constant.STARTING_POSITION));
        liveGameStorage.setLiveGame(liveGame);
    }

    public Optional<LiveGameDTO> getGame(String nickname) {
        try {
            return Optional.of(createLiveGameDTO(liveGameStorage.getLiveGameByPlayer(nickname)));
        } catch (LiveGameNotFoundException e) {
            return Optional.empty();
        }
    }


    //TODO da vedere se il lock è corretto

    @Lock(LockModeType.OPTIMISTIC)
    public LiveGameDTO makeMove(String gameId, String move, String player) throws GameException, LiveGameNotFoundException, PlayerNotFoundException {
        LiveGame liveGame = liveGameStorage.getLiveGame(gameId);

        if (player.equals(liveGame.getTurn())) {
            long timePassed = Time.currentTimeMillis() -
                    ((liveGame.getBlackPlayer()).equals(player)? liveGame.getBTimeBeforeMoveMillis(): liveGame.getWTimeBeforeMoveMillis());
            long timeRemaining = liveGame.getBlackPlayer().equals(player)? liveGame.getBRemainingTime() : liveGame.getWRemainingTime();
            if (timePassed >= timeRemaining) {
                endGame(player, false);
            }

            List<String> list = liveGame.getFENs();
            Board board = new Board(list);

            if (board.getMoves().contains(move) && board.makeMove(move)) {
                liveGame.getFENs().add(board.getFENtrim());
                liveGame.switchTurn();
                if (board.isCheckMate()) endGame(liveGame.getTurn(), false);
                else if (board.isDraw()) endGame(liveGame.getTurn(), true);
            }
            else throw new GameException(GameException.TYPE.INVALID_MOVE);
        } else throw new GameException(GameException.TYPE.INVALID_MOVE);

        return createLiveGameDTO(liveGame);
    }

    public Optional<LiveGameDTO> startGame(String nickname, String mode) throws PlayerNotFoundException {
        try {
//            if (!playerService.isGuest(nickname)) {
//                //check for player presence if not guest
//                playerService.getPlayer(nickname);
//            }
            //check if player has already started a game
            return Optional.of(createLiveGameDTO(liveGameStorage.getLiveGameByPlayer(nickname)));

        } catch (LiveGameNotFoundException e) {
            //check if player can connect to already existing NEW game or push new LiveGame in the queue
            List<LiveGame> list = liveGameStorage.getGamesByModeAndState(Game.TYPE.valueOf(mode), GameState.FINDING_OPPONENT);

            if (list.isEmpty()) {
                LiveGame liveGame = new LiveGame(nickname, Game.TYPE.valueOf(mode));
                liveGameStorage.setLiveGame(liveGame);
                return Optional.empty();
            }
            else {
                LiveGame liveGame = list.get(0);
                liveGame.setRemainingPlayer(nickname);
                liveGame.setFENs(List.of(Constant.STARTING_POSITION));
                return Optional.of(createLiveGameDTO(liveGame));
            }
        }
    }

    public LiveGameDTO endGame(String nickname, boolean isDraw) throws LiveGameNotFoundException, PlayerNotFoundException, GameException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        //should not happen
        if (liveGame.getGameState() == GameState.FINDING_OPPONENT) {
            throw new GameException(GameException.TYPE.GAME_NOT_STARTED);
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

        LiveGameDTO liveGameDTO = createLiveGameDTO(liveGame);
        if (isDraw) liveGameDTO.setResult("draw");
        else liveGameDTO.setResult(liveGame.getWhitePlayer().equals(nickname)?
                "black" :
                "white");                   //set result
        return liveGameDTO;
    }


    public void abortGame(String nickname) throws LiveGameNotFoundException, CannotAbortException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        if (liveGame.getGameState() == GameState.FINDING_OPPONENT) {
            liveGameStorage.removeLiveGame(liveGame.getId());
        }
        else throw new CannotAbortException();
    }


    private LiveGameDTO createLiveGameDTO(LiveGame liveGame) {
        LiveGameDTO result = new LiveGameDTO();
        result.setTurn(liveGame.getTurn());
        result.setId(liveGame.getId());
        result.setWhitePlayer(liveGame.getWhitePlayer());
        result.setBlackPlayer(liveGame.getBlackPlayer());
        result.setFENs(liveGame.getFENs());
        return result;
    }
}
