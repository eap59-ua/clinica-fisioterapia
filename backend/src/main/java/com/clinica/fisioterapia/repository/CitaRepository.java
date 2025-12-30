package com.clinica.fisioterapia.repository;

import com.clinica.fisioterapia.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CitaRepository extends JpaRepository<Cita, Long> {

    // Obtener citas de un cliente
    @Query("SELECT c FROM Cita c WHERE c.cliente.id = :clienteId ORDER BY c.fecha DESC, c.horaInicio DESC")
    List<Cita> findByClienteIdOrderByFechaDesc(@Param("clienteId") Long clienteId);

    // Obtener próximas citas de un cliente
    @Query("SELECT c FROM Cita c WHERE c.cliente.id = :clienteId AND c.fecha >= :hoy AND c.estado = 'PENDIENTE' ORDER BY c.fecha, c.horaInicio")
    List<Cita> findProximasCitasCliente(@Param("clienteId") Long clienteId, @Param("hoy") LocalDate hoy);

    // Verificar disponibilidad de fisioterapeuta en fecha/hora
    @Query("SELECT c FROM Cita c WHERE c.fisioterapeuta.id = :fisioId AND c.fecha = :fecha " +
           "AND c.estado != 'CANCELADA' " +
           "AND ((c.horaInicio <= :horaInicio AND c.horaFin > :horaInicio) " +
           "OR (c.horaInicio < :horaFin AND c.horaFin >= :horaFin) " +
           "OR (c.horaInicio >= :horaInicio AND c.horaFin <= :horaFin))")
    List<Cita> findConflictingCitas(
        @Param("fisioId") Long fisioId,
        @Param("fecha") LocalDate fecha,
        @Param("horaInicio") LocalTime horaInicio,
        @Param("horaFin") LocalTime horaFin
    );

    // Obtener citas de un fisioterapeuta en una fecha
    @Query("SELECT c FROM Cita c WHERE c.fisioterapeuta.id = :fisioId AND c.fecha = :fecha AND c.estado != 'CANCELADA' ORDER BY c.horaInicio")
    List<Cita> findCitasFisioterapeutaEnFecha(@Param("fisioId") Long fisioId, @Param("fecha") LocalDate fecha);
}
