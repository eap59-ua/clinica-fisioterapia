package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.*;
import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.service.FisioterapeutaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fisioterapeuta")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('FISIOTERAPEUTA')")
public class FisioterapeutaController {

    private final FisioterapeutaService fisioterapeutaService;

    /**
     * GET /api/fisioterapeuta/citas
     * Obtener todas las citas del fisioterapeuta con filtro opcional por fecha
     */
    @GetMapping("/citas")
    public ResponseEntity<List<CitaDTO>> getMisCitas(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        List<CitaDTO> citas = fisioterapeutaService.getMisCitas(fisioterapeutaId, fecha);
        return ResponseEntity.ok(citas);
    }

    /**
     * GET /api/fisioterapeuta/citas/hoy
     * Obtener citas de hoy (para Dashboard)
     */
    @GetMapping("/citas/hoy")
    public ResponseEntity<List<CitaDTO>> getMisCitasHoy(@AuthenticationPrincipal Usuario usuario) {
        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        List<CitaDTO> citas = fisioterapeutaService.getMisCitasHoy(fisioterapeutaId);
        return ResponseEntity.ok(citas);
    }

    /**
     * GET /api/fisioterapeuta/citas/semana
     * Obtener citas de una semana completa (para AgendaSemanal)
     */
    @GetMapping("/citas/semana")
    public ResponseEntity<List<CitaDTO>> getMisCitasSemana(
            @AuthenticationPrincipal Usuario usuario,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();

        // Si no se proporciona fecha, usar el lunes de esta semana
        LocalDate inicio = fechaInicio != null ? fechaInicio : obtenerLunesSemanaActual();

        List<CitaDTO> citas = fisioterapeutaService.getMisCitasSemana(fisioterapeutaId, inicio);
        return ResponseEntity.ok(citas);
    }

    /**
     * GET /api/fisioterapeuta/citas/{id}
     * Obtener detalle completo de una cita
     */
    @GetMapping("/citas/{id}")
    public ResponseEntity<CitaDTO> getDetalleCita(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        CitaDTO cita = fisioterapeutaService.getDetalleCita(id, fisioterapeutaId);
        return ResponseEntity.ok(cita);
    }

    /**
     * PUT /api/fisioterapeuta/citas/{id}/completar
     * Marcar una cita como completada
     */
    @PutMapping("/citas/{id}/completar")
    public ResponseEntity<CitaDTO> marcarComoCompletada(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        CitaDTO cita = fisioterapeutaService.marcarComoCompletada(id, fisioterapeutaId);
        return ResponseEntity.ok(cita);
    }

    /**
     * POST /api/fisioterapeuta/citas/{id}/nota
     * Crear o actualizar nota de sesión
     */
    @PostMapping("/citas/{id}/nota")
    public ResponseEntity<NotaSesionDTO> crearOActualizarNota(
            @PathVariable Long id,
            @Valid @RequestBody CrearNotaSesionRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        request.setCitaId(id); // Asegurar que el ID coincide con la ruta

        NotaSesionDTO nota = fisioterapeutaService.crearOActualizarNota(id, request, fisioterapeutaId);
        return new ResponseEntity<>(nota, HttpStatus.CREATED);
    }

    /**
     * GET /api/fisioterapeuta/citas/{id}/nota
     * Obtener la nota de una cita específica
     */
    @GetMapping("/citas/{id}/nota")
    public ResponseEntity<NotaSesionDTO> getNotaDeCita(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        NotaSesionDTO nota = fisioterapeutaService.getNotaDeCita(id, fisioterapeutaId);
        return ResponseEntity.ok(nota);
    }

    /**
     * GET /api/fisioterapeuta/clientes/{clienteId}/historial
     * Obtener historial completo de un cliente (todas sus citas con este fisioterapeuta)
     */
    @GetMapping("/clientes/{clienteId}/historial")
    public ResponseEntity<List<HistorialClienteDTO>> getHistorialCliente(
            @PathVariable Long clienteId,
            @AuthenticationPrincipal Usuario usuario) {

        Long fisioterapeutaId = usuario.getFisioterapeuta().getId();
        List<HistorialClienteDTO> historial = fisioterapeutaService.getHistorialCliente(clienteId, fisioterapeutaId);
        return ResponseEntity.ok(historial);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Obtener el lunes de la semana actual
     */
    private LocalDate obtenerLunesSemanaActual() {
        LocalDate hoy = LocalDate.now();
        int diaSemana = hoy.getDayOfWeek().getValue(); // 1 = Lunes, 7 = Domingo
        return hoy.minusDays(diaSemana - 1);
    }
}