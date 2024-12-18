package com.unir.tfm.gestion_cuestionarios.client;

import com.unir.tfm.gestion_cuestionarios.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {

    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean userExists(Long userId) {
        try {
            String url = "http://localhost:8762/ms-gestion-usuarios/users/" + userId;
            User user = restTemplate.getForObject(url, User.class);
            return user != null;
        } catch (Exception e) {
            return false;
        }
    }
}
