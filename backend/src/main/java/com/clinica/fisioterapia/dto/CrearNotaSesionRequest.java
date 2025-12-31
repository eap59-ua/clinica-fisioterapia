package com.clinica.fisioterapia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearNotaSesionRequest {

    @NotNull(message = "El ID de la cita es obligatorio")
    private Long citaId;

    @NotBlank(message = "El contenido de la nota no puede estar vacío")
    private String contenido;

    private String diagnostico;
    private String tratamientoAplicado;
    private String recomendaciones;
}