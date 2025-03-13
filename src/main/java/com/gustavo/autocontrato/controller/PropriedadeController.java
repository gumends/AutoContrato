package com.gustavo.autocontrato.controller;

import com.gustavo.autocontrato.dto.PropriedadeDTO;
import com.gustavo.autocontrato.model.Propriedade;
import com.gustavo.autocontrato.service.PropriedadeService;
import com.gustavo.autocontrato.infra.security.TokenService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/propriedades")
@SecurityRequirement(name = "Bearer Authentication")
public class PropriedadeController {

    @Autowired
    private PropriedadeService propriedadeService;

    @Autowired
    private TokenService tokenService;

    private String getUserIdFromToken(String token) {
        return tokenService.getUserIdFromToken(token);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Propriedade>> buscaTodasPropriedades(@Parameter(hidden = true) @RequestHeader("Authorization") String token){
        String userId = getUserIdFromToken(token);
        return ResponseEntity.ok(propriedadeService.findAllPropriedades(userId).getBody());
    }

    @PostMapping
    public ResponseEntity<Propriedade> createPropriedade(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestBody PropriedadeDTO propriedadeDTO) {
        System.out.println(propriedadeDTO);
        String userId = getUserIdFromToken(token);
        return propriedadeService.createPropriedade(propriedadeDTO, userId);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePropriedade(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PathVariable("id") String id,
            @RequestBody PropriedadeDTO propriedadeDTO,
            @RequestParam(defaultValue = "false") boolean removeProprietario,
            @RequestParam(defaultValue = "false") boolean removeLocatario
            ) {
        String userId = getUserIdFromToken(token);

        if (removeProprietario){
            return propriedadeService.removeProprietario(id);
        }
        if (removeLocatario){
            return  propriedadeService.removeLocatario(id);
        }

        return propriedadeService.updatePropriedade(id, userId, propriedadeDTO);
    }

    @GetMapping
    public Page<Propriedade> getAllPropriedades(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "true") boolean status,
            @RequestParam(defaultValue = "") String rua,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        String userId = getUserIdFromToken(token);
        return propriedadeService.getAllPropriedades(userId, status, rua, pagina, tamanho);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPropriedadeById(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return propriedadeService.getById(id, userId);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> changeStatusPropriedade(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return propriedadeService.changeStatusPropriedade(id, userId);
    }

    @GetMapping("/aluguel")
    public ResponseEntity<Map<String, Object>> calculateRent(
            @Parameter(hidden = true) @RequestHeader("Authorization") String token
    ){
        String userId = getUserIdFromToken(token);
        return propriedadeService.calculateRent(userId);
    }
}