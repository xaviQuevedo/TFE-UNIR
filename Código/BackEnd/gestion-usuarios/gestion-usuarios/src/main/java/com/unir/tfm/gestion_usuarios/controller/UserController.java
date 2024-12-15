package com.unir.tfm.gestion_usuarios.controller;

import com.unir.tfm.gestion_usuarios.model.request.RegisterUserRequest;
import com.unir.tfm.gestion_usuarios.model.entity.User;
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

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        var user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        if (!userService.validatePassword(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole());
        Map<String, String> response = new HashMap<>();

        response.put("token", token);
        response.put("role", user.getRole());
        response.put("email", user.getEmail());
        // System.out.println(user.getRole());

        return ResponseEntity.ok(response);
    }

    /* @GetMapping("/{id}")
    public ResponseEntity<Boolean> userExists(@PathVariable Long id) {
        
        return ResponseEntity.ok(userService.existById(id));
    }
 */

 @GetMapping("/{id}")
public ResponseEntity<User> getUserById(@PathVariable Long id) {
    User user = userService.findById(id)
                           .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    return ResponseEntity.ok(user);
}

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<User> register(@RequestBody RegisterUserRequest request) {
        System.out.println("Rol recibido: " + request.getRole());

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

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<List<User>> getUsersByRole(@PathVariable String role) {
        List<User> users = userService.getUsersByRole(role);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("userr"+users);
        return ResponseEntity.ok(users);
    }

}
