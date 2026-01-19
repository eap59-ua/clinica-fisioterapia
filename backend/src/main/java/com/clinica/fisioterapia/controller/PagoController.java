package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Cita;
import com.clinica.fisioterapia.integration.tpv.TpvService;
import com.clinica.fisioterapia.integration.tpv.dto.PagoResponse;
import com.clinica.fisioterapia.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final TpvService tpvService;
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
     * Callback del TPV (recibe notificación de pago)
     * POST /api/pagos/callback
     */
    @PostMapping("/callback")
    public ResponseEntity<String> callbackPago(
            @RequestParam Long citaId,
            @RequestBody Map<String, Object> payload) {

        log.info("Callback TPV recibido para cita: {}", citaId);
        log.debug("Payload: {}", payload);

        String transactionId = (String) payload.getOrDefault("transactionId", "");
        String status = (String) payload.getOrDefault("status", "UNKNOWN");

        tpvService.procesarCallbackPago(citaId, transactionId, status);

        return ResponseEntity.ok("OK");
    }

    /**
     * Callback GET para redirección del TPV (usuario vuelve del TPV)
     * GET /api/pagos/callback
     */
    @GetMapping("/callback")
    public ResponseEntity<String> callbackPagoGet(
            @RequestParam Long citaId,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String status) {

        log.info("Callback GET TPV: citaId={}, txnId={}, status={}", citaId, transactionId, status);

        if (transactionId != null && status != null) {
            tpvService.procesarCallbackPago(citaId, transactionId, status);
        }

        // Redirigir al frontend
        return ResponseEntity.ok("Pago procesado. Puedes cerrar esta ventana.");
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
