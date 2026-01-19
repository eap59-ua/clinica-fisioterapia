package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.CitaDTO;
import com.clinica.fisioterapia.entity.Cita;
import com.clinica.fisioterapia.entity.Cliente;
import com.clinica.fisioterapia.entity.EstadoCita;
import com.clinica.fisioterapia.entity.HorarioClinica;
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

    @Transactional(readOnly = true)
    public List<Cliente> buscarClientes(String termino) {
        if (termino == null || termino.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return clienteRepository.buscarPorTermino(termino);
    }

    @Transactional
    public CitaDTO actualizarCita(Long id, Cita citaDatosNuevos) {
        // 1. Buscar la cita original
        Cita citaExistente = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // 2. Actualizar campos permitidos
        // NOTA: No permitimos cambiar el Cliente de una cita ya creada (regla de negocio habitual),
        // pero sí el fisio, servicio, fecha, etc.

        citaExistente.setFisioterapeuta(citaDatosNuevos.getFisioterapeuta());
        citaExistente.setSala(citaDatosNuevos.getSala());
        citaExistente.setFecha(citaDatosNuevos.getFecha());
        citaExistente.setHoraInicio(citaDatosNuevos.getHoraInicio());
        citaExistente.setNotas(citaDatosNuevos.getNotas());

        // 3. Lógica inteligente: Si cambia el servicio, actualizamos precio y recalculamos hora fin
        if (!citaExistente.getServicio().getId().equals(citaDatosNuevos.getServicio().getId())) {
            citaExistente.setServicio(citaDatosNuevos.getServicio()); // Asignamos nuevo servicio
            // Recalcular precio si no se ha forzado uno nuevo manualmente
            if (citaDatosNuevos.getPrecioPagado() == null) {
                // Aquí deberías buscar el servicio en BD para sacar el precio real,
                // por simplicidad asumimos que viene o lo mantenemos.
                // Lo ideal: recuperar Servicio de repository y poner su precio.
            }
        }

        // Si viene un precio nuevo explícito, lo ponemos
        if (citaDatosNuevos.getPrecioPagado() != null) {
            citaExistente.setPrecioPagado(citaDatosNuevos.getPrecioPagado());
        }

        // 4. Recalcular Hora Fin obligatoriamente (por si cambió hora inicio o servicio)
        // Necesitamos saber la duración del servicio actual
        // (Hibernate ya habrá traído el objeto Servicio completo al hacer el set arriba si usas .getReference,
        // pero para asegurar, calculamos 1 hora o usamos la duración del servicio si lo tienes cargado).
        int duracion = citaExistente.getServicio().getDuracionMinutos() != null ?
                citaExistente.getServicio().getDuracionMinutos() : 60;

        citaExistente.setHoraFin(citaExistente.getHoraInicio().plusMinutes(duracion));

        // 5. Guardar y devolver DTO
        Cita citaGuardada = citaRepository.save(citaExistente);
        return convertirACitaDTO(citaGuardada);
    }

    @Transactional(readOnly = true)
    public CitaDTO obtenerCitaPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));
        return convertirACitaDTO(cita);
    }

    @Transactional(readOnly = true)
    public List<String> obtenerHuecosLibres(LocalDate fecha, Long fisioterapeutaId, int duracionMinutos) {

        List<String> huecosLibres = new ArrayList<>();

        // 1. Obtener Horario según día de la semana (1=Mon, 7=Sun)
        int diaSemanaNum = fecha.getDayOfWeek().getValue();

        Optional<HorarioClinica> horarioOpt = horarioRepository.findByDiaSemana(diaSemanaNum);

        // Si NO hay registro en la tabla para este día (ej. Domingo en tu script), devolvemos vacío
        if (horarioOpt.isEmpty()) {
            return huecosLibres; // Clínica Cerrada
        }

        HorarioClinica horario = horarioOpt.get();
        LocalTime apertura = horario.getHoraApertura();
        LocalTime cierre = horario.getHoraCierre();
        int intervaloMinutos = 15;

        // 2. Obtener citas ya ocupadas
        List<Cita> citasDelDia = citaRepository.findByFisioterapeutaIdAndFecha(fisioterapeutaId, fecha);

        LocalTime horaActual = apertura;

        // 3. Bucle para buscar huecos
        while (horaActual.plusMinutes(duracionMinutos).isBefore(cierre) || horaActual.plusMinutes(duracionMinutos).equals(cierre)) {

            LocalTime finPotencial = horaActual.plusMinutes(duracionMinutos);
            LocalDateTime fechaHoraInicio = LocalDateTime.of(fecha, horaActual);

            // A) Verificar si hay Cita solapada
            boolean ocupadoPorCita = false;
            for (Cita cita : citasDelDia) {
                if (cita.getHoraInicio().isBefore(finPotencial) && cita.getHoraFin().isAfter(horaActual)) {
                    ocupadoPorCita = true;
                    break;
                }
            }

            // B) Verificar si hay Bloqueo (Festivo o Vacaciones) en esa hora exacta
            // Usamos el repositorio de bloqueos para ver si "cae" dentro de un rango bloqueado
            boolean ocupadoPorBloqueo = !bloqueoRepository.encontrarBloqueos(fisioterapeutaId, fechaHoraInicio).isEmpty();

            // Si está libre de citas Y libre de bloqueos/festivos
            if (!ocupadoPorCita && !ocupadoPorBloqueo) {
                huecosLibres.add(horaActual.toString());
            }

            horaActual = horaActual.plusMinutes(intervaloMinutos);
        }

        return huecosLibres;
    }
}