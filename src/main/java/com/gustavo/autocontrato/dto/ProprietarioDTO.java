package com.gustavo.autocontrato.dto;

import java.util.Date;

public record ProprietarioDTO(
        String nome,
        String rg,
        String cpf,
        String nacionalidade,
        Date nascimento,
        Boolean status
) {
}
