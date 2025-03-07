package com.gustavo.auth.dto;

import com.gustavo.auth.model.UserRole;

public record RegisterDto(
        String senha,
        UserRole role,
        String nome,
        String cpf,
        String email
) {
}
