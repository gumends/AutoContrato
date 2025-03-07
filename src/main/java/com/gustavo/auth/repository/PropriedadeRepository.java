package com.gustavo.auth.repository;

import com.gustavo.auth.model.Propriedade;
import com.gustavo.auth.model.Proprietario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, String> {
    Page<Propriedade> findAllByUserIdAndStatusAndRuaContainingIgnoreCase(String id, Boolean status, String rua, Pageable pageable);
    List<Propriedade> findAllByUserId(String userId);
    Propriedade findByUserIdAndId(String id, String UserId);
}
