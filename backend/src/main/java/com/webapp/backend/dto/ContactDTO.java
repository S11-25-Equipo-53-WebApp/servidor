package com.webapp.backend.dto;

public record ContactDTO(
        Long id,
        String name,
        String whatsappPhone,
        String phone) {}
