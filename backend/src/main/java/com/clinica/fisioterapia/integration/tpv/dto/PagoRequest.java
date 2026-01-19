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
public class PagoRequest {

    private String merchantId;          // ID del comercio (clínica)
    private String orderId;             // ID único de la orden (cita)

    private BigDecimal amount;          // Importe en euros
    private String callbackUrl;         // URL de callback tras pago
    private String externalReference;

    private String currency;            // EUR
    private String description;         // Descripción del servicio
    private String customerEmail;       // Email del cliente
    private String customerName;        // Nombre del cliente
    private String successUrl;          // URL redirección éxito
    private String errorUrl;            // URL redirección error

    // Datos adicionales opcionales
    private String customerPhone;
    private String metadata;            // JSON con datos extra
}
