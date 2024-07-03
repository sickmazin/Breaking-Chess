package project.backend.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @NonNull
    @Enumerated(EnumType.STRING)
    private TYPE type;

    public enum TYPE {
        BULLET,
        BLITZ,
        RAPID
    }
    public enum RESULT {
        BLACK,
        DRAW,
        WHITE
    }

    @NonNull
    private String pgn;

    @NonNull
    @Enumerated(EnumType.STRING)
    private RESULT result;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "whiteNickname")
    private Player whitePlayer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blackNickname")
    private Player blackPlayer;

    @NonNull
    private LocalDateTime date;
}
