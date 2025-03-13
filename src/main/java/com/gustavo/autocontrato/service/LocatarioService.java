package com.gustavo.autocontrato.service;

import com.gustavo.autocontrato.dto.LocatarioDTO;
import com.gustavo.autocontrato.exception.ExceptionErroMessage;
import com.gustavo.autocontrato.exception.exceptions.EventNotFoundException;
import com.gustavo.autocontrato.model.Locatario;
import com.gustavo.autocontrato.repository.LocatarioRepository;
import com.gustavo.autocontrato.repository.PropriedadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocatarioService {

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    @Autowired
    private PropriedadeService propriedadeService;

    public ResponseEntity<List<Locatario>> buscaTodosLocatarios(String userId, boolean alocado, boolean status) {
        List<Locatario> locatarios = locatarioRepository.findAllByUserIdAndStatus(userId, status);

        if (!alocado) {
            locatarios = locatarios.stream()
                    .filter(locatario -> !locatario.getAlocado())
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(locatarios);
    }

    public ResponseEntity<?> createLocatario(String userId, LocatarioDTO locatarioDTO) {
        
        Optional<Locatario> verificaLocatario = locatarioRepository.findByCpfOrRg(locatarioDTO.cpf(), locatarioDTO.rg());
        if (verificaLocatario.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ExceptionErroMessage(
                            HttpStatus.BAD_REQUEST,
                            "Locatário já cadastrado."
                    ));
        }
        Locatario locatario = new Locatario();
        locatario.setNome(locatarioDTO.nome());
        locatario.setRg(locatarioDTO.rg());
        locatario.setCpf(locatarioDTO.cpf());
        locatario.setNascimento(locatarioDTO.nascimento());
        locatario.setUserId(userId);

        if (locatarioDTO.propriedadeId() != null) {
            locatario.setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId())
                    .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + locatarioDTO.propriedadeId())));
        }

        locatarioRepository.save(locatario);

        if (locatarioDTO.propriedadeId() != null) {
            propriedadeService.toggleHouseRental(locatarioDTO.propriedadeId(), true);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(locatario);
    }

    public Page<Locatario> findAllLocatarios(String userId, Boolean status, String nome, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return locatarioRepository.findAllByUserIdAndStatusAndNomeContainingIgnoreCase(userId, status, nome, pageable);
    }

    public ResponseEntity<Locatario> findLocatario(String id, String userId) {
        Locatario locatario = locatarioRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Locatario não encontrado com o ID: " + id));
        return ResponseEntity.ok(locatario);
    }

    public ResponseEntity<Locatario> changeStatusLocatario(String id, String userId) {

        Locatario locatario = locatarioRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Locatario não encontrado com o ID: " + id));

        locatario.setStatus(!locatario.getStatus());
        return ResponseEntity.ok(locatarioRepository.save(locatario));
    }

    public ResponseEntity<Locatario> updateLocatario(String id, String userId, LocatarioDTO locatarioDTO) {
        return locatarioRepository.findById(id)
                .filter(locatario -> locatario.getUserId().equals(userId))
                .map(locatario -> {
                    updateLocatarioFromDTO(locatario, locatarioDTO);
                    return ResponseEntity.ok(locatarioRepository.save(locatario));
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    private void updateLocatarioFromDTO(Locatario locatario, LocatarioDTO locatarioDTO) {
        locatario.setNome(locatarioDTO.nome());
        locatario.setRg(locatarioDTO.rg());
        locatario.setCpf(locatarioDTO.cpf());
        locatario.setNascimento(locatarioDTO.nascimento());

        if (locatarioDTO.propriedadeId() != null) {
            locatario.setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId())
                    .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + locatarioDTO.propriedadeId())));
            propriedadeService.toggleHouseRental(locatarioDTO.propriedadeId(), true);
        } else if (locatario.getPropriedade() != null) {
            propriedadeService.toggleHouseRental(locatario.getPropriedade().getId(), false);
            locatario.setPropriedade(null);
        }
    }
}