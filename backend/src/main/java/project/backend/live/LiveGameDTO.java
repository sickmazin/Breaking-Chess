package project.backend.live;

import lombok.Data;
import project.backend.data.Game;

import java.util.ArrayList;
import java.util.List;

@Data
public class LiveGameDTO {
    private String id;
    private String whitePlayer;
    private String blackPlayer;
    private String turn;
    private Game.TYPE type;
    private List<String> FENs = new ArrayList<>(20);
    private String result;
    private long whiteTime;
    private long blackTime;
    private boolean drawRequest;
}
