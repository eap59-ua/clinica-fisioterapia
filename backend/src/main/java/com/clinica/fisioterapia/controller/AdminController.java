package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/admin") // Esto define la ruta base para todo el controlador
public class AdminController {

    private final AdminService adminService;

    // Inyección de dependencias por constructor
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // Endpoint de prueba para listar usuarios
    @GetMapping("/usuarios")
    public ResponseEntity<List<Object>> listarUsuarios() {
        return ResponseEntity.ok(adminService.getUsersList());
    }
}