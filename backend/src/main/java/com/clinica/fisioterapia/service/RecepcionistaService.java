package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.entity.Cita;
import com.clinica.fisioterapia.entity.EstadoCita;
import com.clinica.fisioterapia.repository.CitaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecepcionistaService {

    private final CitaRepository citaRepository;

    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerCitasDelDia(LocalDate fecha) {
        // Usamos el método de tu repositorio que hace JOIN FETCH (optimizado)
        List<Cita> citas = citaRepository.findAllByFecha(fecha);
        return citas.stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> obtenerCitasSemana(LocalDate fecha) {
        LocalDate inicio = fecha.minusDays(fecha.getDayOfWeek().getValue() - 1);
        LocalDate fin = inicio.plusDays(6);

        // Usamos el método de tu repositorio que hace JOIN FETCH
        List<Cita> citas = citaRepository.findByFechaBetween(inicio, fin);
        return citas.stream()
                .map(this::convertirACitaDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public CitaDTO crearCita(Cita cita) {
        // 1. Lógica de negocio: Validar/Rellenar datos
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCita.PENDIENTE);
        }

        // Calcular hora fin si no viene (para evitar el error SQL)
        if (cita.getHoraFin() == null && cita.getHoraInicio() != null) {
            // Asumimos 1 hora por defecto si no se especifica
            cita.setHoraFin(cita.getHoraInicio().plusHours(1));
        }

        // 2. Guardar entidad
        Cita citaGuardada = citaRepository.save(cita);

        // 3. Devolver DTO (Aquí evitamos el error de recursividad)
        return convertirACitaDTO(citaGuardada);
    }

    @Transactional
    public CitaDTO cambiarEstado(Long id, EstadoCita nuevoEstado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        cita.setEstado(nuevoEstado);
        Cita citaActualizada = citaRepository.save(cita);

        return convertirACitaDTO(citaActualizada);
    }

    // --- MÉTODOS DE CONVERSIÓN (Igual que en CitaService) ---
    private CitaDTO convertirACitaDTO(Cita cita) {
        return CitaDTO.builder()
                .id(cita.getId())
                .cliente(CitaDTO.ClienteInfo.builder()
                        .id(cita.getCliente().getId())
                        .nombre(cita.getCliente().getNombre())
                        .apellidos(cita.getCliente().getApellidos())
                        .email(cita.getCliente().getEmail())
                        .telefono(cita.getCliente().getTelefono())
                        .build())
                .fisioterapeuta(CitaDTO.FisioterapeutaInfo.builder()
                        .id(cita.getFisioterapeuta().getId())
                        .nombre(cita.getFisioterapeuta().getNombre())
                        .apellidos(cita.getFisioterapeuta().getApellidos())
                        .email(cita.getFisioterapeuta().getEmail())
                        .especialidades(cita.getFisioterapeuta().getEspecialidades())
                        .fotoUrl(cita.getFisioterapeuta().getFotoUrl())
                        .build())
                .servicio(CitaDTO.ServicioInfo.builder()
                        .id(cita.getServicio().getId())
                        .nombre(cita.getServicio().getNombre())
                        .descripcion(cita.getServicio().getDescripcion())
                        .duracionMinutos(cita.getServicio().getDuracionMinutos())
                        .precio(cita.getServicio().getPrecio())
                        .build())
                // Manejo seguro de Sala (puede ser null)
                .sala(cita.getSala() != null ? CitaDTO.SalaInfo.builder()
                        .id(cita.getSala().getId())
                        .nombre(cita.getSala().getNombre())
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
}