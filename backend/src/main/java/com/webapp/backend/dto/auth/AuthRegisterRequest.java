package com.webapp.backend.dto.auth;

public record AuthRegisterRequest(
        String email,
        String password,
        String fullName,
        Long companyId,
        String typeUser
) {}
