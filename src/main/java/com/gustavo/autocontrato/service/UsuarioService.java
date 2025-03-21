package com.gustavo.autocontrato.service;

import com.gustavo.autocontrato.dto.PermissaoDTO;
import com.gustavo.autocontrato.dto.RegisterDto;
import com.gustavo.autocontrato.dto.SenhaDTO;
import com.gustavo.autocontrato.exception.exceptions.EventNotFoundException;
import com.gustavo.autocontrato.model.Usuario;
import com.gustavo.autocontrato.model.UsuarioPermissao;
import com.gustavo.autocontrato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    UserRepository userRepository;

    public List<Usuario> buscaUsuarioPorNome(){
        return userRepository.findAll();
    }

    public Page<Usuario> buscaUsuarios(String nome, int pagina, int tamanho){
        Pageable pageable = PageRequest.of(pagina, tamanho);
        return userRepository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    public ResponseEntity<Optional<Usuario>> buscaUsuario(String id){
        return ResponseEntity.ok(userRepository.findById(id));
    }

    public ResponseEntity<Usuario> alterarUsuario(String id, RegisterDto usuario){
        Usuario u = userRepository.findById(id).orElseThrow();
        u.setNome(usuario.nome());
        u.setEmail(usuario.email());
        u.setCpf(usuario.cpf());

        return ResponseEntity.ok(userRepository.save(u));
    }

    public ResponseEntity<Usuario> alterarRole(String id, PermissaoDTO roleDTO) {
        Usuario u = userRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Usuário não encontrado"));

        if (roleDTO.getPermissao().equalsIgnoreCase("ADMIN")) {
            u.setPermissao(UsuarioPermissao.ADMIN);
        } else if (roleDTO.getPermissao().equalsIgnoreCase("USER")) {
            u.setPermissao(UsuarioPermissao.USER);
        } else {
            throw new EventNotFoundException("Tipo de usuário não encontrado");
        }

        return ResponseEntity.ok(userRepository.save(u));
    }

    public ResponseEntity<Usuario> alterarSenha(String id, SenhaDTO senhaDTO){
        Usuario u = userRepository.findById(id).orElseThrow();
        String encrypted = new BCryptPasswordEncoder().encode(senhaDTO.senha());
        u.setSenha(encrypted);
        return ResponseEntity.ok(userRepository.save(u));
    }

    public ResponseEntity<String> deletarUsuario(String id){
        userRepository.deleteById(id);
        return ResponseEntity.ok("{\"mensagem\": \"Usuario deletado com sucesso\"}");
    }
}
