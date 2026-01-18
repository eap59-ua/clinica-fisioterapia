package com.clinica.fisioterapia.integration.tpv;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "tpv")
public class TpvConfig {

    /**
     * URL base del TPV externo
     * Ejemplo: https://tpv-backend-cbbg.onrender.com
     */
    private String baseUrl = "https://tpv-backend-cbbg.onrender.com";

    /**
     * ID del comercio asignado por el TPV
     */
    private String merchantId = "CLINICA_FISIO_001";

    /**
     * API Key para autenticación
     */
    private String apiKey = "YOUR_API_KEY_HERE";

    /**
     * Secret para firmar peticiones
     */
    private String apiSecret = "YOUR_API_SECRET_HERE";

    /**
     * URL de callback (tu backend) para recibir notificaciones
     */
    private String callbackUrl = "http://localhost:8080/api/pagos/callback";

    /**
     * URL de redirección tras pago exitoso (frontend)
     */
    private String successUrl = "http://localhost:5173/cliente/pago-exitoso";

    /**
     * URL de redirección tras pago fallido (frontend)
     */
    private String errorUrl = "http://localhost:5173/cliente/pago-error";

    /**
     * Timeout para conexiones HTTP (ms)
     */
    private int connectionTimeout = 5000;

    /**
     * Timeout para lectura HTTP (ms)
     */
    private int readTimeout = 10000;

    /**
     * Habilitar/deshabilitar integración (modo sandbox)
     */
    private boolean enabled = true;

    /**
     * Modo simulación (para pruebas sin TPV real)
     */
    private boolean mockMode = true;
}
