package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Cita;
import com.clinica.fisioterapia.integration.tpv.TpvConfig;
import com.clinica.fisioterapia.integration.tpv.TpvService;
import com.clinica.fisioterapia.integration.tpv.dto.PagoResponse;
import com.clinica.fisioterapia.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final TpvService tpvService;
    private final TpvConfig tpvConfig;
    private final CitaRepository citaRepository;

    /**
     * Inicia el proceso de pago para una cita
     * POST /api/pagos/iniciar/{citaId}
     */
    @PostMapping("/iniciar/{citaId}")
    public ResponseEntity<PagoResponse> iniciarPago(@PathVariable Long citaId) {
        log.info("Iniciando pago para cita: {}", citaId);

        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        PagoResponse response = tpvService.iniciarPagoCita(cita);

        return ResponseEntity.ok(response);
    }

    /**
     * Callback del TPV Grupo 19 (recibe notificación de pago)
     * El TPV redirige aquí con ?token=...&status=...
     * GET /api/pagos/callback
     */
    @GetMapping("/callback")
    public ResponseEntity<Void> callbackPagoGet(
            @RequestParam(required = false) String token,
            @RequestParam(required = false) String status,
            // Parámetros legacy para compatibilidad con mock mode
            @RequestParam(required = false) Long citaId,
            @RequestParam(required = false) String transactionId) {

        log.info("Callback TPV recibido: token={}, status={}, citaId={}", token, status, citaId);

        String redirectUrl;

        try {
            if (token != null && status != null) {
                // Flujo TPV Grupo 19: verificar y procesar el pago
                PagoResponse verifyResponse = tpvService.procesarCallbackPago(token, status);

                if (verifyResponse.isSuccess()) {
                    // Buscar la cita para obtener el ID para la redirección
                    Cita cita = citaRepository.findByTransaccionTpvId(token).orElse(null);
                    Long citaIdForRedirect = cita != null ? cita.getId() : null;
                    redirectUrl = tpvConfig.getSuccessUrl() + "?citaId=" + citaIdForRedirect;
                } else {
                    Cita cita = citaRepository.findByTransaccionTpvId(token).orElse(null);
                    Long citaIdForRedirect = cita != null ? cita.getId() : null;
                    redirectUrl = tpvConfig.getErrorUrl() + "?citaId=" + citaIdForRedirect + "&error=" + verifyResponse.getFailureReason();
                }
            } else if (citaId != null) {
                // Flujo legacy/mock mode
                if (transactionId != null && status != null) {
                    tpvService.procesarCallbackPago(citaId, transactionId, status);
                }
                redirectUrl = "COMPLETED".equalsIgnoreCase(status) || "SUCCESS".equalsIgnoreCase(status)
                    ? tpvConfig.getSuccessUrl() + "?citaId=" + citaId
                    : tpvConfig.getErrorUrl() + "?citaId=" + citaId;
            } else {
                log.warn("Callback sin parámetros válidos");
                redirectUrl = tpvConfig.getErrorUrl() + "?error=invalid_callback";
            }

            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();

        } catch (Exception e) {
            log.error("Error procesando callback: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(tpvConfig.getErrorUrl() + "?error=" + e.getMessage()))
                .build();
        }
    }

    /**
     * Callback POST del TPV (alternativo)
     * POST /api/pagos/callback
     */
    @PostMapping("/callback")
    public ResponseEntity<String> callbackPagoPost(
            @RequestParam(required = false) Long citaId,
            @RequestParam(required = false) String token,
            @RequestBody(required = false) Map<String, Object> payload) {

        log.info("Callback POST TPV recibido: citaId={}, token={}", citaId, token);
        log.debug("Payload: {}", payload);

        try {
            if (token != null) {
                String status = payload != null ? (String) payload.getOrDefault("status", "UNKNOWN") : "UNKNOWN";
                tpvService.procesarCallbackPago(token, status);
            } else if (citaId != null && payload != null) {
                String transactionId = (String) payload.getOrDefault("transactionId", "");
                String status = (String) payload.getOrDefault("status", "UNKNOWN");
                tpvService.procesarCallbackPago(citaId, transactionId, status);
            }
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            log.error("Error procesando callback POST: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    /**
     * Verifica el estado de pago de una cita
     * GET /api/pagos/estado/{citaId}
     */
    @GetMapping("/estado/{citaId}")
    public ResponseEntity<PagoResponse> verificarEstado(@PathVariable Long citaId) {
        PagoResponse response = tpvService.verificarEstadoPago(citaId);
        return ResponseEntity.ok(response);
    }

    /**
     * Solicita reembolso de una cita
     * POST /api/pagos/reembolso/{citaId}
     */
    @PostMapping("/reembolso/{citaId}")
    public ResponseEntity<PagoResponse> solicitarReembolso(@PathVariable Long citaId) {
        log.info("Solicitando reembolso para cita: {}", citaId);
        PagoResponse response = tpvService.solicitarReembolso(citaId);
        return ResponseEntity.ok(response);
    }

    /**
     * Simula confirmación de pago (solo para modo mock/desarrollo)
     * POST /api/pagos/mock-confirmar/{citaId}
     */
    @PostMapping("/mock-confirmar/{citaId}")
    public ResponseEntity<String> mockConfirmarPago(@PathVariable Long citaId) {
        log.info("[MOCK] Confirmando pago para cita: {}", citaId);

        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstadoPago("COMPLETADO");
        citaRepository.save(cita);

        return ResponseEntity.ok("Pago confirmado (MOCK)");
    }
}
