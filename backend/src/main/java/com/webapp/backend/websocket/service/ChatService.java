package com.webapp.backend.websocket.service;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.dto.MessageDTO;
import com.webapp.backend.mappers.MessageMapper;
import com.webapp.backend.services.ContactosService;
import com.webapp.backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageService messageService;
    private final ContactosService contactosService;
    private final MessageMapper messageMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // ===========================================================
    // INBOUND: Recibir mensaje entrante (del contacto)
    // ===========================================================
    public void receiveInbound(Long companyId, String from, String body) {

        // 1. Guardar mensaje entrante
        messageService.saveInboundMessage(companyId, from, body);

        // 2. Obtener contacto
        Contact contact = contactosService.getOrCreateContactByWhatsapp(from);

        // 3. Obtener último mensaje del contacto
        Message lastMessage = messageService.getLastMessageForContact(contact.getId());
        if (lastMessage == null) return;

        MessageDTO dto = messageMapper.toMessageDTO(lastMessage);

        // 4. Obtener usuario asignado al contacto
        User assignedUser = contact.getAssignedTo();

        if (assignedUser == null) {
            // No hay usuario asignado -> enviar al topic general de la empresa
            messagingTemplate.convertAndSend("/topic/unassigned", dto);
        } else {
            // Hay usuario asignado -> enviar solo a ese usuario
            messagingTemplate.convertAndSendToUser(
                    assignedUser.getEmail(), // email como identificador único
                    "/topic/contact/" + contact.getId(),
                    dto
            );
        }
    }

    // ===========================================================
    // OUTBOUND: Enviar mensaje desde el usuario al contacto
    // ===========================================================
    public MessageDTO sendOutbound(Long contactId, String body) {

        // 1. Obtener usuario autenticado desde el token
        User currentUser = ((com.webapp.backend.infra.security.userDetails.UserDetailsImpl)
                SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getPrincipal()).user();

        // 2. Guardar mensaje y enviarlo a WhatsApp
        Message saved = messageService.sendMessage(contactId, body, currentUser);

        // 3. Convertir a DTO
        MessageDTO dto = messageMapper.toMessageDTO(saved);

        // 4. Enviar solo al usuario que envió el mensaje (para actualizar su front)
        messagingTemplate.convertAndSendToUser(
                currentUser.getEmail(),
                "/topic/contact/" + contactId,
                dto
        );

        return dto;
    }
}
