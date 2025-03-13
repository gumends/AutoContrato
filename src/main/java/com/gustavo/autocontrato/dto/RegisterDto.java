package com.gustavo.autocontrato.dto;

import com.gustavo.autocontrato.model.UsuarioPermissao;

public record RegisterDto(
        String senha,
        UsuarioPermissao permissao,
        String nome,
        String cpf,
        String email
) {
}
