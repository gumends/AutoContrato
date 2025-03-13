package com.gustavo.autocontrato.dto;

import java.util.Date;

public record LocatarioDTO(
    String nome,
    String rg,
    String cpf,
    Date nascimento,
    String propriedadeId,
    String userId,
    boolean status,
    boolean alocado
) {
}
