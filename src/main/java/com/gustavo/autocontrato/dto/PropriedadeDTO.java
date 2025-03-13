package com.gustavo.autocontrato.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PropriedadeDTO(
        String rua,
        int numero,
        int numCasa,
        String bairro,
        String cep,
        String localizacao,
        BigDecimal aluguel,
        LocalDateTime dataPagamento,
        String proprietarioID,
        String userId
) {
}
