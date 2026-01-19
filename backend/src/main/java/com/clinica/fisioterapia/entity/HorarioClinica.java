package com.clinica.fisioterapia.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "horario_clinica")
@Data
public class HorarioClinica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tu SQL usa INT (1=Lunes, 7=Domingo), así que usamos int aquí.
    @Column(name = "dia_semana", unique = true, nullable = false)
    private int diaSemana;

    @Column(name = "hora_apertura", nullable = false)
    private LocalTime horaApertura;

    @Column(name = "hora_cierre", nullable = false)
    private LocalTime horaCierre;

    // BORRAMOS el campo 'cerrado' porque tu tabla SQL no lo tiene.
    // Lógica: Si no existe registro en la tabla para un día, está cerrado.
}