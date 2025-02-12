package com.gustavo.auth.repository;

import com.gustavo.auth.model.Propriedade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropriedadeRepository extends JpaRepository<Propriedade, String> {
}
