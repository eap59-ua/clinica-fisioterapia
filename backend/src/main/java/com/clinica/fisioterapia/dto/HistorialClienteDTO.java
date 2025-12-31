package com.clinica.fisioterapia.dto;

import com.clinica.fisioterapia.entity.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialClienteDTO {

    private Long citaId;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoCita estado;
    private String servicioNombre;
    private String notaResumen; // Primeras líneas de la nota
    private boolean tieneNota;
}