package project.backend.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "Player")
public class Player {
    @Id
    @Column(name = "username",unique = true, nullable = false,length = 20)
    private String username;

    private String firstName;

    private String lastName;

    @Column(name = "email",unique = true, nullable = false)
    private String email;

    @Column(name = "bulletPoints",nullable = false)
    private short bulletPoints;
    @Column(name = "blitzPoints",nullable = false)
    private short blitzPoints;
    @Column(name = "rapidPoints",nullable = false)
    private short rapidPoints;
    private String avatar;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sender", cascade = CascadeType.ALL) // caricata quando serve
    private Set<Friend> senderFriends;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "receiver", cascade = CascadeType.ALL) // caricata quando serve
    private Set<Friend> receiverFriends;
}