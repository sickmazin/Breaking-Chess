package project.backend.security.keycloak;

import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.catalina.User;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import project.backend.data.Player;
import project.backend.data.PlayerRegistrationRepresentation;
import project.backend.exceptions.UserNotFoundException;
import project.backend.repository.PlayerRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class KeycloakUserServiceImpl implements KeycloakUserService {

    private final String UPDATE_PASSWORD = "UPDATE_PASSWORD";
    private final Keycloak keycloakService;
    @Autowired
    private PlayerRepository playerRepository;

    public KeycloakUserServiceImpl(Keycloak keycloakService) {
        this.keycloakService = keycloakService;
    }

    @Override
    public ResponseEntity<?> createUser(PlayerRegistrationRepresentation userReg) {
        UserRepresentation user= new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(userReg.username());
        user.setEmail(userReg.email());
        user.setFirstName(userReg.firstName());
        user.setLastName(userReg.lastName());
        user.setEmailVerified(true); // SAREBBE DA VERIFICARRE MA UN PO COMPLICATO A LIVELLO DI API ECC...


        CredentialRepresentation password = new CredentialRepresentation();
        password.setType(CredentialRepresentation.PASSWORD);
        password.setValue(userReg.password());
        password.setTemporary(false);

        System.out.println("SONO QUI");

        user.setCredentials(List.of(password));

        UsersResource usersResource = getUsersResource();
        try (Response response = usersResource.create(user)) {
            if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                Player newPlayerAdded=getPlayerByUserRepresentation(userReg);
                playerRepository.save(newPlayerAdded);
                return new ResponseEntity<>(newPlayerAdded,HttpStatus.CREATED);
            }
            System.out.println("Error creating user");
            return new ResponseEntity<>(response.readEntity(String.class), HttpStatusCode.valueOf(response.getStatus()));
        }
    }


    private Player getPlayerByUserRepresentation (PlayerRegistrationRepresentation userReg){
        return Player.builder()
                .username(userReg.username())
                .email(userReg.email())
                .firstName(userReg.firstName())
                .lastName(userReg.lastName())
                .blitzPoints((short) 600) // settato di default a 600
                .bulletPoints((short) 600) // settato di default a 600
                .rapidPoints((short) 600) // settato di default a 600
                .avatar(userReg.avatar())
                .build();
    }

    private UsersResource getUsersResource() {
        String realm = "playerRealm";
        RealmResource realmResource= keycloakService.realm(realm);
        return realmResource.users();
    }

    @Override
    public ResponseEntity<?> getUserByID(String userId) {
        return new ResponseEntity<>(getUsersResource().get(userId).toRepresentation(),HttpStatus.OK);
    }

    @Override
    public void deleteUser(String username) {
        getUsersResource().delete(username);
    }

    @Override
    public void forgotPassword (String username) throws UserNotFoundException {
        UsersResource usersResource = getUsersResource();
        List<UserRepresentation> list = usersResource.searchByUsername(username,true);

        var userRepresentation = list.stream().findFirst().orElse(null);
        if (userRepresentation != null) {

            UserResource utente = usersResource.get(userRepresentation.getId());
            List<String> actions = new ArrayList<>();
            actions.add(UPDATE_PASSWORD);
            utente.executeActionsEmail(actions);

        }
        throw new UserNotFoundException();
    }
}
