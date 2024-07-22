package project.backend.security.keycloak;

import org.springframework.http.ResponseEntity;
import project.backend.data.PlayerRegistrationRepresentation;
import project.backend.exceptions.UserNotFoundException;

public interface KeycloakUserService {
    ResponseEntity<?> createUser (PlayerRegistrationRepresentation user);

    ResponseEntity<?> getUserByID (String userId);

    void deleteUser (String username);

    void forgotPassword (String username) throws UserNotFoundException;
}
