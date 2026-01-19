package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.*;
import com.clinica.fisioterapia.entity.*;
import com.clinica.fisioterapia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
public class FisioterapeutaService {

    private final CitaRepository citaRepository;
    private final NotaSesionRepository notaSesionRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final ClienteRepository clienteRepository;

    /**
     * Obtener todas las citas del fisioterapeuta con filtro opcional por fecha
     */
    public List<CitaDTO> getMisCitas(Long fisioterapeutaId, LocalDate fecha) {
        List<Cita> citas;

        if (fecha != null) {
            // Filtrar por fecha específica
            citas = citaRepository.findByFisioterapeutaIdAndFecha(fisioterapeutaId, fecha);
        } else {
            // Todas las citas del fisioterapeuta
            citas = citaRepository.findByFisioterapeutaId(fisioterapeutaId);
        }

        return citas.stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener citas de hoy del fisioterapeuta (para Dashboard)
     */
    public List<CitaDTO> getMisCitasHoy(Long fisioterapeutaId) {
        LocalDate hoy = LocalDate.now();
        List<Cita> citas = citaRepository.findByFisioterapeutaIdAndFecha(fisioterapeutaId, hoy);

        return citas.stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener citas de una semana completa
     */
    public List<CitaDTO> getMisCitasSemana(Long fisioterapeutaId, LocalDate fechaInicio) {
        LocalDate fechaFin = fechaInicio.plusDays(6);
        List<Cita> citas = citaRepository.findByFisioterapeutaIdAndFechaBetween(
                fisioterapeutaId, fechaInicio, fechaFin);

        return citas.stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener detalle completo de una cita
     */
    public CitaDTO getDetalleCita(Long citaId, Long fisioterapeutaId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Validar que la cita pertenece al fisioterapeuta
        validarCitaPerteneceFisioterapeuta(cita, fisioterapeutaId);

        return convertirACitaDTO(cita);
    }

    /**
     * Marcar cita como completada
     */
    @Transactional
    public CitaDTO marcarComoCompletada(Long citaId, Long fisioterapeutaId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Validar que la cita pertenece al fisioterapeuta
        validarCitaPerteneceFisioterapeuta(cita, fisioterapeutaId);

        // Validar que la cita no esté ya completada o cancelada
        if (cita.getEstado() == EstadoCita.COMPLETADA) {
            throw new RuntimeException("La cita ya está marcada como completada");
        }
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new RuntimeException("No se puede completar una cita cancelada");
        }

        cita.setEstado(EstadoCita.COMPLETADA);
        Cita citaActualizada = citaRepository.save(cita);

        return convertirACitaDTO(citaActualizada);
    }

    /**
     * Crear o actualizar nota de sesión
     */
    @Transactional
    public NotaSesionDTO crearOActualizarNota(Long citaId, CrearNotaSesionRequest request, Long fisioterapeutaId) {
        // Buscar la cita y validar
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        validarCitaPerteneceFisioterapeuta(cita, fisioterapeutaId);

        // Validar que la cita esté completada
        if (cita.getEstado() != EstadoCita.COMPLETADA) {
            throw new RuntimeException("Solo se pueden agregar notas a citas completadas");
        }

        // Buscar si ya existe una nota para esta cita
        NotaSesion nota = notaSesionRepository.findByCitaId(citaId)
                .orElse(new NotaSesion());

        // Si es nueva, setear cita y fisioterapeuta
        if (nota.getId() == null) {
            nota.setCita(cita);
            Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(fisioterapeutaId)
                    .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));
            nota.setFisioterapeuta(fisioterapeuta);
        }

        // Actualizar campos
        nota.setContenido(request.getContenido());
        nota.setDiagnostico(request.getDiagnostico());
        nota.setTratamientoAplicado(request.getTratamientoAplicado());
        nota.setRecomendaciones(request.getRecomendaciones());

        NotaSesion notaGuardada = notaSesionRepository.save(nota);

        return convertirANotaSesionDTO(notaGuardada);
    }

    /**
     * Obtener nota de una cita específica
     */
    public NotaSesionDTO getNotaDeCita(Long citaId, Long fisioterapeutaId) {
        // Validar que la cita pertenece al fisioterapeuta
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        validarCitaPerteneceFisioterapeuta(cita, fisioterapeutaId);

        NotaSesion nota = notaSesionRepository.findByCitaId(citaId)
                .orElse(null);

        return nota != null ? convertirANotaSesionDTO(nota) : null;
    }

