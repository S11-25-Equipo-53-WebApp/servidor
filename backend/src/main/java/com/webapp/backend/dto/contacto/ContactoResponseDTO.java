package com.webapp.backend.dto.contacto;

import java.util.Date;
import com.webapp.backend.Entities.enums.EstadoFunnel; // Importe seu Enum
import io.swagger.v3.oas.annotations.media.Schema;

public record ContactoResponseDTO(
    @Schema(description = "ID único del contacto")
    Long id,
    String nombre,
    String email,
    String telefonoWhatsapp,
    EstadoFunnel estadoFunnel,
    Date dataCreacionContacto
) {}