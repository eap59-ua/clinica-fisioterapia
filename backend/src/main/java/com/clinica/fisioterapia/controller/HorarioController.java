package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.BloqueoDTO;
import com.clinica.fisioterapia.dto.HorarioModificacionDTO;
import com.clinica.fisioterapia.entity.BloqueoHorario;
import com.clinica.fisioterapia.entity.HorarioClinica;
import com.clinica.fisioterapia.service.HorarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/horarios", "/horarios"})
@CrossOrigin(origins = "*") // Para desarrollo Frontend
@RequiredArgsConstructor
public class HorarioController {

    private final HorarioService horarioService;

    // --- FR-REC-10 y 11: Ver y Modificar Horarios Base ---

    @GetMapping("/configuracion")
    public ResponseEntity<List<HorarioClinica>> obtenerConfiguracionSemanal() {
        return ResponseEntity.ok(horarioService.obtenerHorarioSemanal());
    }

    @PutMapping("/actualizar")
    public ResponseEntity<HorarioClinica> actualizarDia(@RequestBody HorarioModificacionDTO dto) {
        return ResponseEntity.ok(horarioService.actualizarHorario(dto));
    }

    // --- FR-REC-12 y 13: Gestión de Festivos y Vacaciones ---

    @PostMapping("/bloqueos")
    public ResponseEntity<BloqueoHorario> crearBloqueo(@RequestBody BloqueoDTO dto) {
        return ResponseEntity.ok(horarioService.crearBloqueo(dto));
    }

    // --- FR-REC-14: Visualizar Calendario de Bloqueos ---

    @GetMapping("/calendario-global")
    public ResponseEntity<List<BloqueoHorario>> obtenerCalendarioGlobal() {
        // Devuelve todos los bloqueos para pintar en el calendario del frontend
        // En una app real pasarías ?fechaInicio=...&fechaFin=...
        return ResponseEntity.ok(horarioService.obtenerCalendarioBloqueos(null, null));
    }
}