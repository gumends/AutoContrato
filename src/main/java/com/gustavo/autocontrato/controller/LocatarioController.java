package com.gustavo.autocontrato.controller;

import com.gustavo.autocontrato.dto.LocatarioDTO;
import com.gustavo.autocontrato.infra.security.TokenService;
import com.gustavo.autocontrato.model.Locatario;
import com.gustavo.autocontrato.service.LocatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locatarios")
public class LocatarioController {

    @Autowired
    private LocatarioService locatarioService;

    @Autowired
    private TokenService tokenService;

    private String getUserIdFromToken(String token) {
        return tokenService.getUserIdFromToken(token);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Locatario>> PropriedadeController(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "true") boolean status,
            @RequestParam(defaultValue = "true") boolean alocado
    ) {
        String userId = getUserIdFromToken(token);
        return locatarioService.buscaTodosLocatarios(userId, alocado, status);
    }

    @PostMapping
    public ResponseEntity<?> createLocatario(
            @RequestHeader("Authorization") String token,
            @RequestBody LocatarioDTO locatarioDTO) {
        String userId = getUserIdFromToken(token);
        return locatarioService.createLocatario(userId, locatarioDTO);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> patchLocatario(
            @RequestHeader("Authorization") String token,
            @RequestBody LocatarioDTO locatarioDTO,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return locatarioService.updateLocatario(id, userId, locatarioDTO);
    }

    @GetMapping
    public Page<Locatario> getAllLocatarios(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "true") boolean status,
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        String userId = getUserIdFromToken(token);
        return locatarioService.findAllLocatarios(userId, status, nome, pagina, tamanho);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locatario> getLocatarioById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return locatarioService.findLocatario(id, userId);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateLocatarioStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return locatarioService.changeStatusLocatario(id, userId);
    }
}