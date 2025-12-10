package com.webapp.backend.services;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.infra.whatsapp.service.WhatsAppMessageService;
import com.webapp.backend.mappers.MessageMapper;
import com.webapp.backend.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final ContactosService contactosService;
    private final MessageMapper messageMapper = new MessageMapper();
    private final WhatsAppMessageService whatsAppMessageService;

    // ============================================================
    // INBOUND (mensaje entrante)
    // ============================================================
    @Transactional
    public void saveInboundMessage(Long companyId, String from, String body) {
        // Buscar contacto existente o crearlo
        Contact contact = contactosService.getOrCreateContactByWhatsapp(from);

        // Crear mensaje inbound
        Message message = messageMapper.toInboundMessage(contact, body);

        // Guardar mensaje
        messageRepository.save(message);
    }


    // ============================================================
    // OUTBOUND (mensaje saliente)
    // ============================================================
    @Transactional
    public Message sendMessage(Long contactId, String body, User user) {

        Contact contact = contactosService.getContactoById(contactId);

        if (contact == null || !contact.getCompany().getId().equals(user.getCompany().getId())) {
            throw new RuntimeException("Contact not found or does not belong to your company.");
        }

        Message message = messageMapper.toOutboundMessage(contact, body, user);

        messageRepository.save(message);

        whatsAppMessageService.sendTextMessage(contact.getWhatsappPhone(), body);

        return message;
    }

}