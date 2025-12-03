package com.webapp.backend.dto;

public record EmailRequest(
		String to,//email 
		Long toId,//Id destinatario
		String subject, 
		String body,
		Long senderId //cod User remitente
		) {
}
