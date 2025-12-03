package com.webapp.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.backend.dto.EmailRequest;
import com.webapp.backend.services.EmailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/email")
@Tag(name = "Emails", description = "Endpoints para la gestión de emails")
public class EmailController {

	@Autowired
	private EmailService emailService;

	@PostMapping("/send-text")
	@Operation(summary = "Enviar correo de texto plano", description = "Envía un correo electrónico simple utilizando solo texto plano. Ideal para notificaciones básicas.", responses = {
			@ApiResponse(responseCode = "200", description = "E-mail enviado con éxito.", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar enviar el e-mail (ej. problema de conexión SMTP).") })
	public ResponseEntity<String> sendTextEmail(@RequestBody EmailRequest emailRequest) {
		try {
			emailService.sendEmail(emailRequest);
			return ResponseEntity.ok("E-mail enviado con exito.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("E-mail no enviado, Contacte al administrador. " + e.getMessage());
		}
	}

	@PostMapping("/send-html")
	@Operation(summary = "Enviar correo HTML", description = "Envía un correo electrónico con contenido enriquecido en formato HTML. Requiere que el campo 'body' contenga código HTML válido.", responses = {
			@ApiResponse(responseCode = "200", description = "E-mail HTML enviado con éxito.", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor al intentar enviar el e-mail. Verifique que el cuerpo JSON sea válido.") })
	public ResponseEntity<String> sendHtmlEmail(@RequestBody EmailRequest emailRequest) {
		try {
			emailService.sendHtmlEmail(emailRequest);
			return ResponseEntity.ok("E-mail Html enviado con exito.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("E-mail no enviado, Contacte al administrador. " + e.getMessage());
		}
	}

}
