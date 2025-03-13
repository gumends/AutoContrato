package com.gustavo.autocontrato.repository;

import com.gustavo.autocontrato.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserRepository extends JpaRepository<Usuario, String> {
    UserDetails findByEmail(String email);
    UserDetails findByCpf(String cpf);
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
