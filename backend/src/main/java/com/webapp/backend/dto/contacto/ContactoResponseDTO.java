package com.webapp.backend.dto.contacto;

import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContactoResponseDTO(
        @Schema(description = "ID único del contacto")
    Long id,
        String name,
        String email,
        String whatsappPhone,
        com.webapp.backend.Entities.enums.FunnelStatus funnelStatus,
        Date dataCreacionContact
) {}