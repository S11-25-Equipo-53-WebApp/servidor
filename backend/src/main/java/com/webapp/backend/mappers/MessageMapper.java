package com.webapp.backend.mappers;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.Entities.enums.DirectionMenssage;
import com.webapp.backend.Entities.enums.TypeMessage;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MessageMapper {

    // ============================================================
    // INBOUND MESSAGE (entrante)
    // ============================================================
    public Message toInboundMessage(Contact contact, String body) {
        return Message.builder()
                .contact(contact)
                .company(contact.getCompany())
                .user(null) // Mensaje entrante no tiene usuario
                .content(body)
                .type(TypeMessage.WHATSAPP)
                .direction(DirectionMenssage.INCOMING)
                .timestamp(new Date())
                .build();
    }

    // ============================================================
    // OUTBOUND MESSAGE (saliente)
    // ============================================================
    public Message toOutboundMessage(Contact contact, String body, User user) {
        return Message.builder()
                .contact(contact)
                .company(contact.getCompany())
                .user(user)               // <-- usuario que envió el mensaje
                .content(body)
                .type(TypeMessage.WHATSAPP)
                .direction(DirectionMenssage.OUTGOING)
                .timestamp(new Date())
                .build();
    }

}
