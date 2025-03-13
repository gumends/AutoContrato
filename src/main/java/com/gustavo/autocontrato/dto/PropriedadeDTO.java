package com.gustavo.autocontrato.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

public record PropriedadeDTO(
        String rua,
        int numero,
        int numCasa,
        String bairro,
        String cep,
        String localizacao,
        BigDecimal aluguel,
        Date dataPagamento,
        String proprietarioID,
        String userId
) {
}
