package com.gustavo.auth.service;

import com.gustavo.auth.dto.PropriedadeDTO;
import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.model.Propriedade;
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
public class PropriedadeService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;
    @Autowired
    private PropriedadeRepository propriedadeRepository;

    public ResponseEntity<Propriedade> criarPropriedade(PropriedadeDTO propriedade, String id) {
        Propriedade p = new Propriedade();

        p.setRua(propriedade.rua());
        p.setNumero(propriedade.numero());
        p.setNumCasa(propriedade.numCasa());
        p.setBairro(propriedade.bairro());
        p.setCep(propriedade.cep());
        p.setLocalizacao(propriedade.localizacao());
        p.setAluguel(propriedade.aluguel());
        p.setDataPagamento(propriedade.dataPagamento());
        p.setProprietario(proprietarioRepository.findById(propriedade.proprietarioID()).orElseThrow());
        p.setUserId(id);

        return ResponseEntity.ok(propriedadeRepository.save(p));
    }

    public Page<Propriedade> getAllPropriedades(String id, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return propriedadeRepository.findAllByUserId(id, pageable);
    }

    public ResponseEntity<Propriedade> getById(String id, String userId) {
        return ResponseEntity.ok(propriedadeRepository.findByUserIdAndId(id, userId));
    }

    public ResponseEntity<Propriedade> alterarStatusProprietario(String id, String userId) {

        Optional<Propriedade> optionalPropriedade = propriedadeRepository.findById(id);

        if (optionalPropriedade.isEmpty() && !optionalPropriedade.get().getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }

        Propriedade p = optionalPropriedade.get();

        p.setStatus(!p.getStatus());

        Propriedade updatedPropriedade = propriedadeRepository.save(p);
        return ResponseEntity.ok(updatedPropriedade);
    }

    public ResponseEntity<Propriedade> atualizarProprietario(String id, String userId, PropriedadeDTO propriedade) {

        Optional<Propriedade> optionalPropriedade = propriedadeRepository.findById(id);

        if (optionalPropriedade.isEmpty() && !optionalPropriedade.get().getUserId().equals(userId)) {
            return ResponseEntity.notFound().build();
        }

        Propriedade p = optionalPropriedade.get();

        p.setRua(propriedade.rua());
        p.setNumero(propriedade.numero());
        p.setNumCasa(propriedade.numCasa());
        p.setBairro(propriedade.bairro());
        p.setCep(propriedade.cep());
        p.setLocalizacao(propriedade.localizacao());
        p.setAluguel(propriedade.aluguel());
        p.setDataPagamento(propriedade.dataPagamento());
        p.setProprietario(proprietarioRepository.findById(propriedade.proprietarioID()).orElseThrow());
        p.setUserId(id);

        Propriedade updatedPropriedade = propriedadeRepository.save(p);

        return ResponseEntity.ok(updatedPropriedade);
    }
}
