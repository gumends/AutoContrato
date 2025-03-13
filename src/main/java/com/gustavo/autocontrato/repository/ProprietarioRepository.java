package com.gustavo.autocontrato.repository;

import com.gustavo.autocontrato.model.Locatario;
import com.gustavo.autocontrato.model.Proprietario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProprietarioRepository extends JpaRepository<Proprietario, String> {
    Page<Proprietario> findAllByUserIdAndStatusAndNomeContainingIgnoreCase(String id, Boolean status, String nome, Pageable pageable);
    List<Proprietario> findAllByUserIdAndStatus(String userId, boolean status);
    Optional<Proprietario> findByUserIdAndId(String id, String UserId);
    Optional<Proprietario> findByCpfOrRg(String cpf, String rg);
}
