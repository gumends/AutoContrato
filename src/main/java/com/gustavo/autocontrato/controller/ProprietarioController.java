package com.gustavo.autocontrato.controller;

import com.gustavo.autocontrato.dto.ProprietarioDTO;
import com.gustavo.autocontrato.infra.security.TokenService;
import com.gustavo.autocontrato.model.Proprietario;
import com.gustavo.autocontrato.repository.ProprietarioRepository;
import com.gustavo.autocontrato.service.PropriedadeService;
import com.gustavo.autocontrato.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietarios")
@CrossOrigin(origins = "*")
public class ProprietarioController {

    @Autowired
    private ProprietarioService proprietarioService;

    @Autowired
    private TokenService tokenService;
    @Autowired
    private ProprietarioRepository proprietarioRepository;

    @Autowired
    private PropriedadeService propriedadeService;

    private String getUserIdFromToken(String token) {
        return tokenService.getUserIdFromToken(token);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Proprietario>> findAllProprietarios(
            @RequestHeader("Authorization") String token,
            @PathVariable("status") boolean status
    ){
        String userId = getUserIdFromToken(token);
        return ResponseEntity.ok(proprietarioService.findAllProprietarios(userId, status).getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> getById(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.getById(id, userId);
    }

    @PostMapping
    public ResponseEntity<?> createProprietario(
            @RequestHeader("Authorization") String token,
            @RequestBody ProprietarioDTO proprietarioDTO) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.createProprietario(proprietarioDTO, userId);
    }

    @GetMapping
    public Page<Proprietario> getAllProprietarios(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "true") boolean status,
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.getAllProprietarios(userId, status, nome, pagina, tamanho);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Proprietario> changeStatusProprietario(
            @PathVariable("id") String id) {
        return proprietarioService.changeStatusProprietario(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Proprietario> updateProprietario(
            @PathVariable("id") String id,
            @RequestBody ProprietarioDTO proprietarioDTO) {
        return proprietarioService.updateProprietario(id, proprietarioDTO);
    }


}