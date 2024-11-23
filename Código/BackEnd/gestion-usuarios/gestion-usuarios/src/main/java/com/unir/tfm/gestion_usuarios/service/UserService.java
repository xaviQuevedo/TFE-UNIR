package com.unir.tfm.gestion_usuarios.service;

import com.unir.tfm.gestion_usuarios.model.entity.User;
import com.unir.tfm.gestion_usuarios.model.request.RegisterUserRequest;

import java.util.Optional;

public interface UserService {
    User registerUser(RegisterUserRequest request);

    Optional<User> findByEmail(String email);

    boolean validatePassword(String rawPassword, String encodedPassword);

}
