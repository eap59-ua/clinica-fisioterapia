package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.CitaDTO;
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
    public List<Fisioterapeuta> getFisios() { return fisioterapeutaRepository.findAll(); }

    @GetMapping("/servicios")
    public List<Servicio> getServicios() { return servicioRepository.findAll(); }

    @GetMapping("/salas")
    public List<Sala> getSalas() { return salaRepository.findAll(); }
}