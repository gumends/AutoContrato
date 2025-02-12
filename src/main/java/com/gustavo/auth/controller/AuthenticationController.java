package com.gustavo.auth.controller;

import com.gustavo.auth.dto.AutenticateDTO;
import com.gustavo.auth.dto.LoginResponseDTO;
import com.gustavo.auth.dto.RegisterDto;
import com.gustavo.auth.infra.security.TokenService;
import com.gustavo.auth.model.Usuario;
import com.gustavo.auth.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody AutenticateDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDto registerDto){
        if(this.repository.findByLogin(registerDto.login()) != null) return ResponseEntity.badRequest().build();

        String encrypted = new BCryptPasswordEncoder().encode(registerDto.senha());
        Usuario newUser = new Usuario(
                registerDto.login(),
                encrypted,
                registerDto.nome(),
                registerDto.idade(),
                registerDto.email(),
                registerDto.role()
        );

        this.repository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
