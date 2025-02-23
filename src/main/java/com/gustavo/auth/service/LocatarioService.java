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

@Service
public class LocatarioService {

    @Autowired
    private LocatarioRepository locatarioRepository;

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    public ResponseEntity<Locatario> createLocatario(String userID, LocatarioDTO locatarioDTO) {
        Locatario lo = new Locatario();

        lo.setNome(locatarioDTO.nome());
        lo.setRg(locatarioDTO.rg());
        lo.setCpf(locatarioDTO.cpf());
        lo.setNascimento(locatarioDTO.nascimento());
        lo.setUserId(userID);
        lo.setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId()).orElseThrow());
        locatarioRepository.save(lo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(lo);
    }

    public Page<Locatario> findAllLocatarios(String id, int pagina, int tamanho){
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return locatarioRepository.findAllByUserId(id, pageable);
    }

    public ResponseEntity<Locatario> findLocatario(String id, String userId) {
        Locatario locatario = locatarioRepository.findByUserIdAndId(userId, id);

        if (locatario == null) {
            throw new EventNotFoundException("Proprietário não encontrado com o ID: " + id);
        }
        return ResponseEntity.ok(locatario);
    }

    public ResponseEntity<Locatario> alteraStatusLocatario(String id, String userId){
        Optional<Locatario> locatario = locatarioRepository.findById(id);

        if (locatario.isPresent() && locatario.get().getUserId().equals(userId)) {
            locatario.get().setStatus(!locatario.get().getStatus());
            return ResponseEntity.ok(locatarioRepository.save(locatario.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<Locatario> patchedLocatario(String id, String userId, LocatarioDTO locatarioDTO){
        Optional<Locatario> locatario = locatarioRepository.findById(id);

        if (locatario.isPresent() && locatario.get().getUserId().equals(userId)) {
            locatario.get().setNome(locatarioDTO.nome());
            locatario.get().setRg(locatarioDTO.rg());
            locatario.get().setCpf(locatarioDTO.cpf());
            locatario.get().setNascimento(locatarioDTO.nascimento());
            locatario.get().setPropriedade(propriedadeRepository.findById(locatarioDTO.propriedadeId()).orElseThrow());
            return ResponseEntity.ok(locatarioRepository.save(locatario.get()));
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

}
