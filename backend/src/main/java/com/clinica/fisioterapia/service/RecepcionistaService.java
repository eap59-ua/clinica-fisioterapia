package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.entity.*;
import com.clinica.fisioterapia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecepcionistaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final HorarioClinicaRepository horarioRepository;
    private final BloqueoHorarioRepository bloqueoRepository;
    private final ServicioRepository servicioRepository;

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
        if (cita.getEstado() == null) {
            cita.setEstado(EstadoCita.PENDIENTE);
        }

        // 1. OBTENER DATOS REALES DEL SERVICIO (Seguridad)
        // No nos fiamos de la duración que viene del JSON, la buscamos en BD.
        Servicio servicioReal = servicioRepository.findById(cita.getServicio().getId())
                .orElseThrow(() -> new RuntimeException("El servicio especificado no existe"));

        cita.setServicio(servicioReal);

        // Calcular hora fin exacta
        cita.setHoraFin(cita.getHoraInicio().plusMinutes(servicioReal.getDuracionMinutos()));

        // 2. VALIDAR DISPONIBILIDAD (Fisio + Sala + Bloqueos)
        validarDisponibilidad(
                cita.getFisioterapeuta().getId(),
                cita.getSala() != null ? cita.getSala().getId() : null,
                cita.getFecha(),
                cita.getHoraInicio(),
                cita.getHoraFin(),
                null // null porque es nueva cita
        );

        // 3. GUARDAR
        Cita citaGuardada = citaRepository.save(cita);
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

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientes(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return clienteRepository.buscarPorTermino(termino);
    }

    @Transactional
    public CitaDTO actualizarCita(Long id, Cita datos) {
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // 1. ACTUALIZAR CAMPOS BÁSICOS
        citaExistente.setFisioterapeuta(datos.getFisioterapeuta());
        citaExistente.setSala(datos.getSala());
        citaExistente.setFecha(datos.getFecha());
        citaExistente.setHoraInicio(datos.getHoraInicio());
        if (datos.getNotas() != null) citaExistente.setNotas(datos.getNotas());

        // 2. SI CAMBIA EL SERVICIO, RECALCULAR TODO
        if (!citaExistente.getServicio().getId().equals(datos.getServicio().getId())) {
            Servicio nuevoServicio = servicioRepository.findById(datos.getServicio().getId())
                    .orElseThrow(() -> new RuntimeException("Servicio no existe"));
            citaExistente.setServicio(nuevoServicio);

            // Si no se fuerza un precio manual, ponemos el del servicio
            if (datos.getPrecioPagado() == null) {
                citaExistente.setPrecioPagado(nuevoServicio.getPrecio());
            }
        }

        // Permitir sobreescribir precio manualmente si viene en el JSON
        if (datos.getPrecioPagado() != null) {
            citaExistente.setPrecioPagado(datos.getPrecioPagado());
        }

        // 3. RECALCULAR HORA FIN (Siempre, por si cambió hora inicio o servicio)
        int duracion = citaExistente.getServicio().getDuracionMinutos();
        citaExistente.setHoraFin(citaExistente.getHoraInicio().plusMinutes(duracion));

        // 4. VALIDAR DISPONIBILIDAD (Excluyendo la propia cita actual)
        validarDisponibilidad(
                citaExistente.getFisioterapeuta().getId(),
                citaExistente.getSala() != null ? citaExistente.getSala().getId() : null,
                citaExistente.getFecha(),
                citaExistente.getHoraInicio(),
                citaExistente.getHoraFin(),
                id // Pasamos ID para ignorar conflicto consigo misma
        );

        Cita guardada = citaRepository.save(citaExistente);
        return convertirACitaDTO(guardada);
    }

    @Transactional(readOnly = true)
    public CitaDTO obtenerCitaPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return convertirACitaDTO(cita);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerHuecosLibres(LocalDate fecha, Long fisioterapeutaId, Long salaId, int duracionMinutos) {
        List<String> huecos = new ArrayList<>();

        // 1. Obtener Horario Clínica (1=Lunes, 7=Domingo)
        int diaSemana = fecha.getDayOfWeek().getValue();
        Optional<HorarioClinica> horario = horarioRepository.findByDiaSemana(diaSemana);

        if (horario.isEmpty()) return huecos; // Clínica cerrada ese día

        LocalTime apertura = horario.get().getHoraApertura();
        LocalTime cierre = horario.get().getHoraCierre();
        int intervalo = 15; // Slots de 15 min

        // 2. Cargar datos en memoria (Optimización para no hacer queries en el bucle)
        // a) Citas del Fisio
        List<Cita> citasFisio = citaRepository.findCitasFisioterapeutaEnFecha(fisioterapeutaId, fecha);

        // b) Citas de la Sala (Solo si se seleccionó sala)
        List<Cita> citasSala = (salaId != null)
                ? citaRepository.findBySalaIdAndFecha(salaId, fecha)
                : new ArrayList<>();

        // c) Bloqueos del día (Globales y Personales)
        List<BloqueoHorario> bloqueosDia = bloqueoRepository.encontrarBloqueos(
                fisioterapeutaId,
                fecha.atStartOfDay(),
                fecha.atTime(23, 59, 59)
        );

        // 3. Barrido de horas
        LocalTime actual = apertura;
        while (!actual.plusMinutes(duracionMinutos).isAfter(cierre)) {
            LocalTime finSlot = actual.plusMinutes(duracionMinutos);

            // Verificaciones booleanas
            boolean fisioOcupado = haySolape(citasFisio, actual, finSlot, null);
            boolean salaOcupada = (salaId != null) && haySolape(citasSala, actual, finSlot, null);

            boolean bloqueoEncontrado = false;
            if (!fisioOcupado && !salaOcupada) {
                // Verificar Bloqueos (requiere convertir a LocalDateTime)
                LocalDateTime slotInicio = LocalDateTime.of(fecha, actual);
                LocalDateTime slotFin = LocalDateTime.of(fecha, finSlot);

                bloqueoEncontrado = bloqueosDia.stream().anyMatch(b ->
                        b.getFechaInicio().isBefore(slotFin) && b.getFechaFin().isAfter(slotInicio)
                );
            }

            // Si pasa todas las validaciones, es un hueco válido
            if (!fisioOcupado && !salaOcupada && !bloqueoEncontrado) {
                huecos.add(actual.toString());
            }

            actual = actual.plusMinutes(intervalo);
        }

        return huecos;
    }

    /**
     * Valida si se puede agendar. Lanza RuntimeException si hay conflicto.
     */
    private void validarDisponibilidad(Long fisioId, Long salaId, LocalDate fecha, LocalTime inicio, LocalTime fin, Long idExcluir) {

        // 1. Validar Fisioterapeuta
        List<Cita> citasFisio = citaRepository.findCitasFisioterapeutaEnFecha(fisioId, fecha);
        if (haySolape(citasFisio, inicio, fin, idExcluir)) {
            throw new RuntimeException("El fisioterapeuta ya tiene una cita asignada en ese horario.");
        }

        // 2. Validar Sala
        if (salaId != null) {
            List<Cita> citasSala = citaRepository.findBySalaIdAndFecha(salaId, fecha);
            if (haySolape(citasSala, inicio, fin, idExcluir)) {
                throw new RuntimeException("La sala seleccionada está ocupada en ese horario.");
            }
        }

        // 3. Validar Bloqueos / Festivos
        List<BloqueoHorario> bloqueos = bloqueoRepository.encontrarBloqueos(
                fisioId,
                LocalDateTime.of(fecha, inicio),
                LocalDateTime.of(fecha, fin)
        );
        if (!bloqueos.isEmpty()) {
            throw new RuntimeException("Horario no disponible por festivo, vacaciones o bloqueo administrativo.");
        }
    }

    /**
     * Comprueba si hay intersección de horarios en una lista de citas.
     * Algoritmo: (StartA < EndB) && (EndA > StartB)
     */
    private boolean haySolape(List<Cita> citas, LocalTime inicio, LocalTime fin, Long idExcluir) {
        return citas.stream().anyMatch(c -> {
            // Si estamos editando, ignoramos la cita original (idExcluir)
            if (idExcluir != null && c.getId().equals(idExcluir)) return false;

            return c.getHoraInicio().isBefore(fin) && c.getHoraFin().isAfter(inicio);
        });
    }
}