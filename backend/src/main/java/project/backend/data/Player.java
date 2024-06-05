package project.backend.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "Player")
public class Player{
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

//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @Lob // represented as BLOB (binary data).
//    @Basic(fetch = FetchType.LAZY) // caricata quando serve
//    private byte[] profilePicture;
//
//    @ManyToMany(fetch = FetchType.LAZY) // caricata quando serve
//    private List<Player> friends;

    //@OneToMany
    //private List<Game> matches;

}

/*
*  To create an image from bytes
public Image generateImage(User user) {
    Long id = user.getId();
    StreamResource sr = new StreamResource("user", () ->  {
        User attached = userRepository.findWithPropertyPictureAttachedById(id);
        return new ByteArrayInputStream(attached.getProfilePicture());
    });
    sr.setContentType("image/png");
    Image image = new Image(sr, "profile-picture");
    return image;
}
*
* */