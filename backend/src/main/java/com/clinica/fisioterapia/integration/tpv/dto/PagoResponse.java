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

    // Campos del TPV del Grupo 19
    private String token;               // Token único de sesión de pago
    private String paymentUrl;          // URL para redirigir al usuario al TPV
    private String status;              // PENDING, COMPLETED, FAILED, CANCELLED
    private String failureReason;       // Razón del fallo si aplica

    // Campos adicionales para uso interno
    private String transactionId;       // Alias de token para compatibilidad
    private String message;             // Mensaje descriptivo
    private String errorCode;           // Código de error si aplica

    // Estados del TPV Grupo 19
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_COMPLETED = "COMPLETED";
    public static final String STATUS_FAILED = "FAILED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_REFUNDED = "REFUNDED";

    public boolean isSuccess() {
        return STATUS_COMPLETED.equalsIgnoreCase(status);
    }

    public boolean isPending() {
        return STATUS_PENDING.equalsIgnoreCase(status);
    }

    public boolean isFailed() {
        return STATUS_FAILED.equalsIgnoreCase(status) || STATUS_CANCELLED.equalsIgnoreCase(status);
    }

    // Obtener el identificador de la transacción (token o transactionId)
    public String getTransactionToken() {
        return token != null ? token : transactionId;
    }
}
