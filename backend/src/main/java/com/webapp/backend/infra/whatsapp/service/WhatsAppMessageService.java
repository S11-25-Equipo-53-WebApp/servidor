package com.webapp.backend.infra.whatsapp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WhatsAppMessageService {

    private final WebClient whatsappWebClient;
    private final String phoneNumberId;

    public void sendTextMessage(String to, String message) {
        var payload = """
        {
            "messaging_product": "whatsapp",
            "to": "%s",
            "type": "text",
            "text": { "body": "%s" }
        }
        """.formatted(to, message);

        whatsappWebClient.post()
                .uri("/" + phoneNumberId + "/messages")
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(err -> System.err.println("Error enviando mensaje a WhatsApp: " + err.getMessage()))
                .subscribe(response -> System.out.println("WhatsApp response: " + response));

    }
}
