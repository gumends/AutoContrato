package com.gustavo.auth.controller;

import com.gustavo.auth.dto.LocatarioDTO;
import com.gustavo.auth.dto.ProprietarioDTO;
import com.gustavo.auth.infra.security.TokenService;
import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.model.Propriedade;
import com.gustavo.auth.model.Proprietario;
import com.gustavo.auth.repository.PropriedadeRepository;
import com.gustavo.auth.service.ProprietarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("proprietario")
public class ProprietrioController {

    @Autowired
    private ProprietarioService proprietarioService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private PropriedadeRepository propriedade;

    @PostMapping("/criar")
    public ResponseEntity<Proprietario> criarLocatario(@RequestHeader("Authorization") String token, @RequestBody ProprietarioDTO proprietario){
        String id = tokenService.getUserIdFromToken(token);
        return proprietarioService.saveProprietario(proprietario, id);
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Proprietario>> buscarProprietarios(@RequestHeader("Authorization") String token){
        String id = tokenService.getUserIdFromToken(token);
        return proprietarioService.getAllProprietarios(id);
    }

    @DeleteMapping("/status/alterar/{id}")
    public ResponseEntity<Proprietario> alterarStatus(@PathVariable("id") String id){
        return proprietarioService.alterarStatusProprietario(id);
    }

    @PatchMapping("/atualizar/{id}")
    public ResponseEntity<Proprietario> atualizar(@PathVariable("id") String id, @RequestBody ProprietarioDTO proprietario){
        return proprietarioService.atualizarProprietario(id, proprietario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proprietario> buscarProprietario(@PathVariable("id") String id){
        return proprietarioService.getById(id);
    }

}
