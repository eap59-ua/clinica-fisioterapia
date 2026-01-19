package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.BloqueoHorarioDTO;
import com.clinica.fisioterapia.dto.CrearBloqueoRequest;
import com.clinica.fisioterapia.entity.BloqueoHorario;
import com.clinica.fisioterapia.entity.Fisioterapeuta;
import com.clinica.fisioterapia.repository.BloqueoHorarioRepository;
import com.clinica.fisioterapia.repository.FisioterapeutaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloqueoHorarioService {

    private final BloqueoHorarioRepository bloqueoHorarioRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;

    /**
     * Crear un nuevo bloqueo de horario
     */
    @Transactional
    public BloqueoHorarioDTO crearBloqueo(CrearBloqueoRequest request, Long fisioterapeutaId) {
        // Validar fechas
        if (request.getFechaInicio().isAfter(request.getFechaFin())) {
            throw new RuntimeException("La fecha de inicio debe ser anterior a la fecha de fin");
        }

        // Buscar fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(fisioterapeutaId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        // Crear bloqueo
        BloqueoHorario bloqueo = new BloqueoHorario();
        bloqueo.setFisioterapeuta(fisioterapeuta);
        bloqueo.setFechaInicio(request.getFechaInicio());
        bloqueo.setFechaFin(request.getFechaFin());
        bloqueo.setMotivo(request.getMotivo());
        bloqueo.setTipo(request.getTipo() != null ? request.getTipo() : "PERSONAL");

        BloqueoHorario bloqueoGuardado = bloqueoHorarioRepository.save(bloqueo);

        return convertirABloqueoDTO(bloqueoGuardado);
    }

    /**
     * Obtener todos los bloqueos de un fisioterapeuta
     */
    public List<BloqueoHorarioDTO> getMisBloqueos(Long fisioterapeutaId) {
        List<BloqueoHorario> bloqueos = bloqueoHorarioRepository.findByFisioterapeutaId(fisioterapeutaId);
        return bloqueos.stream()
                .map(this::convertirABloqueoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtener bloqueos en un rango de fechas
     */
    public List<BloqueoHorarioDTO> getBloqueosEnRango(Long fisioterapeutaId, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<BloqueoHorario> bloqueos = bloqueoHorarioRepository.findByFisioterapeutaIdAndFechaBetween(
                fisioterapeutaId, fechaInicio, fechaFin);
        return bloqueos.stream()
                .map(this::convertirABloqueoDTO)
                .collect(Collectors.toList());
    }

    /**
     * Eliminar un bloqueo
     */
    @Transactional
    public void eliminarBloqueo(Long bloqueoId, Long fisioterapeutaId) {
        BloqueoHorario bloqueo = bloqueoHorarioRepository.findById(bloqueoId)
                .orElseThrow(() -> new RuntimeException("Bloqueo no encontrado"));

        // Validar que el bloqueo pertenece al fisioterapeuta
        if (!bloqueo.getFisioterapeuta().getId().equals(fisioterapeutaId)) {
            throw new RuntimeException("No tienes permiso para eliminar este bloqueo");
        }

        bloqueoHorarioRepository.delete(bloqueo);
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Convertir BloqueoHorario a BloqueoHorarioDTO
     */
    private BloqueoHorarioDTO convertirABloqueoDTO(BloqueoHorario bloqueo) {
        return BloqueoHorarioDTO.builder()
                .id(bloqueo.getId())
                .fisioterapeutaId(bloqueo.getFisioterapeuta().getId())
                .fechaInicio(bloqueo.getFechaInicio())
                .fechaFin(bloqueo.getFechaFin())
                .motivo(bloqueo.getMotivo())
                .tipo(bloqueo.getTipo())
                .build();
    }
}
