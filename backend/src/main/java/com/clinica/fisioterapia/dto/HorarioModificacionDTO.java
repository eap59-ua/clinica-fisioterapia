package com.clinica.fisioterapia.dto;

import lombok.Data;
@Data
public class HorarioModificacionDTO {
    private int diaSemana;

    private String horaApertura;
    private String horaCierre;
}