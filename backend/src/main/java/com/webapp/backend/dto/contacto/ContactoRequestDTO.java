package com.webapp.backend.dto.contacto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank; // Se usar validação

public record ContactoRequestDTO(
    @Schema(description = "Nombre del contacto", example = "Juan Perez")
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @Schema(description = "Correo electrónico", example = "juan@example.com")
    String email,

    @Schema(description = "Teléfono o Whatsapp", example = "+5511999999999")
    String whatsappPhone,
    
    //IDs de las relaciones
    @Schema(description = "ID del Usuario responsable", example = "1")
    Long userId,

    @Schema(description = "ID de la Empresa", example = "5")
    Long companyId
) {}