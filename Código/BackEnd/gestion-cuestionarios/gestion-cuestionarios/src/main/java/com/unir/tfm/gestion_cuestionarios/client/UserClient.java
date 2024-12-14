package com.unir.tfm.gestion_cuestionarios.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClient {
    private final RestTemplate restTemplate;

    public UserClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean userExists(Long userId) {
        String url = "http://localhost:8080/users/" + userId + "/exist";
        return restTemplate.postForObject(url, null, Boolean.class);
    }

}
