package com.clinica.fisioterapia.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "nota_sesion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotaSesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fisioterapeuta_id", nullable = false)
    private Fisioterapeuta fisioterapeuta;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "diagnostico")
    private String diagnostico;

    @Column(name = "tratamiento_aplicado", columnDefinition = "TEXT")
    private String tratamientoAplicado;

    @Column(name = "recomendaciones", columnDefinition = "TEXT")
    private String recomendaciones;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}