package com.unir.tfm.gestion_usuarios.data;

import com.unir.tfm.gestion_usuarios.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsById(@NonNull Long id);
    List<User> findByRole(String role);

    
}
