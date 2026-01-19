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
}
