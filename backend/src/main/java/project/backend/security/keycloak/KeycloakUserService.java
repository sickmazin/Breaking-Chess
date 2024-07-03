package project.backend.security.keycloak;

import org.springframework.http.ResponseEntity;
import project.backend.data.PlayerRegistrationRepresentation;

public interface KeycloakUserService {
    ResponseEntity<?> createUser(PlayerRegistrationRepresentation user);
    ResponseEntity<?> getUserByID(String userId);
    void deleteUser(String username);
}
