package com.webapp.backend.dto;

public record AuthRegisterResponse(
        Long id,
        String email,
        String nombre,
        String tipoUsuario,
        String token
) {}
