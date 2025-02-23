package com.gustavo.auth.repository;

import com.gustavo.auth.model.Propriedade;
import com.gustavo.auth.model.Proprietario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProprietarioRepository extends JpaRepository<Proprietario, String> {
    Page<Proprietario> findAllByUserId(String id, Pageable pageable);
    Proprietario findByUserIdAndId(String id, String UserId);
}
