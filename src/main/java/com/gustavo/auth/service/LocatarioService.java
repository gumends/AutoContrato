package com.gustavo.auth.service;

import com.gustavo.auth.dto.LocatarioDTO;
import com.gustavo.auth.exception.exceptions.EventNotFoundException;
import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.repository.LocatarioRepository;
import com.gustavo.auth.repository.PropriedadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
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

    public ResponseEntity<List<Locatario>> buscaTodosLocatarios(String userId, boolean alocado, boolean status){
        System.out.println(!alocado);
        if (!alocado){
            return ResponseEntity.ok(locatarioRepository.findAllByUserIdAndStatus(userId, status).stream()
                    .filter(locatario -> !locatario.getAlocado())
                    .collect(Collectors.toList()
            ));
        }
        return ResponseEntity.ok(locatarioRepository.findAllByUserIdAndStatus(userId, status));
    }

    public ResponseEntity<Locatario> createLocatario(String userID, LocatarioDTO locatarioDTO) {
        Locatario lo = new Locatario();

        lo.setNome(locatarioDTO.nome());
        lo.setRg(locatarioDTO.rg());
        lo.setCpf(locatarioDTO.cpf());
        lo.setNascimento(locatarioDTO.nascimento());
        lo.setUserId(userID);
        if (locatarioDTO.propriedadeId() != null){
            lo.setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId()).orElseThrow());
        } else {
            lo.setPropriedade(null);
        }
        locatarioRepository.save(lo);

        if (locatarioDTO.propriedadeId() != null){
            propriedadeService.alugarDesalugarCasa(locatarioDTO.propriedadeId(), true);
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lo);
    }

    public Page<Locatario> findAllLocatarios(String id, Boolean status, String nome, int pagina, int tamanho){
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return locatarioRepository.findAllByUserIdAndStatusAndNomeContainingIgnoreCase(id, status, nome, pageable);
    }

    public ResponseEntity<Locatario> findLocatario(String id, String userId) {
        Locatario locatario = locatarioRepository.findByUserIdAndId(userId, id);

        if (locatario == null) {
            throw new EventNotFoundException("Locatario não encontrado com o ID: " + id);
        }
        return ResponseEntity.ok(locatario);
    }

    public ResponseEntity<Locatario> alteraStatusLocatario(String id, String userId){
        Locatario locatario = locatarioRepository.findByUserIdAndId(id, userId);
        locatario.setStatus(!locatario.getStatus());
        return ResponseEntity.ok(locatarioRepository.save(locatario));
    }

    public ResponseEntity<Locatario> patchedLocatario(String id, String userId, LocatarioDTO locatarioDTO){
        Optional<Locatario> locatario = locatarioRepository.findById(id);

        if (locatario.isPresent() && locatario.get().getUserId().equals(userId)) {
            locatario.get().setNome(locatarioDTO.nome());
            locatario.get().setRg(locatarioDTO.rg());
            locatario.get().setCpf(locatarioDTO.cpf());
            locatario.get().setNascimento(locatarioDTO.nascimento());
            if (locatarioDTO.propriedadeId() != null) {
                locatario.get().setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId()).orElseThrow());
                propriedadeService.alugarDesalugarCasa(locatario.get().getPropriedade().getId(), true);
            } else {
                propriedadeService.alugarDesalugarCasa(locatario.get().getPropriedade().getId(), false);
                locatario.get().setPropriedade(null);
            }

            return ResponseEntity.ok(locatarioRepository.save(locatario.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
