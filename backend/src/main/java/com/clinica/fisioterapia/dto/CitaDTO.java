package com.clinica.fisioterapia.dto;

import com.clinica.fisioterapia.entity.EstadoCita;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaDTO {

    private Long id;
    private ClienteInfo cliente;
    private FisioterapeutaInfo fisioterapeuta;
    private ServicioInfo servicio;
    private SalaInfo sala;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private EstadoCita estado;
    private String notas;
    private BigDecimal precioPagado;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClienteInfo {
        private Long id;
        private String nombre;
        private String apellidos;
        private String email;
        private String telefono;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FisioterapeutaInfo {
        private Long id;
        private String nombre;
        private String apellidos;
        private String email;
        private String especialidades;
        private String fotoUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServicioInfo {
        private Long id;
        private String nombre;
        private String descripcion;
        private Integer duracionMinutos;
        private BigDecimal precio;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SalaInfo {
        private Long id;
        private String nombre;
    }
}
