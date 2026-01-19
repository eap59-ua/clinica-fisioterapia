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
     */
    @Transactional
    public PagoResponse iniciarPagoCita(Cita cita) {
        log.info("Iniciando pago para cita ID: {}", cita.getId());

        // Construir request de pago
        PagoRequest request = PagoRequest.builder()
            .merchantId(tpvConfig.getMerchantId())
            .orderId("CITA_" + cita.getId())
            .amount(cita.getPrecioPagado())
            .currency("EUR")
            .description("Cita: " + cita.getServicio().getNombre())
            .customerEmail(cita.getCliente().getEmail())
            .customerName(cita.getCliente().getNombre() + " " + cita.getCliente().getApellidos())
            .customerPhone(cita.getCliente().getTelefono())
            .callbackUrl(tpvConfig.getCallbackUrl() + "?citaId=" + cita.getId())
            .successUrl(tpvConfig.getSuccessUrl() + "?citaId=" + cita.getId())
            .errorUrl(tpvConfig.getErrorUrl() + "?citaId=" + cita.getId())
            .build();

        // Llamar al TPV
        PagoResponse response = tpvClient.iniciarPago(request);

        // Guardar ID de transacción en la cita
        if (response.getTransactionId() != null) {
            cita.setTransaccionTpvId(response.getTransactionId());
            cita.setEstadoPago("PENDIENTE");
            citaRepository.save(cita);
            log.info("Transacción TPV guardada: {}", response.getTransactionId());
        }

        return response;
    }

    /**
     * Procesa el callback del TPV (notificación de pago)
     */
    @Transactional
    public void procesarCallbackPago(Long citaId, String transactionId, String status) {
        log.info("Procesando callback TPV: citaId={}, txnId={}, status={}", citaId, transactionId, status);

        Cita cita = citaRepository.findById(citaId)
            .orElseThrow(() -> new RuntimeException("Cita no encontrada: " + citaId));

        // Verificar que la transacción coincide
        if (!transactionId.equals(cita.getTransaccionTpvId())) {
            log.warn("Transaction ID no coincide. Esperado: {}, Recibido: {}",
                cita.getTransaccionTpvId(), transactionId);
            // En producción, esto sería un error de seguridad
        }

        // Actualizar estado del pago
        switch (status.toUpperCase()) {
            case "SUCCESS":
            case "COMPLETED":
                cita.setEstadoPago("COMPLETADO");
                log.info("Pago completado para cita {}", citaId);
                break;
            case "FAILED":
            case "DECLINED":
                cita.setEstadoPago("FALLIDO");
                log.warn("Pago fallido para cita {}", citaId);
                break;
            case "CANCELLED":
                cita.setEstadoPago("CANCELADO");
                log.info("Pago cancelado para cita {}", citaId);
                break;
            default:
                cita.setEstadoPago("PENDIENTE");
                log.info("Estado de pago desconocido: {}", status);
        }

        citaRepository.save(cita);
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
                .status("FAILED")
                .message("Solo se pueden reembolsar pagos completados")
                .build();
        }

        PagoResponse response = tpvClient.solicitarReembolso(cita.getTransaccionTpvId());

        if (response.isSuccess() || "REFUNDED".equals(response.getStatus())) {
            cita.setEstadoPago("REEMBOLSADO");
            citaRepository.save(cita);
        }

        return response;
    }
}
