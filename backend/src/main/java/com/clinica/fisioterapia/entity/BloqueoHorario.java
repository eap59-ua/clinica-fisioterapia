package com.clinica.fisioterapia.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "bloqueo_horario")
@Data
public class BloqueoHorario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Puede ser null si es un festivo nacional (GLOBAL)
    @ManyToOne
    @JoinColumn(name = "fisioterapeuta_id")
    private Fisioterapeuta fisioterapeuta;

    @Column(name = "fecha_inicio")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") // Añade esto
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm") // Añade esto
    private LocalDateTime fechaFin;
    private String motivo;

    // 'PERSONAL' o 'GLOBAL'
    private String tipo;
}