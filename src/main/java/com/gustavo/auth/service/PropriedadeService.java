package com.gustavo.auth.service;

import com.gustavo.auth.dto.PropriedadeDTO;
import com.gustavo.auth.exception.exceptions.EventNotFoundException;
import com.gustavo.auth.model.Propriedade;
import com.gustavo.auth.repository.PropriedadeRepository;
import com.gustavo.auth.repository.ProprietarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        System.out.println(propriedade.proprietarioID());
        if (propriedade.proprietarioID() != null){
            p.setProprietario(proprietarioRepository.findById(propriedade.proprietarioID()).orElseThrow());
        } else {
            p.setProprietario(null);
        }

        p.setUserId(id);

        return ResponseEntity.ok(propriedadeRepository.save(p));
    }

    public ResponseEntity<Propriedade> removerLocatario(String id){
        Propriedade p = propriedadeRepository.findById(id).orElseThrow();

        p.setLocatario(null);
        return ResponseEntity.ok(propriedadeRepository.save(p));
    }

    public ResponseEntity<Propriedade> removerProprietario(String id){
        Propriedade p = propriedadeRepository.findById(id).orElseThrow();
        p.setProprietario(null);
        return ResponseEntity.ok(propriedadeRepository.save(p));
    }

    public Page<Propriedade> getAllPropriedades(String id, Boolean status, String rua, int pagina, int tamanho) {
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return propriedadeRepository.findAllByUserIdAndStatusAndRuaContainingIgnoreCase(id, status, rua, pageable);
    }

    public ResponseEntity<Propriedade> getById(String id,  String userId) {
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
        if (propriedade.proprietarioID() == null){
            p.setProprietario(null);
        } else {
            p.setProprietario(proprietarioRepository.findById(propriedade.proprietarioID()).orElseThrow());
        }

        Propriedade updatedPropriedade = propriedadeRepository.save(p);

        return ResponseEntity.ok(updatedPropriedade);
    }

    public ResponseEntity<List<Propriedade>> buscaTodasPropriedades(String userId){
        return ResponseEntity.ok(propriedadeRepository.findAllByUserId(userId));
    }

    public ResponseEntity<String> alugarDesalugarCasa(String id, boolean alugar){
        Propriedade p = propriedadeRepository.findById(id).orElseThrow();
        p.setAlugada(alugar);
        propriedadeRepository.save(p);
        if (alugar){
            return ResponseEntity.ok("Propriedade alugada");
        } else {
            return ResponseEntity.ok("Propriedade Desoculpadada");
        }
    }

    public ResponseEntity<Map<String, Object>> aluguel(String userId){
        List<Propriedade> propriedades = propriedadeRepository.findAllByUserId(userId);

        List<BigDecimal> propriedadesAlugadas = propriedades
                .stream()
                .filter(Propriedade::getAlugada)
                .map(Propriedade::getAluguel).toList();

        BigDecimal alugelReceber = BigDecimal.ZERO;

        for (BigDecimal valor: propriedadesAlugadas){
            alugelReceber = alugelReceber.add(valor);
        }

        List<BigDecimal> propriedadesDisponiveis = propriedades
                .stream()
                .map(Propriedade::getAluguel).toList();

        BigDecimal alugelTotal = BigDecimal.ZERO;

        for (BigDecimal valor: propriedadesDisponiveis){
            alugelTotal = alugelTotal.add(valor);
        }

        Map<String, Object> json = new HashMap<>();

        json.put("receber", alugelReceber);
        json.put("total", alugelTotal);
        return ResponseEntity.ok(json);
    }
}
