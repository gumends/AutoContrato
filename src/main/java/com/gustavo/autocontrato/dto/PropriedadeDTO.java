package com.gustavo.autocontrato.dto;

import java.math.BigDecimal;

public record PropriedadeDTO(
        String id,
        String rua,
        int numero,
        int numCasa,
        String bairro,
        String cep,
        String localizacao,
        BigDecimal aluguel,
        String dataPagamento,
        String locatarioID,
        String proprietarioID,
        String userId
) {
}
