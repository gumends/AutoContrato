package com.gustavo.auth.controller;

import com.gustavo.auth.dto.LocatarioDTO;
import com.gustavo.auth.infra.security.TokenService;
import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.service.LocatarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<Locatario> createLocatario(
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
        return locatarioService.patchedLocatario(id, userId, locatarioDTO);
    }

    @GetMapping
    public Page<Locatario> getAllLocatarios(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        String userId = getUserIdFromToken(token);
        return locatarioService.findAllLocatarios(userId, pagina, tamanho);
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
        return locatarioService.alteraStatusLocatario(userId, id);
    }
}