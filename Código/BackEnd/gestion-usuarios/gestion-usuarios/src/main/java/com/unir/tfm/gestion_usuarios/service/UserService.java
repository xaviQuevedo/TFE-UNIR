package com.unir.tfm.gestion_usuarios.service;

import com.unir.tfm.gestion_usuarios.model.entity.User;
import com.unir.tfm.gestion_usuarios.model.request.RegisterUserRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(RegisterUserRequest request);
    Optional <User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean validatePassword(String rawPassword, String encodedPassword);

    boolean existById(Long userId);

    List<User> getAllUsers();

    User updateUser(Long id, RegisterUserRequest request);
    void deleteUser(Long id);
    List<User> getUsersByRole(String role);

}
