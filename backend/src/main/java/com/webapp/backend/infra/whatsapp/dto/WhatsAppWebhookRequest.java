package com.webapp.backend.infra.whatsapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // evita errores si llegan otros campos
public record WhatsAppWebhookRequest(
        List<Entry> entry
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Entry(
            List<Change> changes
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Change(
            Value value
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Value(
            List<Message> messages
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(
            String from,
            Text text
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Text(
            String body
    ) {}
}
