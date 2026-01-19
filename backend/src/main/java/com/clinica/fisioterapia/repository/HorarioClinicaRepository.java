package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.HorarioClinica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HorarioClinicaRepository extends JpaRepository<HorarioClinica, Long> {
    // Buscamos por el número del día (1 al 7)
    Optional<HorarioClinica> findByDiaSemana(int diaSemana);
}