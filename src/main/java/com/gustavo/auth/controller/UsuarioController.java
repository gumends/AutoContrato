package com.gustavo.auth.controller;

import com.gustavo.auth.dto.PropriedadeDTO;
import com.gustavo.auth.dto.RegisterDto;
import com.gustavo.auth.dto.RoleDTO;
import com.gustavo.auth.model.UserRole;
import com.gustavo.auth.model.Usuario;
import com.gustavo.auth.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
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
            @RequestBody String senha,
            @PathVariable("id") String id
    ){
        return usuarioService.alterarSenha(id, senha);
    }

    @PatchMapping("/{id}/permissao")
    public ResponseEntity<Usuario> alterarRole(
            @RequestBody RoleDTO role,
            @PathVariable("id") String id
    ){
        return usuarioService.alterarRole(id, role);
    }
}
