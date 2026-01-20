package com.clinica.fisioterapia.integration.tpv;

import com.clinica.fisioterapia.integration.tpv.dto.PagoRequest;
import com.clinica.fisioterapia.integration.tpv.dto.PagoResponse;
import com.clinica.fisioterapia.integration.tpv.dto.RefundRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class TpvClient {

    private final TpvConfig config;
    private final RestTemplate restTemplate;

    // Endpoints del TPV Grupo 19
    private static final String ENDPOINT_INIT = "/api/v1/payments/init";
    private static final String ENDPOINT_VERIFY = "/api/v1/payments/verify/";
    private static final String ENDPOINT_REFUND = "/api/v1/refunds/external";

    /**
     * Inicia un pago en el TPV externo (Grupo 19)
     * POST /api/v1/payments/init
     */
    public PagoResponse iniciarPago(PagoRequest request) {
        log.info("Iniciando pago en TPV: orderId={}, amount={}", request.getOrderId(), request.getAmount());

        // Modo simulación para desarrollo
        if (config.isMockMode()) {
            return mockIniciarPago(request);
        }

        try {
            HttpHeaders headers = createHeaders();

            // Crear el body según la especificación del TPV Grupo 19
            Map<String, Object> body = new HashMap<>();
            body.put("amount", request.getAmount());
            body.put("callbackUrl", request.getCallbackUrl());
            body.put("externalReference", request.getExternalReference());

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            String url = config.getBaseUrl() + ENDPOINT_INIT;
            log.debug("Llamando a TPV: POST {}", url);

            ResponseEntity<PagoResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    PagoResponse.class
            );

            PagoResponse pagoResponse = response.getBody();
            if (pagoResponse != null) {
                // El TPV devuelve token y paymentUrl
                pagoResponse.setStatus(PagoResponse.STATUS_PENDING);
                pagoResponse.setTransactionId(pagoResponse.getToken()); // Alias para compatibilidad
                log.info("Respuesta TPV: token={}, paymentUrl={}",
                        pagoResponse.getToken(),
                        pagoResponse.getPaymentUrl());
            }

            return pagoResponse;

        } catch (RestClientException e) {
            log.error("Error al conectar con TPV: {}", e.getMessage(), e);
            return PagoResponse.builder()
                    .status(PagoResponse.STATUS_FAILED)
                    .errorCode("TPV_CONNECTION_ERROR")
                    .message("No se pudo conectar con el servidor de pagos: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Verifica el estado de un pago (Grupo 19)
     * GET /api/v1/payments/verify/{token}
     */
    public PagoResponse verificarPago(String token) {
        log.info("Verificando estado de pago: token={}", token);

        // Modo simulación
        if (config.isMockMode()) {
            return mockVerificarPago(token);
        }

        try {
            HttpHeaders headers = createHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            String url = config.getBaseUrl() + ENDPOINT_VERIFY + token;
            log.debug("Llamando a TPV: GET {}", url);

            ResponseEntity<PagoResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    PagoResponse.class
            );

            PagoResponse pagoResponse = response.getBody();
            if (pagoResponse != null) {
                pagoResponse.setToken(token);
                pagoResponse.setTransactionId(token);
                log.info("Estado del pago: token={}, status={}", token, pagoResponse.getStatus());
            }

            return pagoResponse;

        } catch (RestClientException e) {
            log.error("Error al verificar pago: {}", e.getMessage(), e);
            return PagoResponse.builder()
                    .token(token)
                    .transactionId(token)
                    .status("UNKNOWN")
                    .message("No se pudo verificar el estado del pago: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Solicita reembolso de un pago (Grupo 19)
     * POST /api/v1/refunds/external
     */
    public PagoResponse solicitarReembolso(String token, BigDecimal amount) {
        log.info("Solicitando reembolso: token={}, amount={}", token, amount);

        if (config.isMockMode()) {
            return PagoResponse.builder()
                    .token(token)
                    .transactionId(token)
                    .status(PagoResponse.STATUS_REFUNDED)
                    .message("Reembolso procesado correctamente (MOCK)")
                    .build();
        }

        try {
            HttpHeaders headers = createHeaders();

            RefundRequest refundRequest = RefundRequest.builder()
                    .transactionToken(token)
                    .amount(amount)
                    .build();

            HttpEntity<RefundRequest> entity = new HttpEntity<>(refundRequest, headers);

            String url = config.getBaseUrl() + ENDPOINT_REFUND;
            log.debug("Llamando a TPV: POST {}", url);

            ResponseEntity<Map> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            Map responseBody = response.getBody();
            log.info("Respuesta reembolso: {}", responseBody);

            return PagoResponse.builder()
                    .token(token)
                    .transactionId(token)
                    .status(PagoResponse.STATUS_REFUNDED)
                    .message("Reembolso procesado correctamente")
                    .build();

        } catch (RestClientException e) {
            log.error("Error al solicitar reembolso: {}", e.getMessage(), e);
            return PagoResponse.builder()
                    .token(token)
                    .transactionId(token)
                    .status(PagoResponse.STATUS_FAILED)
                    .message("Error al procesar el reembolso: " + e.getMessage())
                    .build();
        }
    }

    /**
     * Método legacy para compatibilidad
     */
    public PagoResponse solicitarReembolso(String token) {
        // Reembolso total - necesitamos obtener el monto original
        // Por ahora usamos un valor placeholder que debería ser reemplazado
        return solicitarReembolso(token, null);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // El TPV Grupo 19 usa X-API-KEY para autenticación
        headers.set("X-API-KEY", config.getApiKey());
        return headers;
    }

    // ========== MOCK METHODS (para desarrollo) ==========

    private PagoResponse mockIniciarPago(PagoRequest request) {
        String mockToken = "MOCK_TXN_" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        log.info("[MOCK] Pago simulado iniciado: token={}", mockToken);

        return PagoResponse.builder()
                .token(mockToken)
                .transactionId(mockToken)
                .status(PagoResponse.STATUS_PENDING)
                .paymentUrl("http://localhost:5173/cliente/mock-pago?txn=" + mockToken
                        + "&amount=" + request.getAmount()
                        + "&orderId=" + request.getOrderId())
                .message("Pago iniciado correctamente (MOCK)")
                .build();
    }

    private PagoResponse mockVerificarPago(String token) {
        log.info("[MOCK] Verificando pago simulado: token={}", token);

        return PagoResponse.builder()
                .token(token)
                .transactionId(token)
                .status(PagoResponse.STATUS_COMPLETED)
                .message("Pago completado correctamente (MOCK)")
                .build();
    }
}
