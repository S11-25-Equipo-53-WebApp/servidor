package com.webapp.backend.dto.auth;

public record AuthRegisterResponse(
        Long id,
        String email,
        String fullName,
        String typeUser,
        String token
) {}
