package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.dto.ProfileResponse;
import com.clinica.fisioterapia.dto.ProfileUpdateRequest;
import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping({"/perfil", "/api/perfil"})
@RequiredArgsConstructor
public class PerfilController {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseEntity<ProfileResponse> getPerfil() {
        Usuario usuario = getAuthenticatedUser();

        ProfileResponse response = ProfileResponse.builder()
                .id(usuario.getId())
                .nombre(usuario.getNombre())
                .apellidos(usuario.getApellidos())
                .dni(usuario.getDni())
                .email(usuario.getEmail())
                .telefono(usuario.getTelefono())
                .rol(usuario.getRol().name())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<?> updatePerfil(@Valid @RequestBody ProfileUpdateRequest request) {
        Usuario usuario = getAuthenticatedUser();

        // Verificar si el email ya está en uso por otro usuario
        if (request.getEmail() != null && !request.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.existsByEmail(request.getEmail())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "El email ya está en uso por otro usuario");
                return ResponseEntity.badRequest().body(error);
            }
            usuario.setEmail(request.getEmail());
        }

        // Actualizar campos básicos
        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }

        if (request.getApellidos() != null) {
            usuario.setApellidos(request.getApellidos());
        }

        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }

        // Cambio de contraseña (opcional)
        if (request.getNewPassword() != null && !request.getNewPassword().isEmpty()) {
            // Verificar contraseña actual
            if (request.getCurrentPassword() == null ||
                !passwordEncoder.matches(request.getCurrentPassword(), usuario.getPassword())) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "La contraseña actual no es correcta");
                return ResponseEntity.badRequest().body(error);
            }
            usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }

        try {
            Usuario updated = usuarioRepository.save(usuario);

            ProfileResponse response = ProfileResponse.builder()
                    .id(updated.getId())
                    .nombre(updated.getNombre())
                    .apellidos(updated.getApellidos())
                    .dni(updated.getDni())
                    .email(updated.getEmail())
                    .telefono(updated.getTelefono())
                    .rol(updated.getRol().name())
                    .createdAt(updated.getCreatedAt())
                    .updatedAt(updated.getUpdatedAt())
                    .build();

            return ResponseEntity.ok(response);
        } catch (DataIntegrityViolationException e) {
            log.error("Error de integridad al actualizar perfil: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            if (e.getMessage() != null && e.getMessage().contains("email")) {
                error.put("message", "El email ya está en uso por otro usuario");
            } else if (e.getMessage() != null && e.getMessage().contains("telefono")) {
                error.put("message", "El teléfono ya está en uso por otro usuario");
            } else {
                error.put("message", "Error al actualizar el perfil. Verifica que los datos sean correctos.");
            }
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    private Usuario getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
