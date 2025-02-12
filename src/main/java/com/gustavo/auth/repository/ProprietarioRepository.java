package com.gustavo.auth.repository;

import com.gustavo.auth.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProprietarioRepository extends JpaRepository<Proprietario, String> {
    Proprietario findAllByUserId(String id);
}
