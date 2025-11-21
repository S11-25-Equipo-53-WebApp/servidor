package com.webapp.backend.dto;

public record AuthLoginRequest(
        String email,
        String password
) {}
