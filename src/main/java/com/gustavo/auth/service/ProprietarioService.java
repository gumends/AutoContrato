package com.gustavo.auth.service;

import com.gustavo.auth.dto.ProprietarioDTO;
import com.gustavo.auth.exception.exceptions.EventNotFoundException;
import com.gustavo.auth.model.Proprietario;
import com.gustavo.auth.repository.PropriedadeRepository;
import com.gustavo.auth.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ProprietarioService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    public ResponseEntity<Proprietario> saveProprietario(ProprietarioDTO proprietarioDTO, String id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

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
        Proprietario proprietario = proprietarioRepository.findByUserIdAndId(userId, id);
        if (proprietario == null) {
            throw new EventNotFoundException("Proprietário não encontrado com o ID: " + id);
        }
        return ResponseEntity.ok(proprietario);
    }

    public ResponseEntity<Proprietario> alterarStatusProprietario(String id) {

        Optional<Proprietario> proprietario = proprietarioRepository.findById(id);

        if (proprietario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proprietario p = proprietario.get();

        p.setStatus(!p.getStatus());

        Proprietario updatedProprietario = proprietarioRepository.save(p);
        return ResponseEntity.ok(updatedProprietario);
    }

    public ResponseEntity<Proprietario> atualizarProprietario(String id, ProprietarioDTO proprietarioDTO) {

        Optional<Proprietario> optionalProprietario = proprietarioRepository.findById(id);

        if (optionalProprietario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Proprietario proprietario = optionalProprietario.get();

        proprietario.setNome(proprietarioDTO.nome());
        proprietario.setRg(proprietarioDTO.rg());
        proprietario.setCpf(proprietarioDTO.cpf());
        proprietario.setNacionalidade(proprietarioDTO.nacionalidade());
        proprietario.setNascimento(proprietarioDTO.nascimento());

        Proprietario updatedProprietario = proprietarioRepository.save(proprietario);

        return ResponseEntity.ok(updatedProprietario);
    }
    
    public  ResponseEntity<List<Proprietario>> buscaTodosProprietarios(String userId, boolean status){
        return ResponseEntity.ok(proprietarioRepository.findAllByUserIdAndStatus(userId, status));
    }
}
