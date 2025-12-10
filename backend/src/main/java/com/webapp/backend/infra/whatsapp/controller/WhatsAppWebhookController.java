package com.webapp.backend.infra.whatsapp.controller;

import com.webapp.backend.infra.whatsapp.dto.WhatsAppWebhookRequest;
import com.webapp.backend.infra.whatsapp.service.WhatsAppMessageService;
import com.webapp.backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook/whatsapp")
@RequiredArgsConstructor
public class WhatsAppWebhookController {

    @Value("${whatsapp.verify.token}")
    private String verifyToken;

    private final WhatsAppMessageService whatsAppMessageService; // para enviar mensajes
    private final MessageService messageService; // ⚡ servicio interno para guardar mensaje

    @GetMapping
    public String verifyWebhook(
            @RequestParam(name = "hub.mode") String mode,
            @RequestParam(name = "hub.verify_token") String token,
            @RequestParam(name = "hub.challenge") String challenge
    ) {
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return challenge;
        }
        return "Invalid.";
    }

    @PostMapping
    public void receiveMessage(@RequestBody WhatsAppWebhookRequest request) {
        System.out.println("🔥 RAW BODY:");
        System.out.println(request);

        if (request.entry() == null || request.entry().isEmpty()) {
            System.out.println("Webhook vacío: sin entries");
            return;
        }

        var entry = request.entry().getFirst();

        if (entry.changes() == null || entry.changes().isEmpty()) {
            System.out.println("Webhook sin changes. Ignorado.");
            return;
        }

        var change = entry.changes().getFirst();
        var value = change.value();

        if (value == null || value.messages() == null || value.messages().isEmpty()) {
            System.out.println("Evento recibido sin mensajes. Ignorado.");
            return;
        }

        var message = value.messages().getFirst();

        String from = message.from();
        System.out.println("Mensaje de: " + from);
        String body = message.text() != null ? message.text().body() : "(mensaje no textual)";

        System.out.println("📩 Mensaje recibido de " + from + ": " + body);

        // ⚡ GUARDA EN TU BASE DE DATOS
        Long companyId = 1L; // <-- en un futuro lo obtendrás del header o del token
        messageService.saveInboundMessage(companyId, from, body);

        // Respuesta automática opcional
        whatsAppMessageService.sendTextMessage(from, "Hola! Hemos recibido tu mensaje 😊");
    }
}
