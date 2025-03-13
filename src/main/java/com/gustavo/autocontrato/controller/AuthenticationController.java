package com.gustavo.autocontrato.controller;

import com.gustavo.autocontrato.dto.AutenticateDTO;
import com.gustavo.autocontrato.dto.LoginResponseDTO;
import com.gustavo.autocontrato.dto.RegisterDto;
import com.gustavo.autocontrato.exception.ExceptionErroMessage;
import com.gustavo.autocontrato.infra.security.TokenService;
import com.gustavo.autocontrato.model.Usuario;
import com.gustavo.autocontrato.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AutenticateDTO data) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        Authentication auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto){
        UserDetails usuarioEmail = userRepository.findByEmail(registerDto.email());
        UserDetails usuarioCPF = userRepository.findByCpf(registerDto.cpf());
        if (usuarioEmail != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionErroMessage(
                    HttpStatus.BAD_REQUEST,
                    "E-mail já cadastrado, tente outro."
                ));
        }
        if (usuarioCPF != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionErroMessage(
                        HttpStatus.BAD_REQUEST,
                        "Usuário já cadastrada."
                ));
        }

        try {
            if(this.repository.findByEmail(registerDto.email()) != null) return ResponseEntity.badRequest().build();
            String encrypted = new BCryptPasswordEncoder().encode(registerDto.senha());
            Usuario newUser = new Usuario(
                    encrypted,
                    registerDto.nome(),
                    registerDto.cpf(),
                    registerDto.email(),
                    registerDto.permissao()
            );
            this.repository.save(newUser);
            return this.login(new AutenticateDTO(registerDto.email(), registerDto.senha()));
        }catch (Exception e){
            return ResponseEntity.status(401).body("Não foi possivel realizar o cadastro");
        }
    }
}
