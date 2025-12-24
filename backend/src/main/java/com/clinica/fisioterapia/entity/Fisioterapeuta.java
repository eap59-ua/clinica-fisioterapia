package com.clinica.fisioterapia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "fisioterapeuta")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "usuario_id")
public class Fisioterapeuta extends Usuario {

    @Column(length = 255)
    private String especialidades;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(columnDefinition = "TEXT")
    private String biografia;

    @Column(name = "numero_colegiado", unique = true, length = 20)
    private String numeroColegiado;

    @Column(name = "valoracion_promedio", precision = 3, scale = 2)
    private BigDecimal valoracionPromedio = BigDecimal.ZERO;
}