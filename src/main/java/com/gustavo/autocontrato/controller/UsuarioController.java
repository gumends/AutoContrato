package com.gustavo.autocontrato.controller;

import com.gustavo.autocontrato.dto.PermissaoDTO;
import com.gustavo.autocontrato.dto.RegisterDto;
import com.gustavo.autocontrato.dto.SenhaDTO;
import com.gustavo.autocontrato.model.Usuario;
import com.gustavo.autocontrato.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "Bearer Authentication")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public Page<Usuario> buscaUsuarios(
            @RequestParam(defaultValue = "") String nome,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "10") int tamanho
    ){
        return usuarioService.buscaUsuarios(nome, pagina, tamanho);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscaUsuario(
            @PathVariable("id") String id
    ){
        return ResponseEntity.ok(usuarioService.buscaUsuario(id).getBody().orElseThrow());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Usuario> atualizaUsuario(
            @RequestBody RegisterDto registerDto,
            @PathVariable("id") String id
    ){
        return usuarioService.alterarUsuario(id, registerDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(
            @PathVariable("id") String id
    ){
        return usuarioService.deletarUsuario(id);
    }

    @PatchMapping("/{id}/senha")
    public ResponseEntity<Usuario> novaSenha(
            @RequestBody SenhaDTO senha,
            @PathVariable("id") String id
    ){
        return usuarioService.alterarSenha(id, senha);
    }

    @PatchMapping("/{id}/permissao")
    public ResponseEntity<Usuario> alterarRole(
            @RequestBody PermissaoDTO role,
            @PathVariable("id") String id
    ){
        return usuarioService.alterarRole(id, role);
    }
}
