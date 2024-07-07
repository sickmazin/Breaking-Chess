package project.backend.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
    private MODALITY modality;

    public enum MODALITY {
        BULLET,
        BLITZ,
        RAPID
    }

    @NonNull
    private String pgn;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "whiteNickname")
    private Player whitePlayer;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "blackNickname")
    private Player blackPlayer;

    @NonNull
    private Date date;
}
