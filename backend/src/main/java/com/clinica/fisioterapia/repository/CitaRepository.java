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

    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "WHERE c.fecha = :fecha " +
            "ORDER BY c.horaInicio ASC")
    List<Cita> findAllByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "WHERE c.fecha BETWEEN :inicio AND :fin " +
            "ORDER BY c.fecha ASC, c.horaInicio ASC")
    List<Cita> findByFechaBetween(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    // ==================== MÉTODOS PARA FISIOTERAPEUTA ====================

    // Todas las citas de un fisioterapeuta
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "LEFT JOIN FETCH c.sala " +
            "WHERE c.fisioterapeuta.id = :fisioId " +
            "ORDER BY c.fecha DESC, c.horaInicio DESC")
    List<Cita> findByFisioterapeutaId(@Param("fisioId") Long fisioId);

    // Citas de un fisioterapeuta en una fecha específica
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "LEFT JOIN FETCH c.sala " +
            "WHERE c.fisioterapeuta.id = :fisioId AND c.fecha = :fecha " +
            "ORDER BY c.horaInicio ASC")
    List<Cita> findByFisioterapeutaIdAndFecha(@Param("fisioId") Long fisioId, @Param("fecha") LocalDate fecha);

    // Citas de un fisioterapeuta en un rango de fechas (para semana)
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "LEFT JOIN FETCH c.sala " +
            "WHERE c.fisioterapeuta.id = :fisioId " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "ORDER BY c.fecha ASC, c.horaInicio ASC")
    List<Cita> findByFisioterapeutaIdAndFechaBetween(
            @Param("fisioId") Long fisioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    // Historial de un cliente con un fisioterapeuta específico
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.cliente " +
            "JOIN FETCH c.fisioterapeuta " +
            "JOIN FETCH c.servicio " +
            "LEFT JOIN FETCH c.sala " +
            "WHERE c.cliente.id = :clienteId " +
            "AND c.fisioterapeuta.id = :fisioId " +
            "ORDER BY c.fecha DESC, c.horaInicio DESC")
    List<Cita> findByClienteIdAndFisioterapeutaIdOrderByFechaDesc(
            @Param("clienteId") Long clienteId,
            @Param("fisioId") Long fisioId);
}