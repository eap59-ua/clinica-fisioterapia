package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Sala;
import com.clinica.fisioterapia.repository.SalaRepository; // O su servicio
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/salas") // Ruta: /admin/salas
@CrossOrigin(origins = "http://localhost:5173")
public class AdminSalasController {

    private final SalaRepository salaRepository; // Usamos repo directo para ir rápido

    public AdminSalasController(SalaRepository salaRepository) {
        this.salaRepository = salaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listar() {
        return ResponseEntity.ok(salaRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Sala> crear(@RequestBody Sala sala) {
        return ResponseEntity.ok(salaRepository.save(sala));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Sala> editar(@PathVariable Long id, @RequestBody Sala datos) {
        Sala sala = salaRepository.findById(id).orElseThrow();
        sala.setNombre(datos.getNombre());
        sala.setCapacidad(datos.getCapacidad());
        // Añade más campos si tu entidad Sala los tiene
        return ResponseEntity.ok(salaRepository.save(sala));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        salaRepository.deleteById(id);
        return ResponseEntity.ok("Sala eliminada");
    }
}