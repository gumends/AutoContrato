package com.gustavo.auth.repository;

import com.gustavo.auth.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserRepository extends JpaRepository<Usuario, String> {
    UserDetails findByEmail(String email);
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
