package com.clinica.fisioterapia.dto;

import com.clinica.fisioterapia.entity.RolUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String nombre;
    private String email;
    private RolUsuario rol;
}