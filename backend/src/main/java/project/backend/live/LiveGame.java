package project.backend.live;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.keycloak.common.util.Time;
import project.backend.data.Game;

import java.util.ArrayList;
import java.util.List;

@Data
public class LiveGame {
    @Setter(AccessLevel.NONE)
    private String id;
    @Setter(AccessLevel.NONE)
    private String whitePlayer = "";
    @Setter(AccessLevel.NONE)
    private String blackPlayer = "";
    private Game.TYPE type;
    @Setter(AccessLevel.NONE)
    private String turn;
    @Setter(AccessLevel.NONE)
    private GameState gameState;
    private String result;
    private List<String> FENs = new ArrayList<>(20);
    private long timeBeforeMoveMillis;
    private long wRemainingTime;
    private long bRemainingTime;
    private boolean whiteAsked;
    private boolean blackAsked;

    public enum GameState {
        FINDING_OPPONENT,
        STARTED,
        ENDED
    }

    public LiveGame(String player1, Game.TYPE type) {
        this.type = type;
        double value = Math.round(Math.random());

        if (value == 0) {
            whitePlayer = player1;
            turn = player1;
        }
        else {
            blackPlayer = player1;
        }

        this.gameState = GameState.FINDING_OPPONENT;
        this.wRemainingTime = (long) type.minutes *60*1000;
        this.bRemainingTime = (long) type.minutes *60*1000;
        this.id = java.util.UUID.randomUUID().toString();
        this.timeBeforeMoveMillis = Time.currentTimeMillis();
    }

    public void setRemainingPlayer(String player) {
        if (whitePlayer.isBlank()) {
            whitePlayer = player;
            turn = player;
        }
        else {
            blackPlayer = player;
        }
        gameState = GameState.STARTED;
    }

    public void switchTurn() {
        turn = turn.equals(blackPlayer) ? whitePlayer : blackPlayer;
    }
    public void addFEN(String fen) {
        this.FENs.add(fen);
    }

    public void setResult (String result) {
        this.gameState = GameState.ENDED;
        this.result = result;
    }
}
