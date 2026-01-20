package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.BloqueoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BloqueoHorarioRepository extends JpaRepository<BloqueoHorario, Long> {

    List<BloqueoHorario> findByFisioterapeutaId(Long fisioterapeutaId);

    @Query("SELECT b FROM BloqueoHorario b WHERE b.fisioterapeuta.id = :fisioterapeutaId " +
           "AND b.fechaInicio <= :fechaFin AND b.fechaFin >= :fechaInicio")
    List<BloqueoHorario> findByFisioterapeutaIdAndFechaBetween(
            @Param("fisioterapeutaId") Long fisioterapeutaId,
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );
  
    // Buscar bloqueos que coincidan con una fecha y hora específica
    // Sirve para saber si HOY es festivo o el fisio está de vacaciones
    @Query("SELECT b FROM BloqueoHorario b WHERE " +
            "(b.tipo = 'GLOBAL' OR b.fisioterapeuta.id = :fisioId) AND " +
            ":fechaHora BETWEEN b.fechaInicio AND b.fechaFin")
    List<BloqueoHorario> encontrarBloqueos(@Param("fisioId") Long fisioId,
                                           @Param("fechaHora") LocalDateTime fechaHora);

    // === ESTA ES LA CLAVE ===
    // Busca intersección de horarios:
    // 1. Que sea del Fisio O que sea GLOBAL (festivo).
    // 2. Que el bloqueo empiece antes de que acabe la cita Y termine después de que empiece.
    @Query("SELECT b FROM BloqueoHorario b WHERE " +
            "(b.tipo = 'GLOBAL' OR b.fisioterapeuta.id = :fisioId) " +
            "AND (b.fechaInicio < :fin AND b.fechaFin > :inicio)")
    List<BloqueoHorario> encontrarBloqueos(@Param("fisioId") Long fisioId,
                                           @Param("inicio") LocalDateTime inicio,
                                           @Param("fin") LocalDateTime fin);
}