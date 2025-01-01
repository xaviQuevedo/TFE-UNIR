package com.unir.tfm.gestion_usuarios.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.unir.tfm.gestion_usuarios.data.UserRepository;
import com.unir.tfm.gestion_usuarios.model.entity.User;
import com.unir.tfm.gestion_usuarios.model.request.ChangePasswordRequest;
import com.unir.tfm.gestion_usuarios.model.request.RegisterUserRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User registerUser(RegisterUserRequest registerUserRequest) {
        if (registerUserRequest.getEmail() == null || registerUserRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }
        if (registerUserRequest.getName() == null || registerUserRequest.getName().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (registerUserRequest.getLast_name() == null || registerUserRequest.getLast_name().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        if (registerUserRequest.getPassword() == null || registerUserRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }
        if (registerUserRequest.getRole() == null || registerUserRequest.getRole().isEmpty()) {
            throw new IllegalArgumentException("El rol es obligatorio.");
        }

        if (userRepository.findByEmail(registerUserRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya existe.");
        }

        User user = new User();
        user.setEmail(registerUserRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        user.setRole(registerUserRequest.getRole());
        user.setName(registerUserRequest.getName());
        user.setLast_name(registerUserRequest.getLast_name());
        return userRepository.save(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public boolean existById(Long userId) {
        if (userId == null) {
            throw new IllegalArgumentException("el ID no puede ser nulo");
        }
        return userRepository.existsById(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateUser(Long id, RegisterUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEmail(request.getEmail());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado" + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
    @Override
    public String changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validar contraseña actual
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        // Validar que la nueva contraseña cumpla con los requisitos
        if (request.getNewPassword().length() < 8) {
            throw new RuntimeException("La nueva contraseña debe tener al menos 8 caracteres");
        }

        // Encriptar y actualizar la contraseña
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return "Contraseña actualizada correctamente";
    }



}
