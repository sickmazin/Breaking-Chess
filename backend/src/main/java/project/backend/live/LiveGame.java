package project.backend.live;

import jakarta.persistence.Version;
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
    private String whitePlayer;
    @Setter(AccessLevel.NONE)
    private String blackPlayer;
    private Game.TYPE type;
    @Setter(AccessLevel.NONE)
    private String turn;
    @Setter(AccessLevel.NONE)
    private GameState gameState;
    @Version
    private Long version;
    private List<String> FENs = new ArrayList<>(20);
    private long wTimeBeforeMoveMillis;
    private long bTimeBeforeMoveMillis;
    private long wRemainingTime;
    private long bRemainingTime;


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
        this.wRemainingTime = type.minutes;
        this.bRemainingTime = type.minutes;
        this.id = java.util.UUID.randomUUID().toString();
        this.wTimeBeforeMoveMillis = Time.currentTimeMillis();
        this.bTimeBeforeMoveMillis = wTimeBeforeMoveMillis;
    }

    public void setRemainingPlayer(String player) {
        if (whitePlayer == null) {
            whitePlayer = player;
            turn = player;
        }
        else if (blackPlayer == null) {
            blackPlayer = player;
        }
        gameState = GameState.STARTED;
    }

    public void switchTurn() {
        turn = turn.equals(blackPlayer) ? whitePlayer : blackPlayer;
    }
}
