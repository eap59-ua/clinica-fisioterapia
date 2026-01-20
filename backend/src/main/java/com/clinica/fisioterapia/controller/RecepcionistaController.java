package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.dto.FisioterapeutaDTO;
import com.clinica.fisioterapia.entity.*;
import com.clinica.fisioterapia.repository.ClienteRepository;
import com.clinica.fisioterapia.repository.FisioterapeutaRepository;
import com.clinica.fisioterapia.repository.SalaRepository;
import com.clinica.fisioterapia.repository.ServicioRepository;
import com.clinica.fisioterapia.service.RecepcionistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/recepcionista", "/recepcionista"})
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecepcionistaController {

    private final RecepcionistaService service;

    // Inyectamos los repositorios vía Constructor (Lombok) para mantener consistencia
    private final ClienteRepository clienteRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final ServicioRepository servicioRepository;
    private final SalaRepository salaRepository;

    // --- ENDPOINTS DE GESTIÓN DE CITAS ---

    @GetMapping("/citas/dia")
    public ResponseEntity<List<CitaDTO>> getCitasDia(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate f = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(service.obtenerCitasDelDia(f));
    }

    @GetMapping("/citas/semana")
    public ResponseEntity<List<CitaDTO>> getCitasSemana(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        LocalDate f = (fecha != null) ? fecha : LocalDate.now();
        return ResponseEntity.ok(service.obtenerCitasSemana(f));
    }

    @PostMapping("/citas")
    public ResponseEntity<?> crearCita(@RequestBody Cita cita) {
        try {
            // El servicio ya valida Sala, Fisio y Bloqueos
            CitaDTO nuevaCita = service.crearCita(cita);
            return ResponseEntity.ok(nuevaCita);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creando cita: " + e.getMessage());
        }
    }

    @GetMapping("/citas/{id}")
    public ResponseEntity<CitaDTO> getCita(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerCitaPorId(id));
    }

    @PutMapping("/citas/{id}")
    public ResponseEntity<?> actualizarCita(@PathVariable Long id, @RequestBody Cita cita) {
        try {
            CitaDTO citaActualizada = service.actualizarCita(id, cita);
            return ResponseEntity.ok(citaActualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al actualizar la cita");
        }
    }

    @PutMapping("/citas/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            EstadoCita estadoEnum = EstadoCita.valueOf(estado);
            return ResponseEntity.ok(service.cambiarEstado(id, estadoEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // --- BUSCADOR INTELIGENTE DE HUECOS (ARREGLADO) ---

    @GetMapping("/disponibilidad")
    public ResponseEntity<List<String>> comprobarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long fisioterapeutaId,
            @RequestParam(required = false) Long salaId, // <--- AÑADIDO: Ahora recibimos la Sala
            @RequestParam int duracion) {

        // Pasamos el salaId al servicio (puede ser null y el servicio lo gestiona)
        return ResponseEntity.ok(service.obtenerHuecosLibres(fecha, fisioterapeutaId, salaId, duracion));
    }

    // --- ENDPOINTS AUXILIARES PARA FORMULARIOS ---

    @GetMapping("/clientes")
    public List<Cliente> getClientes() {
        return clienteRepository.findAll();
    }

    @GetMapping("/clientes/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam String query) {
        return ResponseEntity.ok(service.buscarClientes(query));
    }

    @GetMapping("/fisioterapeutas")
    public ResponseEntity<List<FisioterapeutaDTO>> getFisios() {
        List<Fisioterapeuta> entidades = fisioterapeutaRepository.findAll();
        List<FisioterapeutaDTO> dtos = entidades.stream()
                .map(fisio -> new FisioterapeutaDTO(
                        fisio.getId(),
                        fisio.getNombre(),
                        fisio.getApellidos(),
                        fisio.getEmail(),
                        fisio.getTelefono(),
                        fisio.getEspecialidades(),
                        fisio.getNumeroColegiado(),
                        fisio.getFotoUrl(),
                        fisio.getValoracionPromedio(),
                        fisio.getBiografia()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/servicios")
    public List<Servicio> getServicios() {
        return servicioRepository.findAll();
    }

    @GetMapping("/salas")
    public List<Sala> getSalas() {
        return salaRepository.findAll();
    }
}