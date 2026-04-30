package com.ikernell.backend.repository;

import com.ikernell.backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Añade esta línea para que el Service pueda usarlo
    Optional<Usuario> findByEmail(String email);
}