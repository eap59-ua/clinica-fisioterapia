package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Servicio;
import com.clinica.fisioterapia.repository.ServicioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/servicios")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminServiciosController {

    private final ServicioRepository servicioRepository;

    public AdminServiciosController(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @GetMapping
    public ResponseEntity<List<Servicio>> listar() {
        return ResponseEntity.ok(servicioRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Servicio> crear(@RequestBody Servicio servicio) {
        return ResponseEntity.ok(servicioRepository.save(servicio));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servicio> editar(@PathVariable Long id, @RequestBody Servicio datos) {
        Servicio servicio = servicioRepository.findById(id).orElseThrow();
        servicio.setNombre(datos.getNombre());
        servicio.setDescripcion(datos.getDescripcion());
        servicio.setDuracionMinutos(datos.getDuracionMinutos());
        servicio.setPrecio(datos.getPrecio());
        servicio.setActivo(datos.getActivo());
        servicio.setImagenUrl(datos.getImagenUrl());
        return ResponseEntity.ok(servicioRepository.save(servicio));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        servicioRepository.deleteById(id);
        return ResponseEntity.ok("Servicio eliminado");
    }
}
