package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.AuthResponse;
import com.clinica.fisioterapia.dto.LoginRequest;
import com.clinica.fisioterapia.dto.RegisterRequest;
import com.clinica.fisioterapia.entity.Cliente;
import com.clinica.fisioterapia.entity.RolUsuario;
import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.exception.EmailAlreadyExistsException;
import com.clinica.fisioterapia.exception.DniAlreadyExistsException;
import com.clinica.fisioterapia.repository.ClienteRepository;
import com.clinica.fisioterapia.repository.UsuarioRepository;
import com.clinica.fisioterapia.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Validar email único
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El email ya está registrado");
        }

        // Validar DNI único
        if (usuarioRepository.existsByDni(request.getDni())) {
            throw new DniAlreadyExistsException("El DNI ya está registrado");
        }

        // Crear cliente (por defecto los registros son clientes)
        Cliente cliente = new Cliente();
        cliente.setNombre(request.getNombre());
        cliente.setApellidos(request.getApellidos());
        cliente.setDni(request.getDni());
        cliente.setEmail(request.getEmail());
        cliente.setTelefono(request.getTelefono());
        cliente.setPassword(passwordEncoder.encode(request.getPassword()));
        cliente.setRol(RolUsuario.CLIENTE);
        cliente.setActivo(true);
        cliente.setCreatedAt(LocalDateTime.now());
        cliente.setDireccion(request.getDireccion());
        cliente.setFechaNacimiento(request.getFechaNacimiento());

        Cliente savedCliente = clienteRepository.save(cliente);

        // Generar token JWT
        String jwtToken = jwtService.generateToken(savedCliente);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .usuarioId(savedCliente.getId())
                .nombre(savedCliente.getNombre() + " " + savedCliente.getApellidos())
                .email(savedCliente.getEmail())
                .rol(savedCliente.getRol())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        // Autenticar con Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Obtener usuario
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generar token JWT
        String jwtToken = jwtService.generateToken(usuario);

        return AuthResponse.builder()
                .token(jwtToken)
                .tipo("Bearer")
                .usuarioId(usuario.getId())
                .nombre(usuario.getNombre() + " " + usuario.getApellidos())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .build();
    }
}