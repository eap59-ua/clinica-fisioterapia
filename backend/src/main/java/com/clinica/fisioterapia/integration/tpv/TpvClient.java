package com.clinica.fisioterapia.integration.tpv;

import com.clinica.fisioterapia.integration.tpv.dto.PagoRequest;
import com.clinica.fisioterapia.integration.tpv.dto.PagoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TpvClient {

    private final TpvConfig config;
    private final RestTemplate restTemplate;

    /**
     * Inicia un pago en el TPV externo
     * @param request Datos del pago
     * @return Respuesta con URL de pago o error
     */
    public PagoResponse iniciarPago(PagoRequest request) {
        log.info("Iniciando pago en TPV: orderId={}, amount={}", request.getOrderId(), request.getAmount());

        // Modo simulación para desarrollo
        if (config.isMockMode()) {
            return mockIniciarPago(request);
        }

        try {
            HttpHeaders headers = createHeaders();

            HttpEntity<PagoRequest> entity = new HttpEntity<>(request, headers);

            // TODO: Ajustar endpoint según documentación real del TPV
            String url = config.getBaseUrl() + "/api/v1/payments/init";

            ResponseEntity<PagoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                PagoResponse.class
            );

            log.info("Respuesta TPV: status={}, transactionId={}",
                response.getBody().getStatus(),
                response.getBody().getTransactionId());

            return response.getBody();

        } catch (RestClientException e) {
            log.error("Error al conectar con TPV: {}", e.getMessage());
            return PagoResponse.builder()
                .status("FAILED")
                .errorCode("TPV_CONNECTION_ERROR")
                .message("No se pudo conectar con el servidor de pagos. Intente más tarde.")
                .build();
        }
    }

    /**
     * Verifica el estado de un pago
     * @param  ID de la transacción
     * @return Estado actual del pago
     */
    public PagoResponse verificarPago(String token) {
        log.info("Verificando estado de pago: transactionId={}", token);

        // Modo simulación
        if (config.isMockMode()) {
            return mockVerificarPago(token);
        }

        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            // TODO: Ajustar endpoint según documentación real del TPV
            String url = config.getBaseUrl() + "/api/v1/payments/verify" + token;

            ResponseEntity<PagoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PagoResponse.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            log.error("Error al verificar pago: {}", e.getMessage());
            return PagoResponse.builder()
                .transactionId(token)
                .status("UNKNOWN")
                .message("No se pudo verificar el estado del pago")
                .build();
        }
    }

    /**
     * Solicita reembolso de un pago
     * @param transactionId ID de la transacción original
     * @return Resultado del reembolso
     */
    public PagoResponse solicitarReembolso(String transactionId) {
        log.info("Solicitando reembolso: transactionId={}", transactionId);

        if (config.isMockMode()) {
            return PagoResponse.builder()
                .transactionId(transactionId)
                .status("REFUNDED")
                .message("Reembolso procesado correctamente (MOCK)")
                .build();
        }

        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = config.getBaseUrl() + "/api/payments/" + transactionId + "/refund";

            ResponseEntity<PagoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                PagoResponse.class
            );

            return response.getBody();

        } catch (RestClientException e) {
            log.error("Error al solicitar reembolso: {}", e.getMessage());
            return PagoResponse.builder()
                .transactionId(transactionId)
                .status("FAILED")
                .message("Error al procesar el reembolso")
                .build();
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-Key", config.getApiKey());
        headers.set("X-Merchant-Id", config.getMerchantId());
        // TODO: Añadir firma HMAC si el TPV lo requiere
        return headers;
    }

    // ========== MOCK METHODS (para desarrollo) ==========

    private PagoResponse mockIniciarPago(PagoRequest request) {
        String mockTransactionId = "MOCK_TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("[MOCK] Pago simulado iniciado: {}", mockTransactionId);

        return PagoResponse.builder()
            .transactionId(mockTransactionId)
            .status("PENDING")
            .paymentUrl("http://localhost:5173/cliente/mock-pago?txn=" + mockTransactionId + "&amount=" + request.getAmount())
            .message("Pago iniciado correctamente (MOCK)")
            .build();
    }

    private PagoResponse mockVerificarPago(String transactionId) {
        // En modo mock, siempre devolvemos SUCCESS para facilitar pruebas
        return PagoResponse.builder()
            .transactionId(transactionId)
            .status("SUCCESS")
            .message("Pago completado correctamente (MOCK)")
            .build();
    }
}
