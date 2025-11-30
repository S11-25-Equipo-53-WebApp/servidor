package com.webapp.backend.dto.auth;

public record AuthLoginRequest(
        String email,
        String password
) {}
