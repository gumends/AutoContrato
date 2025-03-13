package com.gustavo.autocontrato.service;

import com.gustavo.autocontrato.dto.PropriedadeDTO;
import com.gustavo.autocontrato.exception.exceptions.EventNotFoundException;
import com.gustavo.autocontrato.model.Propriedade;
import com.gustavo.autocontrato.repository.PropriedadeRepository;
import com.gustavo.autocontrato.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PropriedadeService {

    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    public ResponseEntity<Propriedade> createPropriedade(PropriedadeDTO propriedadeDTO, String userId) {
        Propriedade propriedade = new Propriedade();
        propriedade.setRua(propriedadeDTO.rua());
        propriedade.setNumero(propriedadeDTO.numero());
        propriedade.setNumCasa(propriedadeDTO.numCasa());
        propriedade.setBairro(propriedadeDTO.bairro());
        propriedade.setCep(propriedadeDTO.cep());
        propriedade.setLocalizacao(propriedadeDTO.localizacao());
        propriedade.setAluguel(propriedadeDTO.aluguel());
        propriedade.setDataPagamento(propriedadeDTO.dataPagamento());
        propriedade.setUserId(userId);

        if (propriedadeDTO.proprietarioID() != null) {
            propriedade.setProprietario(proprietarioRepository.findById(propriedadeDTO.proprietarioID())
                    .orElseThrow(() -> new EventNotFoundException("Proprietário não encontrado com o ID: " + propriedadeDTO.proprietarioID())));
        } else {
            propriedade.setProprietario(null);
        }

        Propriedade savedPropriedade = propriedadeRepository.save(propriedade);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPropriedade);
    }

    public ResponseEntity<Propriedade> removeLocatario(String id) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));
        propriedade.setLocatario(null);
        return ResponseEntity.ok(propriedadeRepository.save(propriedade));
    }

    public ResponseEntity<Propriedade> removeProprietario(String id) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));
        propriedade.setProprietario(null);
        return ResponseEntity.ok(propriedadeRepository.save(propriedade));
    }

    public Page<Propriedade> getAllPropriedades(String userId, Boolean status, String rua, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return propriedadeRepository.findAllByUserIdAndStatusAndRuaContainingIgnoreCase(userId, status, rua, pageable);
    }

    public ResponseEntity<Propriedade> getById(String id, String userId) {
        Propriedade propriedade = propriedadeRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));
        return ResponseEntity.ok(propriedade);
    }

    public ResponseEntity<Propriedade> changeStatusPropriedade(String id, String userId) {
        Propriedade propriedade = propriedadeRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));
        propriedade.setStatus(!propriedade.getStatus());
        return ResponseEntity.ok(propriedadeRepository.save(propriedade));
    }

    public ResponseEntity<Propriedade> updatePropriedade(String id, String userId, PropriedadeDTO propriedadeDTO) {
        Propriedade propriedade = propriedadeRepository.findByUserIdAndId(userId, id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));

        updatePropriedadeFromDTO(propriedade, propriedadeDTO);
        Propriedade updatedPropriedade = propriedadeRepository.save(propriedade);
        return ResponseEntity.ok(updatedPropriedade);
    }

    private void updatePropriedadeFromDTO(Propriedade propriedade, PropriedadeDTO propriedadeDTO) {
        propriedade.setRua(propriedadeDTO.rua());
        propriedade.setNumero(propriedadeDTO.numero());
        propriedade.setNumCasa(propriedadeDTO.numCasa());
        propriedade.setBairro(propriedadeDTO.bairro());
        propriedade.setCep(propriedadeDTO.cep());
        propriedade.setLocalizacao(propriedadeDTO.localizacao());
        propriedade.setAluguel(propriedadeDTO.aluguel());
        propriedade.setDataPagamento(propriedadeDTO.dataPagamento());

        if (propriedadeDTO.proprietarioID() == null) {
            propriedade.setProprietario(null);
        } else {
            propriedade.setProprietario(proprietarioRepository.findById(propriedadeDTO.proprietarioID())
                    .orElseThrow(() -> new EventNotFoundException("Proprietário não encontrado com o ID: " + propriedadeDTO.proprietarioID())));
        }
    }

    public ResponseEntity<List<Propriedade>> findAllPropriedades(String userId) {
        return ResponseEntity.ok(propriedadeRepository.findAllByUserId(userId));
    }

    public ResponseEntity<String> toggleHouseRental(String id, boolean alugar) {
        Propriedade propriedade = propriedadeRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Propriedade não encontrada com o ID: " + id));
        propriedade.setAlugada(alugar);
        propriedadeRepository.save(propriedade);
        return ResponseEntity.ok(alugar ? "Propriedade alugada" : "Propriedade desocupada");
    }

    public ResponseEntity<Map<String, Object>> calculateRent(String userId) {
        List<Propriedade> propriedades = propriedadeRepository.findAllByUserId(userId);

        BigDecimal aluguelReceber = propriedades.stream()
                .filter(Propriedade::getAlugada)
                .map(Propriedade::getAluguel)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal aluguelTotal = propriedades.stream()
                .map(Propriedade::getAluguel)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> response = new HashMap<>();
        response.put("receber", aluguelReceber);
        response.put("total", aluguelTotal);
        return ResponseEntity.ok(response);
    }
}