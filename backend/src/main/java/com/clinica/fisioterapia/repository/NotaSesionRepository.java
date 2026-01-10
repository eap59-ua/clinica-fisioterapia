package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.NotaSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotaSesionRepository extends JpaRepository<NotaSesion, Long> {

    // Buscar nota de una cita específica
    Optional<NotaSesion> findByCitaId(Long citaId);

    // Buscar todas las notas de un fisioterapeuta
    List<NotaSesion> findByFisioterapeutaId(Long fisioterapeutaId);

    // Buscar notas de un cliente específico (a través de las citas)
    @Query("SELECT n FROM NotaSesion n WHERE n.cita.cliente.id = :clienteId " +
            "ORDER BY n.createdAt DESC")
    List<NotaSesion> findByClienteId(@Param("clienteId") Long clienteId);

    // Verificar si existe una nota para una cita
    boolean existsByCitaId(Long citaId);
}