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

    // Campos requeridos por el TPV del Grupo 19
    private BigDecimal amount;          // Importe en euros (requerido)
    private String callbackUrl;         // URL de callback tras pago (requerido)
    private String externalReference;   // Referencia externa - usamos citaId (opcional)

    // Campos adicionales para uso interno
    private String orderId;             // ID de la cita (para compatibilidad)
    private String description;         // Descripción del servicio
    private String customerEmail;       // Email del cliente
    private String customerName;        // Nombre del cliente
}
