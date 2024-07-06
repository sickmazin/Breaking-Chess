package project.backend.live;

import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
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
    @Setter(AccessLevel.NONE)
    private String whitePlayerAddress;
    @Setter(AccessLevel.NONE)
    private String blackPlayerAddress;
    @Setter(AccessLevel.NONE)
    @Version
    private Long version;
    private List<String> FENs = new ArrayList<>(20);


    public LiveGame(String player1, String address) {
        double value = Math.round(Math.random());

        if (value == 0) {
            whitePlayer = player1;
            whitePlayerAddress = address;
            turn = player1;
        }
        else {
            blackPlayer = player1;
            blackPlayerAddress = address;
        }

        this.gameState = GameState.FINDING_OPPONENT;
        this.id = java.util.UUID.randomUUID().toString();
    }

    public void setRemainingPlayer(String player, String remoteAddr) {
        if (whitePlayer == null) {
            whitePlayer = player;
            whitePlayerAddress = remoteAddr;
            turn = player;
        }
        else if (blackPlayer == null) {
            blackPlayer = player;
            blackPlayerAddress = remoteAddr;
        }
        gameState = GameState.STARTED;
    }

    public void switchTurn() {
        turn = turn.equals(blackPlayer) ? whitePlayer : blackPlayer;
    }
}
