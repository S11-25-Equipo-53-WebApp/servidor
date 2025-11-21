package com.webapp.backend.dto;

public record AuthRegisterRequest(
        String email,
        String password,
        String nombre,
        Long empresaId,
        String tipoUsuario
) {}
