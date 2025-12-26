package com.clinica.fisioterapia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DisponibilidadDTO {

    private LocalDate fecha;
    private Long fisioterapeutaId;
    private List<SlotHorario> slots;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotHorario {
        private LocalTime horaInicio;
        private LocalTime horaFin;
        private Boolean disponible;
    }
}
