package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.dto.DisponibilidadDTO;
import com.clinica.fisioterapia.dto.ReservarCitaRequest;
import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.service.CitaService;
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
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CitaDTO> reservarCita(
            @Valid @RequestBody ReservarCitaRequest request,
            @AuthenticationPrincipal Usuario usuario) {

        CitaDTO cita = citaService.reservarCita(request, usuario.getId());
        return new ResponseEntity<>(cita, HttpStatus.CREATED);
    }

    @GetMapping("/mis-citas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaDTO>> getMisCitas(@AuthenticationPrincipal Usuario usuario) {
        List<CitaDTO> citas = citaService.getMisCitas(usuario.getId());
        return ResponseEntity.ok(citas);
    }

    @GetMapping("/proximas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CitaDTO>> getProximasCitas(@AuthenticationPrincipal Usuario usuario) {
        List<CitaDTO> citas = citaService.getProximasCitas(usuario.getId());
        return ResponseEntity.ok(citas);
    }

    @PutMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<Void> cancelarCita(
            @PathVariable Long id,
            @AuthenticationPrincipal Usuario usuario) {

        citaService.cancelarCita(id, usuario.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/disponibilidad")
    public ResponseEntity<DisponibilidadDTO> getDisponibilidad(
            @RequestParam Long fisioterapeutaId,
            @RequestParam Long servicioId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        DisponibilidadDTO disponibilidad = citaService.getDisponibilidad(fisioterapeutaId, servicioId, fecha);
        return ResponseEntity.ok(disponibilidad);
    }
}
