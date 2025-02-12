package com.gustavo.auth.dto;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record ProprietarioDTO(
        String nome,
        String rg,
        String cpf,
        String nacionalidade,
        Date nascimento,
        Set<String> propriedadeId,
        String userId
) {
}
