package com.gustavo.auth.controller;

import com.gustavo.auth.dto.ProprietarioDTO;
import com.gustavo.auth.infra.security.TokenService;
import com.gustavo.auth.model.Proprietario;
import com.gustavo.auth.repository.ProprietarioRepository;
import com.gustavo.auth.service.PropriedadeService;
import com.gustavo.auth.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proprietarios") // Recurso no plural
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

    @GetMapping("/{status}")
    public ResponseEntity<List<Proprietario>> buscaTodosProprietarios(
            @RequestHeader("Authorization") String token,
            @PathVariable("status") boolean status
    ){
        String userId = getUserIdFromToken(token);
        return ResponseEntity.ok(proprietarioService.buscaTodosProprietarios(userId, status).getBody());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> buscarProprietario(
            @RequestHeader("Authorization") String token,
            @PathVariable("id") String id) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.buscaProprietario(id, userId);
    }

    @PostMapping
    public ResponseEntity<Proprietario> criarProprietario(
            @RequestHeader("Authorization") String token,
            @RequestBody ProprietarioDTO proprietarioDTO) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.saveProprietario(proprietarioDTO, userId);
    }

    @GetMapping
    public Page<Proprietario> buscarProprietarios(
            @RequestHeader("Authorization") String token,
            @RequestParam(defaultValue = "true") boolean status,
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho) {
        String userId = getUserIdFromToken(token);
        return proprietarioService.getAllProprietarios(userId, status, nome, pagina, tamanho);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Proprietario> alterarStatusProprietario(
            @PathVariable("id") String id) {
        return proprietarioService.alterarStatusProprietario(id);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Proprietario> atualizarProprietario(
            @PathVariable("id") String id,
            @RequestBody ProprietarioDTO proprietarioDTO) {
        return proprietarioService.atualizarProprietario(id, proprietarioDTO);
    }


}