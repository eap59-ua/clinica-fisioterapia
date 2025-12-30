package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalaRepository extends JpaRepository<Sala, Long> {
    List<Sala> findByActivaTrue();
}
