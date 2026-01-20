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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/api/recepcionista", "/recepcionista"}) // Soporte para ambas rutas
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RecepcionistaController {

    private final RecepcionistaService service;

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
        // NOTA: Seguimos recibiendo 'Cita' (entidad) en la entrada para facilitar
        // el JSON que envía tu Frontend actual, pero devolvemos 'CitaDTO'.
        try {
            CitaDTO nuevaCita = service.crearCita(cita);
            return ResponseEntity.ok(nuevaCita);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creando cita: " + e.getMessage());
        }
    }

    @PutMapping("/citas/{id}/estado")
    public ResponseEntity<?> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String estado) {
        try {
            EstadoCita estadoEnum = EstadoCita.valueOf(estado);
            return ResponseEntity.ok(service.cambiarEstado(id, estadoEnum));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // necesarios para listar los disponibles en el formulario de crear cita
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private FisioterapeutaRepository fisioterapeutaRepository;
    @Autowired private ServicioRepository servicioRepository;
    @Autowired private SalaRepository salaRepository;

    @GetMapping("/clientes")
    public List<Cliente> getClientes() { return clienteRepository.findAll(); }

    @GetMapping("/fisioterapeutas")
    public ResponseEntity<List<FisioterapeutaDTO>> getFisios() {

        List<Fisioterapeuta> entidades = fisioterapeutaRepository.findAll();

        // 2. Transforma la lista de Entidades a DTOs
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
    public List<Servicio> getServicios() { return servicioRepository.findAll(); }

    @GetMapping("/salas")
    public List<Sala> getSalas() { return salaRepository.findAll(); }

    // FR-REC-02: Endpoint de búsqueda
    @GetMapping("/clientes/buscar")
    public ResponseEntity<List<Cliente>> buscarClientes(@RequestParam String query) {
        return ResponseEntity.ok(service.buscarClientes(query));
    }

    // FR-REC-04: Endpoint para obtener una cita individual (para cargar el formulario de edición)
    @GetMapping("/citas/{id}")
    public ResponseEntity<CitaDTO> getCita(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerCitaPorId(id));
    }

    // FR-REC-04: Endpoint para actualizar
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

    // FR-REC-03: Endpoint para sugerir huecos
    @GetMapping("/disponibilidad")
    public ResponseEntity<List<String>> comprobarDisponibilidad(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam Long fisioterapeutaId,
            @RequestParam int duracion) {

        return ResponseEntity.ok(service.obtenerHuecosLibres(fecha, fisioterapeutaId, duracion));
    }
}