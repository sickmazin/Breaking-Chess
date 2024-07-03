package project.backend.security;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    @Bean
    public Keycloak keycloak(){
        String adminClientSecret = "1AOQeWXPye1e5qqF4K59ouosflq6E6P6"; // DA INSERIRE NEL YAML
        String adminClientId = "admin-cli"; // DA INSERIRE NEL YAML
        String domain = "http://localhost:8080"; // DA INSERIRE NEL YAML
        String realm = "playerRealm"; // DA INSERIRE NEL YAML
        return KeycloakBuilder.builder()
                .serverUrl(domain)
                .realm(realm)
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(adminClientId)
                .clientSecret(adminClientSecret)
                .build();
    }
}
