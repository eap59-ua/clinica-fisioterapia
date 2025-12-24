package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.Fisioterapeuta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FisioterapeutaRepository extends JpaRepository<Fisioterapeuta, Long> {
    List<Fisioterapeuta> findByActivoTrue();
}