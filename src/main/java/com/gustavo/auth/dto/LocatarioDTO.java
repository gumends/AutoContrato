package com.gustavo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gustavo.auth.model.Propriedade;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

public record LocatarioDTO(
    String nome,
    String rg,
    String cpf,
    Date nascimento,
    UUID propriedadeId
) {
}
