package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. LISTAR TODOS (GET)
    // Usamos findAll para ver tanto activos como inactivos en el panel de admin
    public List<Usuario> getUsersList() {
        return usuarioRepository.findAll();
    }

    // 2. CREAR (POST)
    public Usuario crearUsuario(Usuario usuario) {
        // Validamos Email
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        // Validamos DNI (Aprovechando tu repositorio)
        if (usuarioRepository.existsByDni(usuario.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asegurar que se crea activo por defecto (si tu lógica lo requiere)
        usuario.setActivo(true);

        return usuarioRepository.save(usuario);
    }

    // 3. EDITAR (PUT)
    public Usuario actualizarUsuario(Long id, Usuario datosNuevos) {
        Usuario usuarioActual = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar datos básicos
        usuarioActual.setNombre(datosNuevos.getNombre());
        usuarioActual.setApellidos(datosNuevos.getApellidos());
        usuarioActual.setEmail(datosNuevos.getEmail());
        usuarioActual.setDni(datosNuevos.getDni());
        usuarioActual.setTelefono(datosNuevos.getTelefono()); // Asumo que tienes teléfono en la entidad

        // Actualizar Rol (Usando tu Enum RolUsuario)
        usuarioActual.setRol(datosNuevos.getRol());

        // Actualizar estado activo/inactivo
        usuarioActual.setActivo(datosNuevos.isActivo());

        // Solo cambiamos contraseña si viene una nueva y no está vacía
        if (datosNuevos.getPassword() != null && !datosNuevos.getPassword().isEmpty()) {
            usuarioActual.setPassword(passwordEncoder.encode(datosNuevos.getPassword()));
        }

        return usuarioRepository.save(usuarioActual);
    }

    // 4. ELIMINAR (DELETE)
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        // Aquí podrías optar por borrado lógico:
        // Usuario u = usuarioRepository.findById(id).get();
        // u.setActivo(false);
        // usuarioRepository.save(u);

        // O borrado físico (lo que pide el CRUD estándar):
        usuarioRepository.deleteById(id);
    }
}