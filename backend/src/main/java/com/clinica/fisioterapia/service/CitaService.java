package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.dto.DisponibilidadDTO;
import com.clinica.fisioterapia.dto.ReservarCitaRequest;
import com.clinica.fisioterapia.entity.*;
import com.clinica.fisioterapia.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final ServicioRepository servicioRepository;
    private final FisioterapeutaRepository fisioterapeutaRepository;
    private final ClienteRepository clienteRepository;

    // Horarios de la clínica
    private static final LocalTime HORA_APERTURA = LocalTime.of(9, 0);
    private static final LocalTime HORA_CIERRE_LABORAL = LocalTime.of(20, 0);
    private static final LocalTime HORA_CIERRE_SABADO = LocalTime.of(14, 0);
    private static final int INTERVALO_MINUTOS = 30;

    @Transactional
    public CitaDTO reservarCita(ReservarCitaRequest request, Long clienteId) {
        // Validar cliente
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar servicio
        Servicio servicio = servicioRepository.findById(request.getServicioId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        if (!servicio.getActivo()) {
            throw new RuntimeException("El servicio no está disponible");
        }

        // Validar fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(request.getFisioterapeutaId())
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        if (!fisioterapeuta.getActivo()) {
            throw new RuntimeException("El fisioterapeuta no está disponible");
        }

        // Calcular hora fin basada en duración del servicio
        LocalTime horaFin = request.getHoraInicio().plusMinutes(servicio.getDuracionMinutos());

        // Validar horario de clínica
        validarHorarioClinica(request.getFecha(), request.getHoraInicio(), horaFin);

        // Verificar disponibilidad del fisioterapeuta
        List<Cita> conflictos = citaRepository.findConflictingCitas(
                request.getFisioterapeutaId(),
                request.getFecha(),
                request.getHoraInicio(),
                horaFin
        );

        if (!conflictos.isEmpty()) {
            throw new RuntimeException("El fisioterapeuta no está disponible en ese horario");
        }

        // Crear la cita
        Cita cita = new Cita();
        cita.setCliente(cliente);
        cita.setFisioterapeuta(fisioterapeuta);
        cita.setServicio(servicio);
        cita.setFecha(request.getFecha());
        cita.setHoraInicio(request.getHoraInicio());
        cita.setHoraFin(horaFin);
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setNotas(request.getNotas());
        cita.setPrecioPagado(servicio.getPrecio());

        Cita citaGuardada = citaRepository.save(cita);

        return convertirACitaDTO(citaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> getMisCitas(Long clienteId) {
        List<Cita> citas = citaRepository.findByClienteIdOrderByFechaDesc(clienteId);
        return citas.stream()
                .map(this::convertirACitaDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> getProximasCitas(Long clienteId) {
        LocalDate hoy = LocalDate.now();
        List<Cita> citas = citaRepository.findProximasCitasCliente(clienteId, hoy);
        return citas.stream()
                .map(this::convertirACitaDTO)
                .toList();
    }

    @Transactional
    public void cancelarCita(Long citaId, Long clienteId) {
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // Verificar que la cita pertenece al cliente
        if (!cita.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("No tiene permiso para cancelar esta cita");
        }

        // Verificar que la cita esté pendiente
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new RuntimeException("Solo se pueden cancelar citas pendientes");
        }

        // Verificar regla de 24 horas
        LocalDateTime fechaHoraCita = LocalDateTime.of(cita.getFecha(), cita.getHoraInicio());
        LocalDateTime ahora = LocalDateTime.now();

        if (fechaHoraCita.minusHours(24).isBefore(ahora)) {
            throw new RuntimeException("No se puede cancelar con menos de 24 horas de anticipación");
        }

        // Cancelar la cita
        cita.setEstado(EstadoCita.CANCELADA);
        citaRepository.save(cita);
    }

    @Transactional(readOnly = true)
    public DisponibilidadDTO getDisponibilidad(Long fisioId, Long servicioId, LocalDate fecha) {
        // Validar fisioterapeuta
        Fisioterapeuta fisioterapeuta = fisioterapeutaRepository.findById(fisioId)
                .orElseThrow(() -> new RuntimeException("Fisioterapeuta no encontrado"));

        // Validar servicio
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Verificar que no sea domingo
        if (fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return DisponibilidadDTO.builder()
                    .fecha(fecha)
                    .fisioterapeutaId(fisioId)
                    .slots(new ArrayList<>())
                    .build();
        }

        // Obtener citas existentes del fisioterapeuta en esa fecha
        List<Cita> citasExistentes = citaRepository.findCitasFisioterapeutaEnFecha(fisioId, fecha);

        // Generar slots de tiempo
        List<DisponibilidadDTO.SlotHorario> slots = generarSlots(fecha, servicio.getDuracionMinutos(), citasExistentes);

        return DisponibilidadDTO.builder()
                .fecha(fecha)
                .fisioterapeutaId(fisioId)
                .slots(slots)
                .build();
    }

    private void validarHorarioClinica(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        DayOfWeek diaSemana = fecha.getDayOfWeek();

        // Verificar que no sea domingo
        if (diaSemana == DayOfWeek.SUNDAY) {
            throw new RuntimeException("La clínica está cerrada los domingos");
        }

        // Verificar hora de apertura
        if (horaInicio.isBefore(HORA_APERTURA)) {
            throw new RuntimeException("La clínica abre a las " + HORA_APERTURA);
        }

        // Verificar hora de cierre según el día
        LocalTime horaCierre = diaSemana == DayOfWeek.SATURDAY ? HORA_CIERRE_SABADO : HORA_CIERRE_LABORAL;

        if (horaFin.isAfter(horaCierre)) {
            throw new RuntimeException("La clínica cierra a las " + horaCierre + " los " +
                    (diaSemana == DayOfWeek.SATURDAY ? "sábados" : "días laborales"));
        }
    }

    private List<DisponibilidadDTO.SlotHorario> generarSlots(LocalDate fecha, Integer duracionServicio, List<Cita> citasExistentes) {
        List<DisponibilidadDTO.SlotHorario> slots = new ArrayList<>();

        DayOfWeek diaSemana = fecha.getDayOfWeek();
        LocalTime horaCierre = diaSemana == DayOfWeek.SATURDAY ? HORA_CIERRE_SABADO : HORA_CIERRE_LABORAL;

        LocalTime horaActual = HORA_APERTURA;

        while (horaActual.plusMinutes(duracionServicio).isBefore(horaCierre) ||
               horaActual.plusMinutes(duracionServicio).equals(horaCierre)) {

            LocalTime horaFin = horaActual.plusMinutes(duracionServicio);
            boolean disponible = esSlotDisponible(horaActual, horaFin, citasExistentes);

            slots.add(DisponibilidadDTO.SlotHorario.builder()
                    .horaInicio(horaActual)
                    .horaFin(horaFin)
                    .disponible(disponible)
                    .build());

            horaActual = horaActual.plusMinutes(INTERVALO_MINUTOS);
        }

        return slots;
    }

    private boolean esSlotDisponible(LocalTime horaInicio, LocalTime horaFin, List<Cita> citasExistentes) {
        for (Cita cita : citasExistentes) {
            // Verificar solapamiento
            if ((horaInicio.isBefore(cita.getHoraFin()) && horaFin.isAfter(cita.getHoraInicio()))) {
                return false;
            }
        }
        return true;
    }

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
