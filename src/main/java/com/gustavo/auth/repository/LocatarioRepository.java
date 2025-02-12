package com.gustavo.auth.repository;

import com.gustavo.auth.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocatarioRepository extends JpaRepository<Locatario, String> {
}
