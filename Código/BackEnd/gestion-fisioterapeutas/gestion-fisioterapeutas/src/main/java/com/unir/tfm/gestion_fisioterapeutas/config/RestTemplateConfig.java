package com.unir.tfm.gestion_fisioterapeutas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


/* package com.unir.tfm.gestion_fisioterapeutas.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(List.of(authorizationInterceptor()));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestInterceptor authorizationInterceptor() {
        return (request, body, execution) -> {
            String token = "Bearer " + getJwtFromSecurityContext();
            System.out.println("TokenMS-GF: " + token);
            request.getHeaders().add("Authorization", token);
            return execution.execute(request, body);
        };
    }

    private String getJwtFromSecurityContext() {
        // Get the JWT from the security context
        return (String) org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication()
                .getCredentials();
    }
}
 */