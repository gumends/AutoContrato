package com.gustavo.autocontrato.repository;

import com.gustavo.autocontrato.model.Locatario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LocatarioRepository extends JpaRepository<Locatario, String> {
    Page<Locatario> findAllByUserIdAndStatusAndNomeContainingIgnoreCase(String id, Boolean status, String nome, Pageable pageable);
    List<Locatario> findAllByUserIdAndStatus(String userId, boolean status);
    Optional<Locatario> findByUserIdAndId(String id, String UserId);
    Optional<Locatario> findByCpfOrRg(String cpf, String rg);
}