    /**
     * Obtener historial completo de un cliente (todas sus citas con el fisioterapeuta)
     */
    public List<HistorialClienteDTO> getHistorialCliente(Long clienteId, Long fisioterapeutaId) {
        // Validar que el cliente existe
        clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Obtener todas las citas del cliente con este fisioterapeuta
        List<Cita> citas = citaRepository.findByClienteIdAndFisioterapeutaIdOrderByFechaDesc(
                clienteId, fisioterapeutaId);

        return citas.stream()
                .map(this::convertirAHistorialDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener lista de pacientes del día (clientes únicos con sus citas de hoy)
     */
    public List<PacienteDiaDTO> getPacientesDelDia(Long fisioterapeutaId) {
        LocalDate hoy = LocalDate.now();
        List<Cita> citasHoy = citaRepository.findByFisioterapeutaIdAndFecha(fisioterapeutaId, hoy);

        return citasHoy.stream()
                .sorted(Comparator.comparing(Cita::getHoraInicio))
                .map(this::convertirAPacienteDiaDTO)
                .collect(Collectors.toList());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Validar que la cita pertenece al fisioterapeuta autenticado
     */
    private void validarCitaPerteneceFisioterapeuta(Cita cita, Long fisioterapeutaId) {
        if (!cita.getFisioterapeuta().getId().equals(fisioterapeutaId)) {
            throw new RuntimeException("No tienes permiso para acceder a esta cita");
        }
    }

    /**
     * Convertir Cita a CitaDTO
     */
    private CitaDTO convertirACitaDTO(Cita cita) {
        Cliente cliente = cita.getCliente();
        Fisioterapeuta fisio = cita.getFisioterapeuta();
        Servicio servicio = cita.getServicio();
        Sala sala = cita.getSala();

        return CitaDTO.builder()
                .id(cita.getId())
                .cliente(CitaDTO.ClienteInfo.builder()
                        .id(cliente.getId())
                        .nombre(cliente.getNombre())
                        .apellidos(cliente.getApellidos())
                        .email(cliente.getEmail())
                        .telefono(cliente.getTelefono())
                        .build())
                .fisioterapeuta(CitaDTO.FisioterapeutaInfo.builder()
                        .id(fisio.getId())
                        .nombre(fisio.getNombre())
                        .apellidos(fisio.getApellidos())
                        .email(fisio.getEmail())
                        .especialidades(fisio.getEspecialidades())
                        .build())
                .servicio(CitaDTO.ServicioInfo.builder()
                        .id(servicio.getId())
                        .nombre(servicio.getNombre())
                        .descripcion(servicio.getDescripcion())
                        .duracionMinutos(servicio.getDuracionMinutos())
                        .precio(servicio.getPrecio())
                        .build())
                .sala(sala != null ? CitaDTO.SalaInfo.builder()
                        .id(sala.getId())
                        .nombre(sala.getNombre())
                        .build() : null)
                .fecha(cita.getFecha())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .estado(cita.getEstado())
                .notas(cita.getNotas())
                .precioPagado(cita.getPrecioPagado())
                .createdAt(cita.getCreatedAt())
                .updatedAt(cita.getUpdatedAt())
                .build();
    }

    /**
     * Convertir NotaSesion a NotaSesionDTO
     */
    private NotaSesionDTO convertirANotaSesionDTO(NotaSesion nota) {
        return NotaSesionDTO.builder()
                .id(nota.getId())
                .citaId(nota.getCita().getId())
                .contenido(nota.getContenido())
                .diagnostico(nota.getDiagnostico())
                .tratamientoAplicado(nota.getTratamientoAplicado())
                .recomendaciones(nota.getRecomendaciones())
                .fisioterapeuta(NotaSesionDTO.FisioterapeutaInfo.builder()
                        .id(nota.getFisioterapeuta().getId())
                        .nombre(nota.getFisioterapeuta().getNombre())
                        .apellidos(nota.getFisioterapeuta().getApellidos())
                        .build())
                .createdAt(nota.getCreatedAt())
                .updatedAt(nota.getUpdatedAt())
                .build();
    }

    /**
     * Convertir Cita a HistorialClienteDTO
     */
    private HistorialClienteDTO convertirAHistorialDTO(Cita cita) {
        // Buscar si existe nota para esta cita
        boolean tieneNota = notaSesionRepository.existsByCitaId(cita.getId());
        String notaResumen = null;

        if (tieneNota) {
            NotaSesion nota = notaSesionRepository.findByCitaId(cita.getId()).orElse(null);
            if (nota != null && nota.getContenido() != null) {
                // Obtener primeras 100 caracteres
                notaResumen = nota.getContenido().length() > 100
                        ? nota.getContenido().substring(0, 100) + "..."
                        : nota.getContenido();
            }
        }

        return HistorialClienteDTO.builder()
                .citaId(cita.getId())
                .fecha(cita.getFecha())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .estado(cita.getEstado())
                .servicioNombre(cita.getServicio().getNombre())
                .tieneNota(tieneNota)
                .notaResumen(notaResumen)
                .build();
    }

    /**
     * Convertir Cita a PacienteDiaDTO
     */
    private PacienteDiaDTO convertirAPacienteDiaDTO(Cita cita) {
        Cliente cliente = cita.getCliente();

        return PacienteDiaDTO.builder()
                .clienteId(cliente.getId())
                .nombreCompleto(cliente.getNombre() + " " + cliente.getApellidos())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .citaId(cita.getId())
                .horaInicio(cita.getHoraInicio())
                .horaFin(cita.getHoraFin())
                .servicioNombre(cita.getServicio().getNombre())
                .estado(cita.getEstado().toString())
                .build();
    }
}