package project.backend.data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class FriendID implements Serializable {

    @Column(name = "sender")
    String sender;
    @Column(name = "receiver")
    String receiver;
}
