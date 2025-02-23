package com.gustavo.auth.repository;

import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.model.Proprietario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocatarioRepository extends JpaRepository<Locatario, String> {
    Page<Locatario> findAllByUserId(String id, Pageable pageable);
    Locatario findByUserIdAndId(String id, String UserId);
}
