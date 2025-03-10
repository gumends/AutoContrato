package com.gustavo.auth.service;

import com.gustavo.auth.dto.ProprietarioDTO;
import com.gustavo.auth.exception.exceptions.EventNotFoundException;
import com.gustavo.auth.model.Proprietario;
import com.gustavo.auth.repository.PropriedadeRepository;
import com.gustavo.auth.repository.ProprietarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProprietarioService {

    private final ProprietarioRepository proprietarioRepository;
    private final PropriedadeRepository propriedadeRepository;

    public ProprietarioService(ProprietarioRepository proprietarioRepository, PropriedadeRepository propriedadeRepository) {
        this.proprietarioRepository = proprietarioRepository;
        this.propriedadeRepository = propriedadeRepository;
    }

    public ResponseEntity<Proprietario> saveProprietario(ProprietarioDTO proprietarioDTO, String id) {
        Proprietario p = new Proprietario();
        p.setCpf(proprietarioDTO.cpf());
        p.setRg(proprietarioDTO.rg());
        p.setNome(proprietarioDTO.nome());
        p.setNascimento(proprietarioDTO.nascimento());
        p.setNacionalidade(proprietarioDTO.nacionalidade());
        p.setUserId(id);

        return ResponseEntity.ok(proprietarioRepository.save(p));
    }

    public Page<Proprietario> getAllProprietarios(String id, Boolean status, String nome, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return proprietarioRepository.findAllByUserIdAndStatusAndNomeContainingIgnoreCase(id, status, nome, pageable);
    }

    public ResponseEntity<Proprietario> buscaProprietario(String id, String userId) {
        Optional<Proprietario> proprietario = Optional.ofNullable(proprietarioRepository.findByUserIdAndId(userId, id));
        return proprietario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Proprietario> alterarStatusProprietario(String id) {
        return proprietarioRepository.findById(id)
                .map(proprietario -> {
                    proprietario.setStatus(!proprietario.getStatus());
                    return ResponseEntity.ok(proprietarioRepository.save(proprietario));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Proprietario> atualizarProprietario(String id, ProprietarioDTO proprietarioDTO) {
        return proprietarioRepository.findById(id)
                .map(proprietario -> {
                    proprietario.setNome(proprietarioDTO.nome());
                    proprietario.setRg(proprietarioDTO.rg());
                    proprietario.setCpf(proprietarioDTO.cpf());
                    proprietario.setNacionalidade(proprietarioDTO.nacionalidade());
                    proprietario.setNascimento(proprietarioDTO.nascimento());
                    return ResponseEntity.ok(proprietarioRepository.save(proprietario));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Proprietario>> buscaTodosProprietarios(String userId, boolean status) {
        List<Proprietario> proprietarios = proprietarioRepository.findAllByUserIdAndStatus(userId, status);
        if (proprietarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(proprietarios);
    }
}