package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.entity.Servicio;
import com.clinica.fisioterapia.repository.ServicioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiciosService {

    private final ServicioRepository servicioRepository;

    public AdminServiciosService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public List<Servicio> listarTodos() {
        return servicioRepository.findAll();
    }

    public Servicio guardarServicio(Servicio servicio) {
        // Aquí podrías validar si el nombre ya existe, etc.
        return servicioRepository.save(servicio);
    }

    public Servicio actualizarServicio(Long id, Servicio datos) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        servicio.setNombre(datos.getNombre());
        servicio.setDescripcion(datos.getDescripcion());
        servicio.setPrecio(datos.getPrecio());
        servicio.setDuracionMinutos(datos.getDuracionMinutos());
        servicio.setActivo(datos.getActivo());

        return servicioRepository.save(servicio);
    }

    public void eliminarServicio(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado");
        }
        servicioRepository.deleteById(id);
    }
}