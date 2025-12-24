package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Fisioterapeuta;
import com.clinica.fisioterapia.entity.Servicio;
import com.clinica.fisioterapia.repository.FisioterapeutaRepository;
import com.clinica.fisioterapia.repository.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final ServicioRepository servicioRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;

    @GetMapping("/servicios")
    public ResponseEntity<List<Servicio>> getServicios() {
        List<Servicio> servicios = servicioRepository.findByActivoTrue();
        return ResponseEntity.ok(servicios);
    }

    @GetMapping("/fisioterapeutas")
    public ResponseEntity<List<Fisioterapeuta>> getFisioterapeutas() {
        List<Fisioterapeuta> fisioterapeutas = fisioterapeutaRepository.findByActivoTrue();
        // Ocultar password antes de enviar
        fisioterapeutas.forEach(f -> f.setPassword(null));
        return ResponseEntity.ok(fisioterapeutas);
    }
}