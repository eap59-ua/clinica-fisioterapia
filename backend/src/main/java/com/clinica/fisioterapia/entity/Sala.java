package com.clinica.fisioterapia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sala")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;

    @Column
    private Integer capacidad = 1;

    @Column(columnDefinition = "TEXT")
    private String equipamiento;

    @Column(nullable = false)
    private Boolean activa = true;
}
