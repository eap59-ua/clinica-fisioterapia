package com.clinica.fisioterapia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaSesionDTO {

    private Long id;
    private Long citaId;
    private String contenido;
    private String diagnostico;
    private String tratamientoAplicado;
    private String recomendaciones;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Info básica del fisioterapeuta que creó la nota
    private FisioterapeutaInfo fisioterapeuta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FisioterapeutaInfo {
        private Long id;
        private String nombre;
        private String apellidos;
    }
}