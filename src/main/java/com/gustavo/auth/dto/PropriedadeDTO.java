package com.gustavo.auth.dto;

import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.model.Proprietario;

import java.math.BigDecimal;
import java.util.UUID;

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
