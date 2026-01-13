package com.clinica.fisioterapia.service;

import com.clinica.fisioterapia.dto.AuthResponse;
import com.clinica.fisioterapia.dto.LoginRequest;
import com.clinica.fisioterapia.dto.RegisterRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Service
public class AdminService {

    // Método básico solicitado en la planificación
    public List<Object> getUsersList() {
        // TODO: En el siguiente paso implementaremos la lógica real con el Repositorio
        return Collections.emptyList();
    }

    @RestController
    @RequestMapping("/auth")
    @RequiredArgsConstructor
    public static class AuthController {

        private final AuthService authService;

        @PostMapping("/register")
        public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
            AuthResponse response = authService.register(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        @PostMapping("/login")
        public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        }

        public static class AdminController {
        }
    }
}