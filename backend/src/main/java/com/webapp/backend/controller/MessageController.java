package com.webapp.backend.controller;

import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.dto.MessageDTO;
import com.webapp.backend.dto.OutBoundRequest;
import com.webapp.backend.infra.security.userDetails.UserDetailsImpl;
import com.webapp.backend.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/outbound")
    public ResponseEntity<MessageDTO> sendOutbound(
            @RequestBody OutBoundRequest request,
            @AuthenticationPrincipal UserDetailsImpl principal
    ) {
        Message saved = messageService.sendMessage(request.id(), request.body(), principal.user());
        return ResponseEntity.ok(messageMapper.toMessageDTO(saved));
    }


        return ResponseEntity.ok(saved);
    }




}

