package com.gustavo.autocontrato.repository;

import com.gustavo.autocontrato.model.Propriedade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PropriedadeRepository extends JpaRepository<Propriedade, String> {
    Page<Propriedade> findAllByUserIdAndStatusAndRuaContainingIgnoreCase(String id, Boolean status, String rua, Pageable pageable);
    List<Propriedade> findAllByUserId(String userId);
    Optional<Propriedade> findByUserIdAndId(String id, String UserId);
}
