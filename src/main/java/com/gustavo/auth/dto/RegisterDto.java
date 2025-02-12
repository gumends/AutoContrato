package com.gustavo.auth.dto;

import com.gustavo.auth.model.UserRole;

public record RegisterDto(
        String login,
        String senha,
        UserRole role,
        String nome,
        int idade,
        String email
) {
}
