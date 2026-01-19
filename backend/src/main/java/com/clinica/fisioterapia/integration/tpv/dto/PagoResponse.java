package com.clinica.fisioterapia.integration.tpv.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {

    private String transactionId;       // ID de transacción del TPV
    private String status;              // PENDING, SUCCESS, FAILED, CANCELLED
    private String paymentUrl;          // URL para redirigir al usuario al TPV
    private String message;             // Mensaje descriptivo
    private String errorCode;           // Código de error si aplica

    // Enum para estados
    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED,
        CANCELLED,
        REFUNDED
    }

    public boolean isSuccess() {
        return "SUCCESS".equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return "PENDING".equalsIgnoreCase(status);
    }
}
