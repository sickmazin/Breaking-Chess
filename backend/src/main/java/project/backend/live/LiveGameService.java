package project.backend.live;

import jakarta.transaction.Transactional;
import org.keycloak.common.util.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import project.backend.bitboard.Board;
import project.backend.bitboard.Constant;
import project.backend.data.Game;
import project.backend.data.Player;
import project.backend.exceptions.CannotAbortException;
import project.backend.exceptions.GameException;
import project.backend.exceptions.LiveGameNotFoundException;
import project.backend.exceptions.PlayerNotFoundException;
import project.backend.repository.GameRepository;
import project.backend.service.GameService;
import project.backend.service.PlayerService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class LiveGameService {
    private final static int DEFAULT_ELO_SHIFT = 10;
    private final static float FACTOR = 0.03f;

    private final GameRepository gameRepository;
    private final LiveGameStorage liveGameStorage;
    private final PlayerService playerService;
    private final GameService gameService;

    @Autowired
    public LiveGameService(LiveGameStorage liveGameStorage, PlayerService playerService, GameService gameService, GameRepository gameRepository) {
        this.liveGameStorage = liveGameStorage;
        this.playerService = playerService;
        this.gameService = gameService;
        this.gameRepository = gameRepository;
    }

    @Transactional(rollbackOn = Exception.class)
    public Optional<LiveGameDTO> getGame(String nickname) throws PlayerNotFoundException, GameException {
        try {
            LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
            if (liveGame.getGameState() == LiveGame.GameState.ENDED)
                return Optional.of(createLiveGameDTO(liveGame, nickname));

            // catch not yet started game
            if (liveGame.getTurn() == null) {
                return Optional.empty();
            }
            boolean isWhite = liveGame.getTurn().equals(liveGame.getWhitePlayer());
            long timeRemaining = isWhite? liveGame.getBRemainingTime() : liveGame.getWRemainingTime();
            long timePassed = Time.currentTimeMillis() - liveGame.getTimeBeforeMoveMillis();
            if (timePassed >= timeRemaining) {
                return Optional.of(endGame(liveGame.getTurn(), false));
            }
            else return Optional.of(createLiveGameDTO(liveGame, nickname));

        } catch (LiveGameNotFoundException e) {
            return Optional.empty();
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public LiveGameDTO makeMove(String player, String move) throws GameException, LiveGameNotFoundException, PlayerNotFoundException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(player);
        LiveGameDTO liveGameDTO = null;

        if (player.equals(liveGame.getTurn())) {

            boolean isWhite = player.equals(liveGame.getWhitePlayer());
            long timeRemaining = isWhite? liveGame.getBRemainingTime() : liveGame.getWRemainingTime();
            long timePassed = Time.currentTimeMillis() - liveGame.getTimeBeforeMoveMillis();

            if (timePassed >= timeRemaining) {
                endGame(player, false);
            }
            else {
                if (isWhite) liveGame.setWRemainingTime(timeRemaining-timePassed);
                else liveGame.setBRemainingTime(timeRemaining-timePassed);
            }

            List<String> list = liveGame.getFENs();
            Board board = new Board(list);

            if (board.getMoves().contains(move) && board.makeMove(move)) {
                liveGame.addFEN(board.getFEN());
                liveGame.switchTurn();
                if (board.isCheckMate()) liveGameDTO = endGame(liveGame.getTurn(), false);
                else if (board.isDraw()) liveGameDTO = endGame(liveGame.getTurn(), true);

                else liveGame.setTimeBeforeMoveMillis(Time.currentTimeMillis());
            }
            else throw new GameException(GameException.TYPE.INVALID_MOVE);
        } else throw new GameException(GameException.TYPE.INVALID_MOVE);

        if (liveGameDTO==null) return createLiveGameDTO(liveGame, player);
        System.out.println("Check Result:"+liveGameDTO);
        return liveGameDTO;
    }

    @Transactional(rollbackOn = Exception.class)
    public Optional<LiveGameDTO> startGame(String nickname, String mode) {
        try {
            //check if player has already started a game
            return Optional.of(createLiveGameDTO(liveGameStorage.getLiveGameByPlayer(nickname), nickname));
        } catch (LiveGameNotFoundException e) {
            //check if player can connect to already existing NEW game or push new LiveGame in the queue
            List<LiveGame> list = liveGameStorage.getGamesByModeAndState(Game.TYPE.valueOf(mode), LiveGame.GameState.FINDING_OPPONENT);

            if (list.isEmpty()) {
                LiveGame liveGame = new LiveGame(nickname, Game.TYPE.valueOf(mode));
                liveGameStorage.setLiveGame(liveGame);
                return Optional.empty();
            }
            else {
                LiveGame liveGame = list.get(0);
                liveGame.setRemainingPlayer(nickname);
                liveGame.addFEN(Constant.STARTING_POSITION);
                return Optional.of(createLiveGameDTO(liveGame, nickname));
            }
        }
    }

    @Transactional(rollbackOn = Exception.class)
    public LiveGameDTO endGame(String nickname, boolean isDraw) throws LiveGameNotFoundException, PlayerNotFoundException, GameException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);

        //should not happen
        if (liveGame.getGameState() == LiveGame.GameState.FINDING_OPPONENT || liveGame.getGameState() == LiveGame.GameState.ENDED)
            throw new GameException(GameException.TYPE.GAME_NOT_STARTED_OR_ENDED);

        //setup db game
        Game game = new Game();
        game.setWhitePlayer(playerService.getPlayer(liveGame.getWhitePlayer()));
        game.setBlackPlayer(playerService.getPlayer(liveGame.getBlackPlayer()));
        game.setMode(liveGame.getType());
        if (isDraw) game.setResult(Game.RESULT.draw);
        else
            game.setResult(liveGame.getWhitePlayer().equals(nickname)?
                                    Game.RESULT.black :
                                    Game.RESULT.white);
        game.setDate(LocalDateTime.now());
        gameService.save(game);

        //update elo points and liveGame result
        updatePoints(game);
        liveGame.setResult(game.getResult().name());

        //create and return liveGameDTO
        return createLiveGameDTO(liveGame, nickname);
    }

    private void updatePoints(Game game) throws PlayerNotFoundException {
        Player whitePlayer = game.getWhitePlayer();
        Player blackPlayer = game.getBlackPlayer();

        int initialShift = 0;
        switch (game.getResult()) {
            case white -> initialShift = -DEFAULT_ELO_SHIFT;
            case black -> initialShift = DEFAULT_ELO_SHIFT;
            default -> {}
        }

        double realShift;
        switch (game.getMode()) {
            case BULLET -> {
                realShift = initialShift + FACTOR * (whitePlayer.getBulletPoints() - blackPlayer.getBulletPoints());
                whitePlayer.setBulletPoints((short) (whitePlayer.getBulletPoints()-realShift));
                blackPlayer.setBulletPoints((short) (blackPlayer.getBulletPoints()+realShift));
            }
            case BLITZ -> {
                realShift = initialShift + FACTOR * (whitePlayer.getBlitzPoints() - blackPlayer.getBlitzPoints());
                whitePlayer.setBlitzPoints((short) (whitePlayer.getBlitzPoints()-realShift));
                blackPlayer.setBlitzPoints((short) (blackPlayer.getBlitzPoints()+realShift));
            }
            default -> {
                realShift = initialShift + FACTOR * (whitePlayer.getRapidPoints() - blackPlayer.getRapidPoints());
                whitePlayer.setRapidPoints((short) (whitePlayer.getRapidPoints()-realShift));
                blackPlayer.setRapidPoints((short) (blackPlayer.getRapidPoints()+realShift));
            }
        }

    }

    @Transactional(rollbackOn = Exception.class)
    public void abortGame(String nickname) throws LiveGameNotFoundException, CannotAbortException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        if (liveGame.getGameState() == LiveGame.GameState.FINDING_OPPONENT) {
            liveGameStorage.removeLiveGame(liveGame.getId());
        }
        else throw new CannotAbortException();
    }

    private LiveGameDTO createLiveGameDTO(LiveGame liveGame, String nickname) {
        LiveGameDTO result = new LiveGameDTO();

        result.setTurn(liveGame.getTurn());
        result.setId(liveGame.getId());
        result.setType(liveGame.getType());
        result.setWhitePlayer(liveGame.getWhitePlayer());
        result.setBlackPlayer(liveGame.getBlackPlayer());
        result.setWhiteTime(liveGame.getWRemainingTime());
        result.setBlackTime(liveGame.getBRemainingTime());
        result.setFENs(liveGame.getFENs());
        result.setDrawRequest(nickname.equals(liveGame.getTurn())?
                liveGame.isBlackAsked() : liveGame.isWhiteAsked());
        result.setResult(liveGame.getResult());
        return result;
    }

    @Transactional(rollbackOn = Exception.class)
    public Optional<LiveGameDTO> drawRequest(String nickname) throws LiveGameNotFoundException, PlayerNotFoundException, GameException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        LiveGameDTO liveGameDTO = null;

        if (nickname.equals(liveGame.getWhitePlayer())) {
            if (liveGame.isBlackAsked())
                liveGameDTO = endGame(nickname, true);
            else liveGame.setWhiteAsked(true);
        }
        else {
            if (liveGame.isWhiteAsked())
                liveGameDTO = endGame(nickname, true);
            else liveGame.setBlackAsked(true);
        }
        if (liveGameDTO == null) return Optional.empty();
        return Optional.of(liveGameDTO);
    }

    @Transactional(rollbackOn = Exception.class)
    public void denyRequest(String nickname) throws LiveGameNotFoundException {
        LiveGame liveGame = liveGameStorage.getLiveGameByPlayer(nickname);
        liveGame.setWhiteAsked(false);
        liveGame.setBlackAsked(false);
    }


    public List<Game> getGames(String playerNickname) throws PlayerNotFoundException {
        // check if player is present
        Player player= playerService.getPlayer(playerNickname);
        return gameRepository.findLast20ByPlayer(player.getUsername());
    }


    @Scheduled(fixedRate = 3000)
    public void removeEndedGames() {
        liveGameStorage.removeEndedGames();
    }
}
