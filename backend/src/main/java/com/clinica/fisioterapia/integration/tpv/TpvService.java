package com.clinica.fisioterapia.integration.tpv;

import com.clinica.fisioterapia.entity.Cita;
import com.clinica.fisioterapia.integration.tpv.dto.PagoRequest;
import com.clinica.fisioterapia.integration.tpv.dto.PagoResponse;
import com.clinica.fisioterapia.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TpvService {

    private final TpvClient tpvClient;
    private final TpvConfig tpvConfig;
    private final CitaRepository citaRepository;

    /**
     * Inicia el proceso de pago para una cita
     * Flujo TPV Grupo 19:
     * 1. POST /api/v1/payments/init con amount, callbackUrl, externalReference
     * 2. Recibimos token y paymentUrl
     * 3. Redirigimos al usuario a paymentUrl
     */
    @Transactional
    public PagoResponse iniciarPagoCita(Cita cita) {
        log.info("Iniciando pago para cita ID: {}", cita.getId());

        // Construir request según especificación TPV Grupo 19
        PagoRequest request = PagoRequest.builder()
            .amount(cita.getPrecioPagado())
            .callbackUrl(tpvConfig.getCallbackUrl())
            .externalReference("CITA_" + cita.getId())
            // Campos adicionales para uso interno y mock mode
            .orderId(String.valueOf(cita.getId()))
            .description("Cita: " + cita.getServicio().getNombre())
            .customerEmail(cita.getCliente().getEmail())
            .customerName(cita.getCliente().getNombre() + " " + cita.getCliente().getApellidos())
            .build();

        // Llamar al TPV
        PagoResponse response = tpvClient.iniciarPago(request);

        // Guardar token de transacción en la cita
        String token = response.getTransactionToken();
        if (token != null) {
            cita.setTransaccionTpvId(token);
            cita.setEstadoPago("PENDIENTE");
            citaRepository.save(cita);
            log.info("Token TPV guardado: {}", token);
        }

        return response;
    }

    /**
     * Procesa el callback del TPV (notificación de pago)
     * El TPV Grupo 19 redirige a callbackUrl con ?token=...&status=...
     * Luego debemos verificar con GET /api/v1/payments/verify/{token}
     */
    @Transactional
    public PagoResponse procesarCallbackPago(String token, String status) {
        log.info("Procesando callback TPV: token={}, status={}", token, status);

        // Buscar la cita por el token de transacción
        Cita cita = citaRepository.findByTransaccionTpvId(token)
            .orElseThrow(() -> new RuntimeException("No se encontró cita con token: " + token));

        // Verificar el estado real con el TPV (paso importante de seguridad)
        PagoResponse verifyResponse = tpvClient.verificarPago(token);
        String realStatus = verifyResponse.getStatus();

        log.info("Estado verificado del pago: token={}, status={}", token, realStatus);

        // Actualizar estado del pago según la verificación
        actualizarEstadoPago(cita, realStatus);
        citaRepository.save(cita);

        return verifyResponse;
    }

    /**
     * Procesa callback por citaId (para mock mode y compatibilidad)
     */
    @Transactional
    public void procesarCallbackPago(Long citaId, String transactionId, String status) {
        log.info("Procesando callback TPV (legacy): citaId={}, txnId={}, status={}", citaId, transactionId, status);

        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));

        // Verificar que la transacción coincide
        if (transactionId != null && !transactionId.equals(cita.getTransaccionTpvId())) {
            log.warn("Transaction ID no coincide. Esperado: {}, Recibido: {}",
                cita.getTransaccionTpvId(), transactionId);
        }

        actualizarEstadoPago(cita, status);
        citaRepository.save(cita);
    }

    private void actualizarEstadoPago(Cita cita, String status) {
        switch (status.toUpperCase()) {
            case "SUCCESS":
            case "COMPLETED":
                cita.setEstadoPago("COMPLETADO");
                log.info("Pago completado para cita {}", cita.getId());
                break;
            case "FAILED":
            case "DECLINED":
                cita.setEstadoPago("FALLIDO");
                log.warn("Pago fallido para cita {}", cita.getId());
                break;
            case "CANCELLED":
                cita.setEstadoPago("CANCELADO");
                log.info("Pago cancelado para cita {}", cita.getId());
                break;
            case "REFUNDED":
                cita.setEstadoPago("REEMBOLSADO");
                log.info("Pago reembolsado para cita {}", cita.getId());
                break;
            default:
                cita.setEstadoPago("PENDIENTE");
                log.info("Estado de pago: {}", status);
        }
    }

    /**
     * Verifica el estado actual de un pago
     */
    public PagoResponse verificarEstadoPago(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));

        if (cita.getTransaccionTpvId() == null) {
            return PagoResponse.builder()
                .status("NOT_INITIATED")
                .message("No se ha iniciado ningún pago para esta cita")
                .build();
        }

        return tpvClient.verificarPago(cita.getTransaccionTpvId());
    }

    /**
     * Solicita reembolso de una cita cancelada
     */
    @Transactional
    public PagoResponse solicitarReembolso(Long citaId) {
        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));

        if (!"COMPLETADO".equals(cita.getEstadoPago())) {
            return PagoResponse.builder()
                .status(PagoResponse.STATUS_FAILED)
                .message("Solo se pueden reembolsar pagos completados")
                .build();
        }

        // Solicitar reembolso con el monto original
        PagoResponse response = tpvClient.solicitarReembolso(
            cita.getTransaccionTpvId(),
            cita.getPrecioPagado()
        );

        if (response.isSuccess() || PagoResponse.STATUS_REFUNDED.equals(response.getStatus())) {
            cita.setEstadoPago("REEMBOLSADO");
            citaRepository.save(cita);
        }

        return response;
    }
}
