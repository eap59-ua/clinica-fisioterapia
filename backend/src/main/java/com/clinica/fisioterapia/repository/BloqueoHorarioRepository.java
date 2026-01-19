package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.BloqueoHorario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface BloqueoHorarioRepository extends JpaRepository<BloqueoHorario, Long> {

    // Buscar bloqueos que coincidan con una fecha y hora específica
    // Sirve para saber si HOY es festivo o el fisio está de vacaciones
    @Query("SELECT b FROM BloqueoHorario b WHERE " +
            "(b.tipo = 'GLOBAL' OR b.fisioterapeuta.id = :fisioId) AND " +
            ":fechaHora BETWEEN b.fechaInicio AND b.fechaFin")
    List<BloqueoHorario> encontrarBloqueos(@Param("fisioId") Long fisioId,
                                           @Param("fechaHora") LocalDateTime fechaHora);
}