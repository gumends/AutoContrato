package com.gustavo.auth.dto;

import com.gustavo.auth.model.Locatario;
import com.gustavo.auth.model.Proprietario;

import java.util.UUID;

public record PropriedadeDTO(
        String id,
        String rua,
        int numero,
        int numCasa,
        String bairro,
        String cep,
        String localizacao,
        String aluguel,
        String dataPagamento,
        String locatarioID,
        String proprietario
) {
}
