package com.webapp.backend.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.webapp.backend.Entities.Contact;
import com.webapp.backend.Entities.Message;
import com.webapp.backend.Entities.User;
import com.webapp.backend.Entities.enums.DirectionMenssage;
import com.webapp.backend.Entities.enums.TypeMessage;
import com.webapp.backend.dto.EmailRequest;
import com.webapp.backend.repository.ContactoRepository;
import com.webapp.backend.repository.MessageRepository;
import com.webapp.backend.repository.UserRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactoRepository contactRepository;

	private static final String SYSTEM_EMAIL_FROM = "mikeljcp@gmail.com";

	@Transactional
	public void sendEmail(EmailRequest request) {
		// Validar e Buscar Entidades (User e Contact)
		User sender = userRepository.findById(request.senderId()).orElseThrow(
				() -> new EntityNotFoundException("Remetente (User) não encontrado com id: " + request.senderId()));

		Contact recipient = contactRepository.findById(request.toId()).orElseThrow(
				() -> new EntityNotFoundException("Destinatário (Contact) não encontrado com id: " + request.toId()));

		SimpleMailMessage message = new SimpleMailMessage();

		// Monta objeto para enviar email
		message.setFrom(SYSTEM_EMAIL_FROM);
		message.setTo(request.to());
		message.setSubject(request.subject());
		message.setText(request.body());

		// Monta e Salva o registro no DB (Log da mensagem)
		saveMessageLog(sender, recipient, request.body());

		mailSender.send(message);
	}

	@Transactional
	public void sendHtmlEmail(EmailRequest request) throws MessagingException {
		// Validar e Buscar Entidades
		User sender = userRepository.findById(request.senderId()).orElseThrow(
				() -> new EntityNotFoundException("Remetente (User) não encontrado com id: " + request.senderId()));

		Contact recipient = contactRepository.findById(request.toId()).orElseThrow(
				() -> new EntityNotFoundException("Destinatário (Contact) não encontrado com id: " + request.toId()));

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

		helper.setFrom(SYSTEM_EMAIL_FROM);
		helper.setTo(request.to());
		helper.setSubject(request.subject());
		helper.setText(request.body(), true); // true indica que es HTML

		mailSender.send(message);

		// Monta e Salva o registro no DB
		saveMessageLog(sender, recipient, request.body());
	}

	private void saveMessageLog(User sender, Contact recipient, String content) {
		Message messageLog = new Message();

		messageLog.setUser(sender);
		messageLog.setContact(recipient);
		// mensagem pertence à empresa do Usuário que enviou
		messageLog.setCompany(sender.getCompany());
		messageLog.setContent(content);
		messageLog.setTimestamp(new Date());
		messageLog.setType(TypeMessage.Email);
		messageLog.setDirection(DirectionMenssage.OUTGOING);

		messageRepository.save(messageLog);
	}

}
