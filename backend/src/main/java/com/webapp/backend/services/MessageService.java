package com.webapp.backend.services;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.dto.MessageDTO;
import com.webapp.backend.infra.whatsapp.service.WhatsAppMessageService;
import com.webapp.backend.mappers.MessageMapper;
import com.webapp.backend.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


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
        Contact contact = contactosService.getOrCreateContactByWhatsapp(from);
        Message message = messageMapper.toInboundMessage(contact, body);
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

    // ============================================================
    // QUERY: Obtener mensajes de un contacto
    // ============================================================
    public List<Message> getMessagesByContact(Long contactId, User user) {

        // Validar que el contacto existe y pertenece al usuario
        Contact contact = contactosService.getContactoById(contactId);
        if (contact == null) {
            throw new IllegalArgumentException("Contact not found.");
        }

        if (!contact.getCompany().getId().equals(user.getCompany().getId())) {
            throw new SecurityException("You don't have permission to access this contact.");
        }

        // Retornar historial ordenado
        return messageRepository.findByContactIdAndCompanyIdOrderByTimestampDesc(
                contactId,
                user.getCompany().getId()
        );


    }

    @Transactional(readOnly = true)
    public Message getLastMessageForContactAndUser(Long contactId, Long userId) {
        return messageRepository.findTopByContactIdAndUserIdOrderByTimestampDesc(contactId, userId)
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Message getLastMessageForContact(Long contactId) {
        return messageRepository.findTopByContactIdOrderByTimestampDesc(contactId)
                .orElse(null);
    }

    public List<MessageDTO> getLastMessagesUnassigned() {
        List<Message> messages = messageRepository.findLastMessagesOfUnassignedContacts();
        
        return messages.stream().map(messageMapper::toMessageDTO).toList();
    }

    public List<MessageDTO> getLastMessagesOfUser(User user) {
        if (user == null) return Collections.emptyList();
        List<Message> messages = messageRepository.findLastMessagesOfAssignedContacts(user.getId());
        return messages.stream()
                .map(messageMapper::toMessageDTO)
                .toList();
    }

    public List<MessageDTO> getMessagesOfContact(Long contactId) {
        List<Message> messages = messageRepository.findByContactIdOrderByTimestampAsc(contactId);
        return messages.stream()
                .map(messageMapper::toMessageDTO)
                .toList();
    }

}