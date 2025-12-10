package com.webapp.backend.dto;

import java.util.Date;

public record MessageDTO(
        Long id,
        String content,
        String direction,
        Date timestamp,
        ContactDTO contact
) {}
