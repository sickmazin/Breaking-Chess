package project.backend.data;

import jakarta.persistence.*;
import lombok.*;


@Data
@Entity
@Builder
@Table(name = "friend")
@NoArgsConstructor
@AllArgsConstructor
public class Friend {

    public enum RequestState {
        sent,
        accepted
    }

    @EmbeddedId
    private FriendID friendID;

    @ManyToOne
    @MapsId("sender")
    private Player sender;

    @ManyToOne
    @MapsId("receiver")
    private Player receiver;

    @NonNull
    @Enumerated(EnumType.STRING)
    private RequestState requestState;
}
