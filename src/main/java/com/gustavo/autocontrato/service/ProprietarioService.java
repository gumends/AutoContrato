package com.gustavo.autocontrato.service;

import com.gustavo.autocontrato.dto.ProprietarioDTO;
import com.gustavo.autocontrato.exception.ExceptionErroMessage;
import com.gustavo.autocontrato.exception.exceptions.EventNotFoundException;
import com.gustavo.autocontrato.model.Proprietario;
import com.gustavo.autocontrato.repository.PropriedadeRepository;
import com.gustavo.autocontrato.repository.ProprietarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

    public ResponseEntity<?> createProprietario(ProprietarioDTO proprietarioDTO, String userId) {
        Optional<Proprietario> p = proprietarioRepository.findByCpfOrRg(proprietarioDTO.cpf(), proprietarioDTO.rg());
        if (p.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionErroMessage(
                            HttpStatus.BAD_REQUEST,
                            "Proprietário já cadastrado."
                    ));
        }
        Proprietario proprietario = new Proprietario();
        proprietario.setCpf(proprietarioDTO.cpf());
        proprietario.setRg(proprietarioDTO.rg());
        proprietario.setNome(proprietarioDTO.nome());
        proprietario.setNascimento(proprietarioDTO.nascimento());
        proprietario.setNacionalidade(proprietarioDTO.nacionalidade());
        proprietario.setUserId(userId);

        Proprietario savedProprietario = proprietarioRepository.save(proprietario);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProprietario);
    }

    public Page<Proprietario> getAllProprietarios(String userId, Boolean status, String nome, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return proprietarioRepository.findAllByUserIdAndStatusAndNomeContainingIgnoreCase(userId, status, nome, pageable);
    }

    public ResponseEntity<Proprietario> getById(String id, String userId) {
        Proprietario proprietario = proprietarioRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Proprietário não encontrado com o ID: " + id));
        return ResponseEntity.ok(proprietario);
    }

    public ResponseEntity<Proprietario> changeStatusProprietario(String id) {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Proprietário não encontrado com o ID: " + id));
        proprietario.setStatus(!proprietario.getStatus());
        return ResponseEntity.ok(proprietarioRepository.save(proprietario));
    }

    public ResponseEntity<Proprietario> updateProprietario(String id, ProprietarioDTO proprietarioDTO) {
        Proprietario proprietario = proprietarioRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Proprietário não encontrado com o ID: " + id));

        updateProprietarioFromDTO(proprietario, proprietarioDTO);
        Proprietario updatedProprietario = proprietarioRepository.save(proprietario);
        return ResponseEntity.ok(updatedProprietario);
    }

    private void updateProprietarioFromDTO(Proprietario proprietario, ProprietarioDTO proprietarioDTO) {
        proprietario.setNome(proprietarioDTO.nome());
        proprietario.setRg(proprietarioDTO.rg());
        proprietario.setCpf(proprietarioDTO.cpf());
        proprietario.setNacionalidade(proprietarioDTO.nacionalidade());
        proprietario.setNascimento(proprietarioDTO.nascimento());
    }

    public ResponseEntity<List<Proprietario>> findAllProprietarios(String userId, boolean status) {
        List<Proprietario> proprietarios = proprietarioRepository.findAllByUserIdAndStatus(userId, status);
        if (proprietarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(proprietarios);
    }
}