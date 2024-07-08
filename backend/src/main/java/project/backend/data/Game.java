package project.backend.data;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import java.util.Date;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Table(name = "Game")
public class Game {
    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @NonNull
    @Enumerated(EnumType.STRING)

    private TYPE type;

    public enum TYPE {
        BULLET(2),
        BLITZ(5),
        RAPID(10);

        public final int minutes;

        TYPE(int time) {
            this.minutes = time;
        }
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
