package com.gustavo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gustavo.auth.model.Propriedade;
import jakarta.persistence.*;

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
