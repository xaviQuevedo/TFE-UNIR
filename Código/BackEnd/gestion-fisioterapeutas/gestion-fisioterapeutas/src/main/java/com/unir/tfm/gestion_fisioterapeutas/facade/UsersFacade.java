package com.unir.tfm.gestion_fisioterapeutas.facade;

import com.unir.tfm.gestion_fisioterapeutas.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UsersFacade {

    @Value("${getUsersByRole.url}")
    private String getUsersByRoleUrl;

    @Value("${getUserById.url}")
    private String getUserByIdUrl;

    private final RestTemplate restTemplate;

    public User getUser(Long userId, String token) {
        try {
            String url = String.format(getUserByIdUrl, userId);
            log.info("Fetching user with ID {}. URL: {}. Token: {}", userId, url, token);
    
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            HttpEntity<?> entity = new HttpEntity<>(headers);
    
            ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.GET, entity, User.class);
    
            return response.getBody();
        } catch (Exception e) {
            log.error("Error fetching user with ID {}: {}", userId, e.getMessage());
            return null;
        }
    }

    public List<User> getUsersByRole(String role) {
        try {
            String url = String.format(getUsersByRoleUrl, role);
            log.info("Fetching users with role '{}'. URL: {}", role, url);

            User[] users = restTemplate.getForObject(url, User[].class);
            if (users != null) {
                log.info("Found {} users with role '{}'", users.length, role);
                return Arrays.asList(users);
            }
        } catch (Exception e) {
            log.error("Error fetching users with role '{}': {}", role, e.getMessage());
        }
        return List.of(); // Retorna una lista vacía en caso de error
    }
}
