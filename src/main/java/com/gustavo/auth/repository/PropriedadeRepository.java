package com.gustavo.auth.repository;

import com.gustavo.auth.model.Propriedade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, String> {
    Page<Propriedade> findAllByUserId(String id, Pageable pageable);

    Propriedade findByUserIdAndId(String id, String UserId);
}
