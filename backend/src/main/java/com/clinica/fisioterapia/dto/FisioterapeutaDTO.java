package com.clinica.fisioterapia.dto;

import com.clinica.fisioterapia.entity.Fisioterapeuta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FisioterapeutaDTO {

    // --- Campos heredados de Usuario (Los necesitamos para mostrar quién es) ---
    private Long id;
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;

    // --- Campos específicos de Fisioterapeuta ---
    private String especialidades;
    private String numeroColegiado;
    private String fotoUrl;
    private BigDecimal valoracionPromedio;
    // La biografía es opcional, si es muy larga a veces se omite en listas,
    // pero para este caso la incluimos.
    private String biografia;

    // --- Constructor o Método Helper para convertir Entidad -> DTO ---
    // Esto facilita mucho la conversión en el Controller o Service
    public static FisioterapeutaDTO fromEntity(Fisioterapeuta f) {
        if (f == null) return null;

        return new FisioterapeutaDTO(
                f.getId(),                 // Heredado de Usuario
                f.getNombre(),             // Heredado de Usuario
                f.getApellidos(),          // Heredado de Usuario
                f.getEmail(),              // Heredado de Usuario
                f.getTelefono(),           // Heredado de Usuario
                f.getEspecialidades(),
                f.getNumeroColegiado(),
                f.getFotoUrl(),
                f.getValoracionPromedio(),
                f.getBiografia()
        );
    }
}