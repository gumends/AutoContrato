package com.gustavo.auth.dto;

import com.gustavo.auth.model.UsuarioPermissao;

public record RegisterDto(
        String senha,
        UsuarioPermissao permissao,
        String nome,
        String cpf,
        String email
) {
}
