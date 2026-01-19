package com.clinica.fisioterapia.dto;

import com.fasterxml.jackson.annotation.JsonFormat; // IMPORTANTE
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BloqueoDTO {
    private Long fisioterapeutaId;

    // El input datetime-local envía "yyyy-MM-ddTHH:mm"
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime fechaFin;

    private String motivo;
    private String tipo;
}