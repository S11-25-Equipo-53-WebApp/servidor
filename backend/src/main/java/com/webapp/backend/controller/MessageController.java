package com.webapp.backend.controller;

import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.dto.MessageDTO;
import com.webapp.backend.dto.OutBoundRequest;
import com.webapp.backend.infra.security.userDetails.UserDetailsImpl;
import com.webapp.backend.mappers.MessageMapper;
import com.webapp.backend.services.MessageService;
import com.webapp.backend.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @PostMapping("/outbound")
    public ResponseEntity<MessageDTO> sendOutbound(@RequestBody OutBoundRequest request) {
        // El ChatService obtiene el usuario desde el token internamente
        MessageDTO dto = chatService.sendOutbound(request.id(), request.body());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/unassigned")
    public ResponseEntity<List<MessageDTO>> getLastUnassignedMessages() {
        List<MessageDTO> messages = messageService.getLastMessagesUnassigned();
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<MessageDTO>> getLastMessagesAssignedToUser(Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetailsImpl(User authUser))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<MessageDTO> messages = messageService.getLastMessagesOfUser(authUser);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/contact/{contactId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByContact(@PathVariable Long contactId) {
        List<MessageDTO> messages = messageService.getMessagesOfContact(contactId);
        return ResponseEntity.ok(messages);
    }

}
