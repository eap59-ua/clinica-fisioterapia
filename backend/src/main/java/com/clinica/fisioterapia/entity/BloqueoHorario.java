package com.clinica.fisioterapia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueo_horario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BloqueoHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fisioterapeuta_id", nullable = false)
    private Fisioterapeuta fisioterapeuta;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(length = 255)
    private String motivo;

    @Column(length = 20, nullable = false)
    private String tipo = "PERSONAL";

    @PrePersist
    protected void onCreate() {
        if (tipo == null) {
            tipo = "PERSONAL";
        }
    }
}
