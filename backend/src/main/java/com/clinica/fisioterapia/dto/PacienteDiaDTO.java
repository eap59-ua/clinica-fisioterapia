package com.clinica.fisioterapia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDiaDTO {

    private Long clienteId;
    private String nombreCompleto;
    private String email;
    private String telefono;
    private Long citaId;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String servicioNombre;
    private String estado;
}
