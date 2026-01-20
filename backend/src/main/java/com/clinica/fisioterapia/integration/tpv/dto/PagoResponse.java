package com.clinica.fisioterapia.integration.tpv.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagoResponse {


    private String message;             // Mensaje descriptivo
    private String errorCode;           // Código de error si aplica


    @JsonAlias("token")
    private String transactionId;

    private String paymentUrl;          // URL para redirigir al usuario al TPV

    private String status;              // PENDING, SUCCESS, FAILED, CANCELLED

    // Enum para estados
    public enum PaymentStatus {
        PENDING,
        SUCCESS,
        FAILED,
        CANCELLED,
        REFUNDED
    }


}
