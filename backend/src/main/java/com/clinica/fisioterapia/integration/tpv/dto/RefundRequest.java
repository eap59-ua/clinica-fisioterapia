package com.clinica.fisioterapia.integration.tpv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {
    private String transactionToken;    // Token de la transacción original
    private BigDecimal amount;          // Importe a reembolsar
}
