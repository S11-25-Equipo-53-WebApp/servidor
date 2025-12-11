package com.webapp.backend.mappers;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.Entities.enums.DirectionMenssage;
import com.webapp.backend.Entities.enums.TypeMessage;
import com.webapp.backend.dto.ContactDTO;
import com.webapp.backend.dto.MessageDTO;
import com.webapp.backend.dto.UserDTO;
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

    public MessageDTO toMessageDTO(Message message) {
        if (message == null) return null;

        // Convertimos el contacto a DTO
        ContactDTO contactDTO = new ContactDTO(
                message.getContact().getId(),
                message.getContact().getName(),
                message.getContact().getWhatsappPhone(),
                message.getContact().getPhone()
        );

        // Convertimos el usuario a DTO si existe
        UserDTO userDTO = null;
        if (message.getUser() != null) {
            userDTO = new UserDTO(
                    message.getUser().getId(),
                    message.getUser().getFullName(),
                    message.getUser().getEmail()
            );
        }

        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getDirection().name(),
                message.getTimestamp(),
                contactDTO,
                userDTO
        );
    }

}
