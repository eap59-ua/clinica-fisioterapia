package com.clinica.fisioterapia.controller;

import com.clinica.fisioterapia.entity.Usuario;
import com.clinica.fisioterapia.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
// @CrossOrigin(origins = "http://localhost:5173") // Descomenta si tienes problemas de CORS con Vue
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // LISTAR
    @GetMapping
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(adminService.getUsersList());
    }

    // CREAR
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(adminService.crearUsuario(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // EDITAR
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(adminService.actualizarUsuario(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            adminService.eliminarUsuario(id);
            return ResponseEntity.ok("Usuario eliminado correctamente"); // Respuesta simple texto
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}