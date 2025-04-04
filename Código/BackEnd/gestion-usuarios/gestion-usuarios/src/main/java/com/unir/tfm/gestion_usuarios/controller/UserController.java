package com.unir.tfm.gestion_usuarios.controller;

import com.unir.tfm.gestion_usuarios.model.request.RegisterUserRequest;
import com.unir.tfm.gestion_usuarios.model.response.ChangePasswordResponse;
import com.unir.tfm.gestion_usuarios.model.entity.User;
import com.unir.tfm.gestion_usuarios.model.request.ChangePasswordRequest;
import com.unir.tfm.gestion_usuarios.model.request.LoginRequest;
import com.unir.tfm.gestion_usuarios.service.UserService;
import com.unir.tfm.gestion_usuarios.util.JwtUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    // Se crea un token cuando el usuario ingresan sus credenciales
    // @PostMapping("/login")
    @PostMapping("/sessions")

    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        var user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!userService.validatePassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Map<String, String> response = new HashMap<>();

        response.put("token", token);
        response.put("id", user.getUser_id().toString());
        response.put("role", user.getRole());
        response.put("email", user.getEmail());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(user);
    }

    // @PostMapping("/admin/register")
    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody RegisterUserRequest request) {
        User updateUser = userService.updateUser(id, request);
        return ResponseEntity.ok(updateUser);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // @GetMapping("/admin/all")
    @GetMapping
    /*
     * @PreAuthorize("hasRole('admin')")
     * public ResponseEntity<List<User>> getAllUsers() {
     * List<User> users = userService.getAllUsers();
     * return ResponseEntity.ok(users);
     * }
     */
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<User>> getAllUsers(@RequestParam(required = false) String role) {
        if (role != null) {
            List<User> users = userService.getUsersByRole(role);
            return users.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(users);
        }
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    // @PutMapping("/{userId}/change-password")
    @PatchMapping("/{userId}")

    public ResponseEntity<ChangePasswordResponse> changePassword(
            @PathVariable Long userId,
            @RequestBody ChangePasswordRequest request) {
        try {
            String message = userService.changePassword(userId, request);
            return ResponseEntity.ok(new ChangePasswordResponse(message));
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(new ChangePasswordResponse(e.getMessage()));
        }
    }

}
