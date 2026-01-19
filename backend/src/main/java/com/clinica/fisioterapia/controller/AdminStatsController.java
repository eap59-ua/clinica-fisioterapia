package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.repository.CitaRepository;
import com.clinica.fisioterapia.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/stats")
@CrossOrigin(origins = "http://localhost:5173")
public class AdminStatsController {

    private final UsuarioRepository usuarioRepository;
    private final CitaRepository citaRepository;

    public AdminStatsController(UsuarioRepository usuarioRepository, CitaRepository citaRepository) {
        this.usuarioRepository = usuarioRepository;
        this.citaRepository = citaRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total usuarios
        long totalUsuarios = usuarioRepository.count();
        stats.put("totalUsuarios", totalUsuarios);
        
        // Citas este mes
        YearMonth mesActual = YearMonth.now();
        LocalDate inicioMes = mesActual.atDay(1);
        LocalDate finMes = mesActual.atEndOfMonth();
        long citasMes = citaRepository.countByFechaBetween(inicioMes, finMes);
        stats.put("citasMes", citasMes);
        
        // Ingresos estimados del mes
        BigDecimal ingresos = citaRepository.sumPrecioPagadoByFechaBetween(inicioMes, finMes);
        stats.put("ingresosMes", ingresos != null ? ingresos : BigDecimal.ZERO);
        
        return ResponseEntity.ok(stats);
    }
}
